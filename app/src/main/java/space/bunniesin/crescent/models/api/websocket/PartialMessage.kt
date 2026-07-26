package space.bunniesin.crescent.models.api.websocket

import space.bunniesin.crescent.models.api.Attachment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Message")
data class PartialMessage(
    @SerialName("_id")
    val id: String? = null,
    @SerialName("channel")
    val channelId: String? = null,
    @SerialName("author")
    val authorId: String? = null,
    val content: String? = null,
    val attachments: List<Attachment>? = null,
    val edited: String? = null,
    val replies: List<String>? = null,
    val system: SystemMessage? = null,
) : BaseEvent()
