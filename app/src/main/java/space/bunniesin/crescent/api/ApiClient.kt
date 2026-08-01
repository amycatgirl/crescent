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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import space.bunniesin.crescent.models.api.User
import kotlin.time.Duration.Companion.milliseconds

data class InstanceConfig(
    val api: String = "https://api.stoat.chat/0.8",
    val gateway: String = "wss://events.stoat.chat",
    val cdn: String = "https://cdn.stoatusercontent.com"
)

class ApiClient constructor(
    val config: InstanceConfig = InstanceConfig()
) {

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

    private suspend fun intervalPing(ws: DefaultWebSocketSession): Job? {
        var job: Job? = null;
        coroutineScope {
            job = launch {
                while (true) {
                    delay((20 * 1000).milliseconds)
                    Log.d("Socket", "Pinging!")
                    ws.send(this@ApiClient.jsonDeserializer.encodeToString(PingEvent(1)))
                }
            }
        }

        return job
    }

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(jsonDeserializer)
        }
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(jsonDeserializer)
        }
    }

    suspend fun getDirectMessages(): List<Channel> {
        val res = client.get("${config.api}/users/dms") {
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
            val res = client.get("${config.api}/channel/${channel.id}/messages/${messageId}") {
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
        val url = "${config.api}channels/${channel.id}/messages?limit=30"
        val res = client.get(url) {
            headers {
                append("X-Session-Token", currentSession?.userToken ?: "")
            }

            accept(ContentType.Application.Json)
        }.body<List<PartialMessage>>()


        Log.d("Cache", "Cache size: ${cache.size}")

        return res
    }

    suspend fun sendMessage(location: Channel, message: String) {
        val url = "${config.api}channels/${location.id}/messages"
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
        val response = client.post("${config.api}/auth/session/login") {
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
        val response = client.post("${config.api}/auth/session/login") {
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
            // TODO: construct url from func
            client.wss("${config.gateway}?version=1&format=json&token=${response.userToken}") {
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

                this@ApiClient.currentIntervalJob = intervalPing(this@wss)
            }
        }
    }

    private suspend fun removeExistingSession(sessionResponse: SessionResponse.Success) {
        client.delete("${config.api}auth/session/${sessionResponse.id}") {
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

    suspend fun fetchUser(id: String): User {
        val response = client.get("${config.api}/users/$id") {
            accept(ContentType.Application.Json)
            headers { append("X-Session-Token", currentSession?.userToken ?: "") }
            // TODO: refactor into extension function
        }.body<User>()

        cache[id] = response
        return response
    }
}
