package space.bunniesin.crescent.api

import android.util.Log
import space.bunniesin.crescent.models.api.authentication.EmailSessionRequest
import space.bunniesin.crescent.models.api.authentication.MFAResponse
import space.bunniesin.crescent.models.api.authentication.MFASessionRequest
import space.bunniesin.crescent.models.api.authentication.SessionResponse
import space.bunniesin.crescent.models.api.channels.Channel
import space.bunniesin.crescent.models.api.websocket.BaseEvent
import space.bunniesin.crescent.models.api.websocket.PartialMessage
import space.bunniesin.crescent.models.api.websocket.PingEvent
import space.bunniesin.crescent.models.api.websocket.SystemMessage
import space.bunniesin.crescent.models.api.websocket.UnimplementedEvent
import space.bunniesin.crescent.utilities.EventBus
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.wss
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@OptIn(DelicateCoroutinesApi::class)
private fun intervalPing(ws: DefaultWebSocketSession) = GlobalScope.launch {
    while (true) {
        delay(20 * 1000)
        Log.d("Socket", "Pinging!")
        ws.send(ApiClient.jsonDeserializer.encodeToString(PingEvent(1)))
    }
}

object ApiClient {
    var useStaging = false
        set(value) {
            when (value) {
                true -> {
                    API_ROOT_URL = "https://revolt.chat/api/"
                    SOCKET_ROOT_URL = "wss://revolt.chat/events/?format=json&version=1"
                }

                false -> {
                    API_ROOT_URL = "https://api.revolt.chat"
                    SOCKET_ROOT_URL = "wss://ws.revolt.chat?format=json&version=1"

                }
            }
            field = value
        }
    private var SOCKET_ROOT_URL: String = "wss://ws.revolt.chat?version=1&format=json"
    private var API_ROOT_URL: String = "https://api.revolt.chat/"
    // EDIT: https://autumn.revolt.chat is moving to https://cdn.revoltusercontent.com
    // Learn more at https://revolt.chat/updates/api-new-cdn
    const val S3_ROOT_URL: String = "https://cdn.revoltusercontent.com/"
    private var currentIntervalJob: Job? = null
    var currentSession: SessionResponse.Success? = null
    private var websocket: DefaultWebSocketSession? = null
    val jsonDeserializer = Json {
        ignoreUnknownKeys = true
        isLenient = true
        serializersModule = SerializersModule {
            polymorphic(BaseEvent::class) {
                defaultDeserializer { UnimplementedEvent.serializer() }
            }

            polymorphic(SystemMessage::class) {
                defaultDeserializer { SystemMessage.UnimplementedMessage.serializer() }
            }
        }
    }

    var cache = mutableMapOf<String, Any>()

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(jsonDeserializer)
        }
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(jsonDeserializer)
        }
    }

    suspend fun getDirectMessages(): List<Channel> {
        val res = client.get("$API_ROOT_URL/users/dms") {
            headers {
                append("X-Session-Token", currentSession?.userToken ?: "")
            }

            accept(ContentType.Application.Json)
        }.body<List<Channel>>()


        res.forEach {
            cache[it.id] = it
        }

        Log.d("Client", "Direct Messages: $res")
        Log.d("Cache", "Cache size: ${cache.size}")
        return res
    }

    suspend fun getSpecificMessageFromChannel(
        channel: Channel, messageId: String
    ): PartialMessage? {
        return try {
            val res = client.get("$API_ROOT_URL/channel/${channel.id}/messages/${messageId}") {
                headers {
                    append("X-Session-Token", currentSession?.userToken ?: "")
                }

                accept(ContentType.Application.Json)
            }.body<PartialMessage>()

            cache[res.id!!] = res

            Log.d("Cache", "Cache size: ${cache.size}")
            return res
        } catch (e: Exception) {
            Log.e("Client", "Fuck, $e")
            null
        }
    }

    suspend fun getChannelMessages(channelId: String): List<PartialMessage> {
        val channel = cache[channelId] as Channel
        Log.d("Cache", "Found Channel: $channel")
        val url = "${API_ROOT_URL}channels/${channel.id}/messages?limit=30"
        val res = client.get(url) {
            headers {
                append("X-Session-Token", currentSession?.userToken ?: "")
            }

            accept(ContentType.Application.Json)
        }.body<List<PartialMessage>>()


        Log.d("Cache", "Cache size: ${cache.size}")

        return res
    }

    suspend fun sendMessage(location: String, message: String) {
        val channel = cache[location] as Channel

        val url = "${API_ROOT_URL}channels/${channel.id}/messages"
        client.post(url) {
            headers {
                append("X-Session-Token", currentSession?.userToken ?: "")
            }

            contentType(ContentType.Application.Json)
            setBody(PartialMessage(content = message))
        }
    }

    suspend fun loginWithPassword(email: String, password: String): SessionResponse {
        // TODO: error handling
        val response = client.post("$API_ROOT_URL/auth/session/login") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)

            setBody(EmailSessionRequest(email, password))
        }.body<SessionResponse>()

        if (response is SessionResponse.Success) {
            startSession(response)
        }

        return response
    }

    suspend fun confirm2FA(ticket: String, code: String): SessionResponse {
        val response = client.post("$API_ROOT_URL/auth/session/login") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)

            setBody(MFASessionRequest(ticket, MFAResponse.TwoFactorMFA(code)))
        }.body<SessionResponse>()

        if (response is SessionResponse.Success) {
            startSession(response)
        }

        return response
    }

    suspend fun startSession(response: SessionResponse.Success) {
        Log.d("Client", "Got response from API: $response")
        currentSession = response
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("Socket", "Starting websocket!")
            client.wss("$SOCKET_ROOT_URL&token=${response.userToken}") {
                websocket = this@wss

                try {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val event: BaseEvent = receiveDeserialized()
                            Log.d("Socket", "Got Event: $event")
                            EventBus.publish(event)
                        }
                    }
                } catch (exception: Exception) {
                    Log.e("Socket", "$exception")
                }

                currentIntervalJob = intervalPing(this@wss)
            }
        }
    }

    private suspend fun removeExistingSession(sessionResponse: SessionResponse.Success) {
        client.delete("${API_ROOT_URL}auth/session/${sessionResponse.id}") {
            headers { append("X-Session-Token", currentSession?.userToken ?: "") }
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun dropSession(): Boolean {
        return try {
            removeExistingSession(currentSession!!)
            currentSession = null

            websocket?.close()

            true
        } catch (error: Exception) {
            Log.e("Client", "Error whilst dropping session: $error")

            false
        }
    }
}
