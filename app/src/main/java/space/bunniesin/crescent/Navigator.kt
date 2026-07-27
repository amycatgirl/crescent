package space.bunniesin.crescent

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation events (forward and back) by updating the navigation state.
 */
class Navigator(val state: NavigationState){
    fun navigate(route: NavKey){
        if (route in state.backStacks.keys){
            // This is a top level route, just switch to it.
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack(){
        val currentStack = state.backStacks[state.topLevelRoute] ?:
        error("Stack for ${state.topLevelRoute} not found")

        if (currentStack.size > 1) {
            currentStack.removeLastOrNull()
        } else if (state.topLevelRoute != state.startRoute) {
            // If we're at the base of the current route, go back to the start route stack.
            state.topLevelRoute = state.startRoute
        }
    }
}
