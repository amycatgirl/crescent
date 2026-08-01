package space.bunniesin.crescent

import android.util.Log
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import space.bunniesin.crescent.api.ApiClient
import space.bunniesin.crescent.models.routes.ConversationList
import space.bunniesin.crescent.models.routes.Debug
import space.bunniesin.crescent.models.routes.DirectMessage
import space.bunniesin.crescent.models.routes.Login
import space.bunniesin.crescent.models.routes.LoginMFA
import space.bunniesin.crescent.models.routes.SettingsPage
import space.bunniesin.crescent.models.routes.StartConversation
import space.bunniesin.crescent.models.viewmodels.ChatViewmodel
import space.bunniesin.crescent.models.viewmodels.HomeViewmodel
import space.bunniesin.crescent.models.viewmodels.LoginViewmodel
import space.bunniesin.crescent.models.viewmodels.MainViewmodel
import space.bunniesin.crescent.ui.composables.MFADialog
import space.bunniesin.crescent.ui.navigation.ChatPage
import space.bunniesin.crescent.ui.navigation.DebugScreen
import space.bunniesin.crescent.ui.navigation.HomePage
import space.bunniesin.crescent.ui.navigation.LoginPage
import space.bunniesin.crescent.ui.navigation.ProfileSettingsPage
import space.bunniesin.crescent.ui.navigation.SettingsPage as SettingsScreen
import space.bunniesin.crescent.ui.navigation.StartConversationPage

@Composable
fun App(
    mainViewmodel: MainViewmodel = viewModel()
) {
    val context = LocalContext.current
    val navigationState = rememberNavigationState(
        startRoute = Login,
        topLevelRoutes = setOf(Login, ConversationList)
    )
    val navigator = remember { Navigator(navigationState) }

    val forwardTransition: () -> ContentTransform = {
        (fadeIn(animationSpec = tween(durationMillis = 250)) + slideInHorizontally(
            animationSpec = tween(
                durationMillis = 250,
                easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
            ),
            initialOffsetX = { it }
        )) togetherWith (fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutHorizontally(
            animationSpec = tween(
                durationMillis = 200,
                easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
            ),
            targetOffsetX = { -it }
        ))
    }

    val backwardTransition: () -> ContentTransform = {
        (fadeIn(animationSpec = tween(durationMillis = 250)) + slideInHorizontally(
            animationSpec = tween(
                durationMillis = 250,
                easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
            ),
            initialOffsetX = { -it }
        )) togetherWith (fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutHorizontally(
            animationSpec = tween(
                durationMillis = 200,
                easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
            ),
            targetOffsetX = { it }
        ))
    }

    val entryProvider = entryProvider {
        entry<Debug> {
            DebugScreen(mainViewmodel.messageList, goBack = {
                navigator.goBack()
            }, navigateToDebugLogin = { navigator.navigate(Login) })
        }

        entry<Login> {
            val viewmodel = viewModel {
                LoginViewmodel(ApiClient, navigator, context)
            }
            LoginPage(viewmodel)
        }

        entry<LoginMFA>(metadata = DialogSceneStrategy.dialog()) { key ->
            MFADialog(key, dismissCallback = { navigator.goBack() }, successCallback = {
                navigator.navigate(ConversationList)
            })
        }

        entry<ConversationList> {
            val homeViewmodel = viewModel {
                HomeViewmodel()
            }

            HomePage(
                homeViewmodel,
                navigateToChat = { channel, user ->
                    navigator.navigate(
                        DirectMessage(
                            user, channel
                        )
                    )
                },
                navigateToDebug = { navigator.navigate(Debug) },
                navigateToSettings = { navigator.navigate(SettingsPage.ROOT) },
                navigateToStartConversation = { navigator.navigate(StartConversation.INDIVIDUAL) }
            )
        }

        entry<StartConversation.INDIVIDUAL> {
            StartConversationPage(goBack = { navigator.goBack() })
        }

        entry<StartConversation.GROUP> {
            // TODO: Implement group conversation
        }

        entry<DirectMessage> { dm ->
            Log.d(
                "Navigator",
                "navigating to chat, id: ${dm.channel}"
            )
            val viewmodel: ChatViewmodel = viewModel {
                ChatViewmodel(dm.user, dm.channel)
            }
            ChatPage(
                viewmodel,
                goBack = {
                    navigator.goBack()
                }
            )
        }

        entry<SettingsPage.ROOT> {
            SettingsScreen(
                goBack = { navigator.goBack() },
                navigateToAccount = {},
                navigateToProfile = { navigator.navigate(SettingsPage.PROFILE) },
                onSessionDropped = {
                    navigator.navigate(Login)
                }
            )
        }

        entry<SettingsPage.PROFILE> {
            ProfileSettingsPage(goBack = { navigator.goBack() })
        }
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            sceneStrategy = remember { DialogSceneStrategy() },
            transitionSpec = { forwardTransition() },
            popTransitionSpec = { backwardTransition() },
            onBack = { navigator.goBack() }
        )
    }
}
