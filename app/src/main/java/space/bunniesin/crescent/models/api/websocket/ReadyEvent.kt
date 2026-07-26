package space.bunniesin.crescent.models.api.websocket

import space.bunniesin.crescent.models.api.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Ready")
data class ReadyEvent(val users: List<User>) : BaseEvent()
