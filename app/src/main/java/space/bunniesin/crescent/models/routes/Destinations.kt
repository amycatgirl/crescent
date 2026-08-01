package space.bunniesin.crescent.models.routes

import androidx.annotation.Keep
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import space.bunniesin.crescent.models.api.User
import space.bunniesin.crescent.models.api.channels.Channel

@Serializable data object ConversationList : NavKey

@Serializable
sealed class StartConversation {
    @Serializable data object INDIVIDUAL : StartConversation(), NavKey
    @Serializable data object GROUP : StartConversation(), NavKey
}

@Serializable
sealed class SettingsPage {
    @Serializable data object ROOT : SettingsPage(), NavKey
    @Serializable data object PROFILE : SettingsPage(), NavKey
    @Serializable data object ACCOUNT : SettingsPage(), NavKey
    @Serializable data object CUSTOMIZATION : SettingsPage(), NavKey
    @Serializable data object ABOUT : SettingsPage(), NavKey
}

@Serializable data object Login : NavKey
@Serializable data class LoginMFA (
    val ticket: String
) : NavKey
@Serializable data class DirectMessage (
    val user: User?,
    val channel: Channel
) : NavKey
@Serializable data class Group(
    val group: Channel.Group
) : NavKey
@Serializable data object Debug : NavKey
