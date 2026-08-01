package space.bunniesin.crescent.nav

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import space.bunniesin.crescent.models.routes.ScreenKey
import javax.inject.Inject

interface AppNavigator {
    val navigationActions: Flow<NavigationAction>
    fun navigate(key: ScreenKey)
    fun navigateUp()
}

sealed interface NavigationAction {
    data class To(val key: ScreenKey) : NavigationAction
    object Up : NavigationAction
}

class AppNavigatorImpl @Inject constructor() : AppNavigator {
    private val _actions = Channel<NavigationAction>(Channel.BUFFERED)
    override val navigationActions: Flow<NavigationAction> = _actions.receiveAsFlow()

    override fun navigate(key: ScreenKey) {
        _actions.trySend(NavigationAction.To(key))
    }

    override fun navigateUp() {
        _actions.trySend(NavigationAction.Up)
    }
}