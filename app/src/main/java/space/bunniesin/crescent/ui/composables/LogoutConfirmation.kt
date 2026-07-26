package space.bunniesin.crescent.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import space.bunniesin.crescent.R
import space.bunniesin.crescent.ui.theme.RevoltTheme

@Composable
fun LogoutConfirmationDialog(logoutCallback: () -> Unit, dismissCallback: () -> Unit) {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Card(
            modifier = Modifier.clip(MaterialTheme.shapes.extraLarge)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    stringResource(R.string.logout_title),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    stringResource(R.string.logout_description),
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = logoutCallback, colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(
                            stringResource(R.string.logout_accept),
                            color = MaterialTheme.colorScheme.onError
                        )
                    }
                    TextButton(onClick = dismissCallback) {
                        Text(stringResource(R.string.ui_button_cancel))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LogoutConfirmationDialogPreviewLight() {
    RevoltTheme {
        LogoutConfirmationDialog(
            logoutCallback = { },
            dismissCallback = { }
        )
    }
}

@Preview
@Composable
fun LogoutConfirmationDialogPreviewDark() {
    RevoltTheme(darkTheme = true) {
        LogoutConfirmationDialog(
            logoutCallback = { },
            dismissCallback = { }
        )
    }
}