package space.bunniesin.crescent.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import space.bunniesin.crescent.models.routes.ScreenKey
import space.bunniesin.crescent.models.viewmodels.ChatViewmodel
import space.bunniesin.crescent.models.viewmodels.HomeViewmodel
import space.bunniesin.crescent.models.viewmodels.LoginViewmodel
import space.bunniesin.crescent.models.viewmodels.MFADialogViewModel
import space.bunniesin.crescent.models.viewmodels.SettingsViewmodel
import space.bunniesin.crescent.ui.composables.MFADialog
import space.bunniesin.crescent.ui.pages.AccountSettingsPage
import space.bunniesin.crescent.ui.pages.ChatPage
import space.bunniesin.crescent.ui.pages.HomePage
import space.bunniesin.crescent.ui.pages.LoginPage
import space.bunniesin.crescent.ui.pages.ProfileSettingsPage
import space.bunniesin.crescent.ui.pages.SettingsPage
import space.bunniesin.crescent.ui.pages.StartConversationPage

@Composable
fun AppNavigation(
    navigator: AppNavigator,
    modifier: Modifier = Modifier
) {
    val backstack = remember { mutableStateListOf<ScreenKey>(ScreenKey.Login) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        navigator.navigationActions.collect { action ->
            when (action) {
                is NavigationAction.To -> backstack.add(action.key)
                is NavigationAction.Up -> if (backstack.size > 1) backstack.removeAt(backstack.lastIndex)
            }
        }
    }

    val navigationEntries = entryProvider {
        entry<ScreenKey.Login> {
            val viewmodel = hiltViewModel(
                creationCallback = { factory: LoginViewmodel.Factory ->
                    factory.create(context)
                }
            )

            LoginPage(viewmodel)
        }

        entry<ScreenKey.LoginMFA>(metadata = DialogSceneStrategy.dialog()) { key ->
            val viewmodel = hiltViewModel(
                creationCallback = { factory: MFADialogViewModel.Factory ->
                    factory.create(key.ticket, context)
                }
            )
            MFADialog(
                viewmodel,
                successCallback = {
                    navigator.navigate(ScreenKey.ConversationList)
                },
                dismissCallback = {
                    navigator.navigateUp()
                }
            )
        }

        entry<ScreenKey.ConversationList> {
            val viewmodel = hiltViewModel<HomeViewmodel>()
            HomePage(
                viewmodel, navigateToChat = { channel, user ->
                    navigator.navigate(ScreenKey.DirectMessage(user, channel))
                },
                navigateToSettings = {
                    navigator.navigate(ScreenKey.SettingsPage.ROOT)
                },
                navigateToDebug = {
                    navigator.navigate(ScreenKey.Debug)
                },
                navigateToStartConversation = {
                    when (it) {
                        "dm" -> navigator.navigate(ScreenKey.StartConversation.INDIVIDUAL)
                        "group" -> navigator.navigate(ScreenKey.StartConversation.GROUP)
                        else -> TODO()
                    }
                }
            )
        }

        entry<ScreenKey.DirectMessage> { key ->
            val viewmodel: ChatViewmodel = hiltViewModel(
                creationCallback = { factory: ChatViewmodel.Factory ->
                    factory.create(key.user, key.channel)
                }
            )
            ChatPage(viewmodel, {}, { navigator.navigateUp() })
        }

        entry<ScreenKey.StartConversation.INDIVIDUAL> {
            StartConversationPage(goBack = { navigator.navigateUp() })
        }

        entry<ScreenKey.StartConversation.GROUP> {
            StartConversationPage(goBack = { navigator.navigateUp() })
        }

        entry<ScreenKey.SettingsPage.ROOT> {
            val viewModel = hiltViewModel<SettingsViewmodel>()
            SettingsPage(
                viewModel,
                goBack = { navigator.navigateUp() },
                navigateToAccount = { navigator.navigate(ScreenKey.SettingsPage.ACCOUNT) },
                navigateToProfile = { navigator.navigate(ScreenKey.SettingsPage.PROFILE) },
                onSessionDropped = { navigator.navigate(ScreenKey.Login) }
            )
        }

        // TODO: This should be nested inside of settings, not part of the main navigation graph!
        entry<ScreenKey.SettingsPage.ACCOUNT> {
            AccountSettingsPage(goBack = { navigator.navigateUp() })
        }

        entry<ScreenKey.SettingsPage.PROFILE> {
            ProfileSettingsPage(goBack = { navigator.navigateUp() })
        }
    }

    NavDisplay(
        backStack = backstack,
        entryDecorators = listOf(
            rememberViewModelStoreNavEntryDecorator(),
            rememberSaveableStateHolderNavEntryDecorator()
        ),
        modifier = modifier,
        sceneStrategy = remember { DialogSceneStrategy() },
        entryProvider = navigationEntries,
        transitionSpec = { forwardTransition() },
        popTransitionSpec = { backwardTransition() },
        onBack = { navigator.navigateUp() }
    )
}