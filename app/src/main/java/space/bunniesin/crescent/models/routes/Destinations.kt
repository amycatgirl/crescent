package space.bunniesin.crescent.models.routes

import androidx.annotation.Keep
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import space.bunniesin.crescent.models.api.User
import space.bunniesin.crescent.models.api.channels.Channel


sealed interface ScreenKey : NavKey {
    @Serializable data object ConversationList : ScreenKey

    @Serializable
    sealed class StartConversation {
        @Serializable data object INDIVIDUAL : StartConversation(), ScreenKey
        @Serializable data object GROUP : StartConversation(), ScreenKey
    }

    @Serializable
    sealed class SettingsPage {
        @Serializable data object ROOT : SettingsPage(), ScreenKey
        @Serializable data object PROFILE : SettingsPage(), ScreenKey
        @Serializable data object ACCOUNT : SettingsPage(), ScreenKey
        @Serializable data object CUSTOMIZATION : SettingsPage(), ScreenKey
        @Serializable data object ABOUT : SettingsPage(), ScreenKey
    }

    @Serializable data object Login : ScreenKey
    @Serializable data class LoginMFA (
        val ticket: String
    ) : ScreenKey
    @Serializable data class DirectMessage (
        val user: User?,
        val channel: Channel
    ) : ScreenKey
    @Serializable data class Group(
        val group: Channel.Group
    ) : ScreenKey
    @Serializable data object Debug : ScreenKey

}