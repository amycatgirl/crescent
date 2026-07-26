package space.bunniesin.crescent.models.api.authentication

import android.os.Build.VERSION.RELEASE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class MFAResponse {
    @Serializable
    data class PasswordMFA(val password: String): MFAResponse()
    @Serializable
    data class TwoFactorMFA(@SerialName("totp_code") val code: String): MFAResponse()
    @Serializable
    data class RecoveryMFA(@SerialName("recovery_code") val code: String): MFAResponse()
}

@Serializable
data class MFASessionRequest(
    @SerialName("mfa_ticket") val ticket: String,
    @SerialName("mfa_response") val response: MFAResponse,
    @SerialName("friendly_name") val friendlyName: String = "Crescent on Android $RELEASE"
)

@Serializable
data class EmailSessionRequest(
    val email: String,
    val password: String,
    @SerialName("friendly_name") val friendlyName: String = "Crescent on Android $RELEASE"
)
