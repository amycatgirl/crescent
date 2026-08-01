package space.bunniesin.crescent.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhonelinkLock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import space.bunniesin.crescent.R
import space.bunniesin.crescent.models.viewmodels.MFADialogViewModel
import space.bunniesin.crescent.models.viewmodels.SelectedMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MFADialog(
    viewmodel: MFADialogViewModel,
    dismissCallback: () -> Unit,
    successCallback: () -> Unit
) {
    var mfaValue by rememberSaveable { mutableStateOf("") }
    var has2FAFinished by remember { mutableStateOf(false) }

    if (has2FAFinished) successCallback()

    Dialog(
        onDismissRequest = dismissCallback,
    )
    {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    Icons.Default.PhonelinkLock,
                    "",
                    modifier = Modifier
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    stringResource(R.string.mfa_required_authenticator_title),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    stringResource(R.string.mfa_required_authenticator_description),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = mfaValue,
                    onValueChange = { mfaValue = it },
                    label = { Text(stringResource(R.string.mfa_required_text_field_placeholder)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 24.dp)
                    .wrapContentSize(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewmodel.handleMFAMethod(SelectedMethod.TWO_FACTOR_AUTHENTICATION, mfaValue)
                    }.invokeOnCompletion { cause -> if (cause == null) has2FAFinished = true }
                }) {
                    Text(stringResource(R.string.ui_button_login))
                }
                TextButton(onClick = dismissCallback) {
                    Text(stringResource(R.string.ui_button_cancel))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// TODO: find a way to preview components without passing dummy data
//@Preview
//@Composable
//fun MFADialogPreview() {
//    RevoltTheme {
//        MFADialog {}
//    }
//}