package org.nekoweb.amycatgirl.revolt.models.api.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Authenticate")
data class AuthenticateEvent(
    val token: String
): BaseEvent()