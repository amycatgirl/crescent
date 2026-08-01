package space.bunniesin.crescent.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.bunniesin.crescent.models.viewmodels.LoginViewmodel
import space.bunniesin.crescent.ui.composables.AccountDisabledDialog
import space.bunniesin.crescent.R

@Composable
fun LoginPage(
    viewmodel: LoginViewmodel
) {
    val state by viewmodel.state.collectAsState()
    // TODO: Please implement when the login gets an error or it's requires MFA code. And plus, implement login functionality too.
    // TODO: fuck off :trl:

    AnimatedVisibility(visible = false) {
        AccountDisabledDialog {}
    }

    val passwordIcon = if (state.isPasswordShown) painterResource(R.drawable.material_symbols_eye_off)
    else painterResource(R.drawable.material_symbols_eye)
    val passwordTransformation = if (state.isPasswordShown) VisualTransformation.None
    else PasswordVisualTransformation()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = stringResource(R.string.app_name),
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = stringResource(R.string.app_name)
                    )
                }
                Text(
                    stringResource(R.string.app_name),
                    fontSize = 50.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
            ) {
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { viewmodel.updateEmail(it) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    label = { Text(stringResource(R.string.ui_input_email)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    maxLines = 1
                )
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { viewmodel.updateEmail(it) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    label = {
                        Text(stringResource(R.string.ui_input_password))
                    },
                    visualTransformation = passwordTransformation,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    maxLines = 1,
                    trailingIcon = { IconButton(onClick = {
                        viewmodel.toggleShowPassword()
                    }) {
                        Icon(painter = passwordIcon, contentDescription = "")
                    } }
                )
                Button(modifier = Modifier.fillMaxWidth(.625f), onClick = {
                    viewmodel.login()
                }) {
                    Text(stringResource(R.string.ui_button_login))
                }
            }
            TextButton(onClick = { /*TODO*/ }) {
                Text(stringResource(R.string.ui_button_recover_password))
            }
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text(stringResource(R.string.ui_button_create_account))
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
