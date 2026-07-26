package space.bunniesin.crescent.models.api

import space.bunniesin.crescent.models.api.attachments.FileMetadata
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    @SerialName("_id")
    val id: String,
    val tag: String,
    val filename: String,
    @SerialName("content_type")
    val mimeType: String,
    val size: Int,
    val metadata: FileMetadata,
    val deleted: Boolean? = null,
    val reported: Boolean? = null,
)