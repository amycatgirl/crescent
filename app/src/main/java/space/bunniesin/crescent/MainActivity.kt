package space.bunniesin.crescent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.android.AndroidEntryPoint
import space.bunniesin.crescent.models.routes.ScreenKey
import space.bunniesin.crescent.models.viewmodels.MainViewmodel
import space.bunniesin.crescent.nav.AppNavigation
import space.bunniesin.crescent.nav.AppNavigator
import space.bunniesin.crescent.ui.theme.RevoltTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appNavigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RevoltTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                ) {
                    AppNavigation(
                        appNavigator,
                        Modifier.padding(it)
                    )
                }
            }
        }
    }
}