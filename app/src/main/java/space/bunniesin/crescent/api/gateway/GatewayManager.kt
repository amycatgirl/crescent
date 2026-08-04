package space.bunniesin.crescent.api.gateway

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.wss
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import space.bunniesin.crescent.api.InstanceConfig
import space.bunniesin.crescent.models.api.websocket.BaseEvent
import space.bunniesin.crescent.models.api.websocket.PingEvent

class GatewayManager(
    private val instance: InstanceConfig,
    private val deserializer: Json
) {
    private val gatewayScope = CoroutineScope(
        Dispatchers.IO + SupervisorJob() + CoroutineName("GatewayScope") + CoroutineExceptionHandler { _, e ->
            Log.e("GatewayManager", "Error in GatewayScope: $e")
            // TODO: Trigger reconnecting with exponential backoff
        }
    )

    private var ws: DefaultWebSocketSession? = null
    private var wsCoroutine: Job? = null
    private val _flow = MutableSharedFlow<BaseEvent>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val events = _flow.asSharedFlow()

    private val client = HttpClient(OkHttp) {
        install(WebSockets)
    }

    fun connect(token: String) {
        wsCoroutine =  gatewayScope.launch {
            client.wss("${instance.gateway}?version=1&format=json&token=${token}") {
                ws = this
                Log.d("Gateway", "Connected to ${instance.gateway}")

                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        val event: BaseEvent = deserializer.decodeFromString(frame.readText())
                        _flow.emit(event)
                        Log.d("Gateway", "$event")
                    }
                }
            }
        }
    }

    fun disconnect() {
        wsCoroutine?.cancel()
        ws = null
    }

    suspend fun ping() {
        val pingPacket = PingEvent(System.currentTimeMillis().toInt())
        ws?.send(deserializer.encodeToString(pingPacket))
        Log.d("Gateway", "Sent ping frame with data: ${pingPacket.data}")
    }

    suspend fun send(event: BaseEvent) {
        ws?.send(deserializer.encodeToString(event))
        Log.d("Gateway", "Sent frame with data: $event")
    }
}

inline fun <reified T: BaseEvent> Flow<BaseEvent>.ofType(): Flow<T> = filterIsInstance<T>()
