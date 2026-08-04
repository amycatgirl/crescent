package space.bunniesin.crescent.models.api.websocket

import space.bunniesin.crescent.models.api.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import space.bunniesin.crescent.models.api.channels.Channel

@Serializable
@SerialName("Ready")
data class ReadyEvent(val users: List<User>, val channels: List<Channel>) : BaseEvent()
