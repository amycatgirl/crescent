package space.bunniesin.crescent.ui.navigation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import space.bunniesin.crescent.BuildConfig
import space.bunniesin.crescent.R
import space.bunniesin.crescent.api.ApiClient
import space.bunniesin.crescent.ui.composables.LogoutConfirmationDialog
import space.bunniesin.crescent.ui.theme.RevoltTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    goBack: () -> Unit,
    navigateToAccount: () -> Unit,
    navigateToProfile: () -> Unit,
    onSessionDropped: () -> Unit
) {
    var shouldShowDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    // TODO: Implement App configuration
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    AnimatedVisibility(visible = shouldShowDialog) {
        LogoutConfirmationDialog(
            logoutCallback = {
                scope.launch {
                    val result = ApiClient.dropSession()
                    when (result) {
                        true -> onSessionDropped()
                        false -> Log.e("App", "Logout failed, do something here")
                    }
                }
            },
            dismissCallback = { shouldShowDialog = false })
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = { Text(stringResource(R.string.settings_header)) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            stringResource(R.string.ui_go_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // TODO: Needed navigation for each categories
            Column(
                Modifier
                    .padding(12.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                ListItem(
                    modifier = Modifier.clickable { navigateToAccount() },
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    leadingContent = {
                        Icon(
                            painterResource(R.drawable.material_symbols_person),
                            stringResource(R.string.settings_account)
                        )
                    },
                    headlineContent = { Text(stringResource(R.string.settings_account)) },
                    supportingContent = {
                        Text(stringResource(R.string.settings_account_description))
                    }
                )
                HorizontalDivider(
                    Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                ListItem(
                    modifier = Modifier.clickable { navigateToProfile() },
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    leadingContent = {
                        Icon(
                            painterResource(R.drawable.material_symbols_badge),
                            stringResource(R.string.settings_profile)
                        )
                    },
                    headlineContent = { Text(stringResource(R.string.settings_profile)) },
                    supportingContent = {
                        Text(stringResource(R.string.settings_profile_description))
                    }
                )
            }

            Column(
                Modifier
                    .padding(12.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                ListItem(
                    modifier = Modifier.clickable { /* TODO */ },
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    leadingContent = {
                        Icon(
                            painterResource(R.drawable.material_symbols_settings),
                            stringResource(R.string.settings_client)
                        )
                    },
                    headlineContent = { Text(stringResource(R.string.settings_client)) },
                    supportingContent = {
                        Text(stringResource(R.string.settings_client_description))
                    }
                )
                HorizontalDivider(
                    Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                ListItem(
                    modifier = Modifier.clickable { /* TODO */ },
                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    leadingContent = {
                        Icon(
                            painterResource(R.drawable.material_symbols_info),
                            "${stringResource(R.string.settings_about)} ${stringResource(R.string.app_name)}"
                        )
                    },
                    headlineContent = {
                        Text("${stringResource(R.string.settings_about)} ${stringResource(R.string.app_name)}")
                    },
                    supportingContent = {
                        Text(stringResource(R.string.settings_about_description))
                    }
                )
            }
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    onClick = { shouldShowDialog = true }) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = stringResource(R.string.settings_logout),
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            stringResource(R.string.settings_logout),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.8f))
            Text(
                "${stringResource(R.string.app_name)} - Build ${BuildConfig.VERSION_CODE}",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
fun SettingsPagePreview() {
    RevoltTheme {
        SettingsPage({}, {}, {}, {})
    }
}