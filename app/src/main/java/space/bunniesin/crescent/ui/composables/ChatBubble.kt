package space.bunniesin.crescent.ui.composables

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Square
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.bunniesin.crescent.api.ApiClient
import space.bunniesin.crescent.models.api.User
import space.bunniesin.crescent.models.api.websocket.PartialMessage
import space.bunniesin.crescent.ui.theme.RevoltTheme
import com.vdurmont.emoji.EmojiParser
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ChatBubble(user: User, message: PartialMessage, modifier: Modifier = Modifier, isSelf: Boolean = false) {
    val author = remember(message.authorId) {
        user
    }

    // BottomSheet states
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    fun checkIfMessageOnlyContainsEmoji(): Boolean {
        if (message.content == null) return false
        return EmojiParser.removeAllEmojis(message.content).isEmpty()
    }

    val isOnlyEmoji = remember { checkIfMessageOnlyContainsEmoji() }

    Log.d("emoji", "Is emoji beeg: $isOnlyEmoji")

    Column(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .widthIn(0.dp, 300.dp),
        horizontalAlignment = if (isSelf) Alignment.End else Alignment.Start
    ) {
        Text(
            if (isSelf) "You" else author.displayName ?: author.username,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))
        Surface(
            color = if (isOnlyEmoji)
                Color.Transparent else if (isSelf)
                MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .combinedClickable(
                    onClick = { /* NOTE: Leave them empty for now */ },
                    onLongClick = {
                        Log.d("ChatBubble", "Long Pressed")
                        showBottomSheet = true
                    }
                )
        ) {
            Text(
                text = if (!message.content.isNullOrBlank()) message.content else if (message.attachments != null) "Message has no content, but it has attachments, and we can't render them yet." else "Unhandled",
                color = if (isSelf) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                fontSize = if (isOnlyEmoji) 48.sp else 16.sp,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(4) {
                    Column(
                        Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .clickable {
                                /* TODO: Do something! */
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        showBottomSheet = false
                                    }
                                }
                            }
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(Icons.Default.Square, "Not Localized yet", Modifier.size(34.dp))
                    }
                }
                Column(
                    Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .clickable {
                            /* TODO: Do something! */
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(Icons.Default.MoreHoriz, "Not Localized yet", Modifier.size(34.dp))
                }
            }
            repeat(5) {
                ListItem(
                    headlineContent = { Text("Item") },
                    leadingContent = { Icon(Icons.Default.Square, "Not Localized yet") },
                    modifier = Modifier.clickable { /* TODO: Do something! */ })
            }
        }
    }
}