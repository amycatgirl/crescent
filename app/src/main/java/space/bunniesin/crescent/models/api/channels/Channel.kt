package space.bunniesin.crescent.models.api.channels

import space.bunniesin.crescent.models.api.Attachment
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("channel_type")
sealed class Channel {
    @SerialName("_id")
    abstract val id: String

    @Serializable
    @SerialName("SavedMessages")
    class SavedMessages(
        @SerialName("_id")
        override val id: String,
        @SerialName("user")
        val userId: String
    ) : Channel()

    @Serializable
    @SerialName("DirectMessage")
    class DirectMessage(
        @SerialName("_id")
        override val id: String,
        val active: Boolean,
        val recipients: List<String>,
        @SerialName("last_message_id")
        val lastSentMessage: String? = null
    ) : Channel()

    @Serializable
    @SerialName("Group")
    class Group(
        @SerialName("_id")
        override val id: String,
        val name: String,
        @SerialName("owner")
        val ownerId: String,
        val description: String? = null,
        val recipients: List<String>,
        val icon: Attachment? = null,
        @SerialName("last_message_id")
        val lastSentMessage: String? = null,
        @SerialName("permissions")
        val memberPermissions: Long? = null,
        @SerialName("nsfw")
        val notSafeForWork: Boolean? = null
    ) : Channel()
}