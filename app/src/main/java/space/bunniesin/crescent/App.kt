package space.bunniesin.crescent

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import space.bunniesin.crescent.api.ApiClient
import space.bunniesin.crescent.models.viewmodels.ChatViewmodel
import space.bunniesin.crescent.models.viewmodels.HomeViewmodel
import space.bunniesin.crescent.models.viewmodels.LoginViewmodel
import space.bunniesin.crescent.models.viewmodels.MainViewmodel
import space.bunniesin.crescent.ui.composables.LoginMFA
import space.bunniesin.crescent.ui.composables.MFADialog
import space.bunniesin.crescent.ui.navigation.ChatPage
import space.bunniesin.crescent.ui.navigation.DebugScreen
import space.bunniesin.crescent.ui.navigation.HomePage
import space.bunniesin.crescent.ui.navigation.LoginPage
import space.bunniesin.crescent.ui.navigation.ProfileSettingsPage
import space.bunniesin.crescent.ui.navigation.SettingsPage
import space.bunniesin.crescent.ui.navigation.StartConversationPage

@Composable
fun App(
    mainViewmodel: MainViewmodel = viewModel()
) {
    val context = LocalContext.current
    val navigator = rememberNavController()

    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navigator, startDestination = "auth") {
            composable("debug",
                enterTransition = {
                    fadeIn(animationSpec = tween(durationMillis = 250)) + slideIntoContainer(
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Left
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutOfContainer(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Right
                    )
                }
            ) {
                DebugScreen(mainViewmodel.messageList, goBack = {
                    navigator.popBackStack()
                }, navigateToDebugLogin = { navigator.navigate("auth") })
            }

            composable("auth") {
                val viewmodel = viewModel {
                    LoginViewmodel(ApiClient, navigator, context)
                }
                LoginPage(viewmodel)
            }
            dialog<LoginMFA> { backStackEntry ->
                val data: LoginMFA = backStackEntry.toRoute()
                MFADialog(data, dismissCallback = { navigator.popBackStack() }, successCallback = {
                    navigator.navigate("home") {
                        popUpTo("auth") { inclusive = true }
                    }
                })
            }

            composable(
                "home",
                enterTransition = {
                    fadeIn(animationSpec = tween(durationMillis = 250)) + slideIntoContainer(
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Right
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutOfContainer(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Left
                    )
                }
            ) {
                val homeViewmodel = viewModel {
                    HomeViewmodel()
                }

                HomePage(
                    homeViewmodel,
                    navigateToChat = { navigator.navigate("messages/${it}") },
                    navigateToDebug = { navigator.navigate("debug") },
                    navigateToSettings = { navigator.navigate("settings") },
                    navigateToStartConversation = { navigator.navigate("home/startconversation") }
                )
            }

            composable(
                "home/startconversation",
                enterTransition = {
                    fadeIn(animationSpec = tween(durationMillis = 250)) + slideIntoContainer(
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Right
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutOfContainer(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Left
                    )
                }
            ) {
                StartConversationPage (goBack = { navigator.popBackStack() })
            }

            composable(
                "messages/{id}",
                enterTransition = {
                    fadeIn(animationSpec = tween(durationMillis = 250)) + slideIntoContainer(
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Left
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutOfContainer(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Right
                    )
                },
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                Log.d(
                    "Navigator",
                    "navigating to chat, id: ${backStackEntry.arguments?.getString("id")}"
                )
                val viewmodel: ChatViewmodel = viewModel {
                    ChatViewmodel(backStackEntry.arguments?.getString("id")!!)
                }
                ChatPage(
                    viewmodel,
                    backStackEntry.arguments?.getString("id")!!,
                    goBack = {
                        navigator.popBackStack()
                    }
                )
            }

            composable(
                "settings",
                enterTransition = {
                    fadeIn(animationSpec = tween(durationMillis = 250)) + slideIntoContainer(
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Left
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutOfContainer(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Right
                    )
                }
            ) {
                SettingsPage(
                    goBack = { navigator.popBackStack() },
                    navigateToAccount = {},
                    navigateToProfile = { navigator.navigate("settings/profile") },
                    onSessionDropped = {
                        navigator.navigate("auth") {
                            popUpTo("settings") { inclusive = true }
                        }
                    }
                )
            }
            composable("settings/profile", enterTransition = {
                fadeIn(animationSpec = tween(durationMillis = 250)) + slideIntoContainer(
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
                    ),
                    towards = AnimatedContentTransitionScope.SlideDirection.Left
                )
            },
                exitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutOfContainer(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
                        ),
                        towards = AnimatedContentTransitionScope.SlideDirection.Right
                    )
                }) {
                ProfileSettingsPage(goBack = { navigator.popBackStack() })
            }
        }
    }
}