package space.bunniesin.crescent.ui.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import space.bunniesin.crescent.models.api.websocket.SystemMessage
import space.bunniesin.crescent.ui.theme.RevoltTheme

@Composable
fun SystemMessageDisplay(systemMessage: SystemMessage) {
    val sysMsgTextStyle = TextStyle(
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.outline,
        fontWeight = FontWeight.W300,
        textAlign = TextAlign.Center
    )

    Log.d("smd", "type $systemMessage")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        when (systemMessage) {
            is SystemMessage.Text -> Text(
                systemMessage.content, style = sysMsgTextStyle
            )

            is SystemMessage.UserAdded -> Text(
                "${systemMessage.id} was added by ${systemMessage.by}", // TODO: Resolve user in cache and by fetching user
                style = sysMsgTextStyle
            )

            is SystemMessage.UserRemove -> Text(
                "${systemMessage.id} was removed by ${systemMessage.by}",
                style = sysMsgTextStyle
            )

            is SystemMessage.UserKicked -> Text(
                "${systemMessage.name} was kicked by ${systemMessage.by}",
                style = sysMsgTextStyle
            )

            is SystemMessage.ChannelTransferred -> Text(
                "Ownership of channel has been transferred to ${systemMessage.to}",
                style = sysMsgTextStyle
            )

            is SystemMessage.ChannelDescriptionChanged -> Text(
                "${systemMessage.by} changed this group's description",
                style = sysMsgTextStyle
            )

            is SystemMessage.ChannelIconChanged -> Text(
                "${systemMessage.by} changed this group's icon",
                style = sysMsgTextStyle
            )

            else -> Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row (verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center)
                {
                    Text("Caught Unimplemented event, please report issue on GitHub.", fontSize = 12.sp)
                }
            }
        }
    }
}

@Preview
@Composable
fun SystemMessagePreview() {
    RevoltTheme {
        Scaffold { innerPadding ->
            Column(Modifier.padding(innerPadding)) {
                SystemMessageDisplay(SystemMessage.Text(content = "I am going to make an announcement"))
                SystemMessageDisplay(
                    SystemMessage.UserAdded(
                        id = "0".repeat(26),
                        by = "0".repeat(26)
                    )
                )
                SystemMessageDisplay(
                    SystemMessage.UserRemove(
                        id = "0".repeat(26),
                        by = "0".repeat(26)
                    )
                )
                SystemMessageDisplay(SystemMessage.UserKicked(name = "John", by = "0".repeat(26)))
                SystemMessageDisplay(SystemMessage.ChannelDescriptionChanged(by = "0".repeat(26)))
                SystemMessageDisplay(SystemMessage.ChannelIconChanged(by = "0".repeat(26)))
                SystemMessageDisplay(
                    SystemMessage.ChannelTransferred(
                        from = "0".repeat(26),
                        to = "0".repeat(26)
                    )
                )
                SystemMessageDisplay(
                    SystemMessage.UnimplementedMessage
                )
            }
        }
    }
}