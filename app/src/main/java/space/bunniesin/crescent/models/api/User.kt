package space.bunniesin.crescent.models.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("_id") val id: String,
    val username: String,
    @SerialName("display_name") val displayName: String? = null,
    val discriminator: String,
    val avatar: Attachment? = null,
    val badges: Int? = null,
    val flags: Int? = null,
    val status: UserStatus? = null,
    val relations: List<Relation>? = null,
    val relationship: RelationshipStatus? = null,
    val online: Boolean? = null
)

@Serializable
data class UserStatus(
    val text: String? = null,
    val presence: Presence? = null
)

@Serializable
enum class Flags(flag: Int) {
    NONE(0),
    SUSPENDED(1),
    DELETED(2),
    BANNED(4),
    SPAM(8)
}

@Serializable
enum class Presence(value: String) {
    @SerialName("Online") ONLINE("Online"),
    @SerialName("Focus") FOCUS("Focus"),
    @SerialName("Busy") DO_NOT_DISTURB("Busy"),
    @SerialName("Idle") IDLE("Idle"),
    @SerialName("Invisible") OFFLINE("Invisible")
}