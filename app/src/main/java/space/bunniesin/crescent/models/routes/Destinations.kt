package space.bunniesin.crescent.models.routes

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import space.bunniesin.crescent.models.api.User
import space.bunniesin.crescent.models.api.channels.Channel

@Serializable data object ConversationList

@Serializable
sealed class StartConversation {
    @Serializable data object INDIVIDUAL : StartConversation()
    @Serializable data object GROUP : StartConversation()
}

@Serializable
sealed class SettingsPage {
    @Serializable data object ROOT : SettingsPage()
    @Serializable data object PROFILE : SettingsPage()
    @Serializable data object ACCOUNT : SettingsPage()
    @Serializable data object CUSTOMIZATION : SettingsPage()
    @Serializable data object ABOUT : SettingsPage()
}

@Serializable data object Login
@Serializable data class LoginMFA (
    val ticket: String
)
@Serializable data class DirectMessage (
    val user: User?,
    val channel: Channel
)
@Serializable data class Group(
    val group: Channel.Group
)
@Serializable data object Debug
