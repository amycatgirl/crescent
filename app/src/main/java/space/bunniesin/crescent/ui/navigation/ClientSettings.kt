package space.bunniesin.crescent.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import space.bunniesin.crescent.R
import space.bunniesin.crescent.ui.theme.RevoltTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientSettingsPage(goBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_client)) },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(
                                R.string.ui_go_back
                            )
                        )
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Column(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().wrapContentSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                painterResource(R.drawable.material_symbols_mood_outlined),
                                ""
                            )
                            Column(modifier = Modifier.fillMaxWidth(.833f).wrapContentSize()) {
                                Text("Emoji Picker")
                                Text(
                                    "Enabled by default, disabling will disable the emoji picker.",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }

                        Switch(
                            checked = true,
                            onCheckedChange = {/* TODO */}
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().wrapContentSize(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                painterResource(R.drawable.material_symbols_notifications_outlined),
                                ""
                            )
                            Column(modifier = Modifier.fillMaxWidth(.833f).wrapContentSize()) {
                                Text("Push Notification")
                                Text(
                                    "Receive push notifications while the app is closed.",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }

                        Switch(
                            checked = false,
                            onCheckedChange = {/* TODO */}
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().wrapContentSize(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                painterResource(R.drawable.material_symbols_network_cell_outlined),
                                ""
                            )
                            Column(modifier = Modifier.fillMaxWidth(.833f).wrapContentSize()) {
                                Text("Low Data Mode")
                                Text(
                                    "Enabling this will reduce image quality while saving data usage",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }

                        Switch(
                            checked = false,
                            onCheckedChange = {/* TODO */}
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ClientSettingsPagePreview() {
    RevoltTheme {
        ClientSettingsPage {}
    }
}