package space.bunniesin.crescent.models.api.authentication

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("result")
sealed class SessionResponse {
    @Serializable
    @SerialName("Success")
    class Success(
        @SerialName("_id") val id: String,
        @SerialName("user_id") val userId: String,
        @SerialName("token") val userToken: String,
        @SerialName("name") val sessionName: String
    ) : SessionResponse()

    @Serializable
    @SerialName("MFA")
    class NeedsMultiFactorAuth(
        val ticket: String,
        @SerialName("allowed_methods") val methods: List<String>
    ) : SessionResponse()

    @Serializable
    @SerialName("Disabled")
    class AccountDisabled(
        @SerialName("user_id") val disabledUserId: String
    ) : SessionResponse()
}