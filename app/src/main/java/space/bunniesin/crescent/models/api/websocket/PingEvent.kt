package space.bunniesin.crescent.models.api.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Ping")
data class PingEvent(val data: Int) : BaseEvent()

@Serializable
@SerialName("Pong")
data class PongEvent(val data: Int) : BaseEvent()