package space.bunniesin.crescent.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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

// TODO: Add resource strings for translation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsPage(goBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.settings_account))
                },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.ui_go_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .clickable { /* TODO: Show the dialog here :) */ }
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Change Username")
                            Text(
                                "Current username: dummy#1234",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Icon(painterResource(R.drawable.material_symbols_edit), "")
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .clickable { /* TODO: Show the dialog here :) */ }
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Change Email")
                                Text(
                                    "Your current email: dummy@example.com",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Icon(painterResource(R.drawable.material_symbols_edit), "")
                        }
                    }
                    HorizontalDivider(
                        Modifier.padding(horizontal = 6.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Change Password")
                                Text(
                                    "Your password: *********",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Icon(painterResource(R.drawable.material_symbols_edit), "")
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .clickable { /* TODO: Show the dialog here :) */ }
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                painterResource(R.drawable.material_symbols_phonelink_lock_outlined),
                                ""
                            )
                            Column {
                                Text("Two-Step Authentication")
                                Text(
                                    "Enable, disable and view backup codes",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Icon(
                            painterResource(R.drawable.material_symbols_chevron_right_outlined),
                            ""
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .clickable { /* TODO: Show the dialog here :) */ }
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                painterResource(R.drawable.material_symbols_devices_outlined),
                                ""
                            )
                            Column {
                                Text("Session")
                                Text(
                                    "Manage, revoke and revoke all sessions",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        Icon(
                            painterResource(R.drawable.material_symbols_chevron_right_outlined),
                            ""
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(MaterialTheme.shapes.large)
                            .clickable { /* TODO */ }
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                painterResource(R.drawable.material_symbols_person_off_outlined),
                                "",
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                "Disable Account",
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(MaterialTheme.shapes.large)
                            .clickable { /* TODO */ }
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(painterResource(R.drawable.material_symbols_delete_outlined),
                                "",
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                "Delete Account",
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AccountSettingsPagePreview() {
    RevoltTheme {
        AccountSettingsPage() {}
    }
}