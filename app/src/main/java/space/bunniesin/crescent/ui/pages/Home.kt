package space.bunniesin.crescent.ui.pages

import android.util.Log
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import space.bunniesin.crescent.R
import space.bunniesin.crescent.models.api.Flags
import space.bunniesin.crescent.models.api.User
import space.bunniesin.crescent.models.api.channels.Channel
import space.bunniesin.crescent.models.viewmodels.HomeViewmodel
import space.bunniesin.crescent.ui.composables.FloatingActionButtonListItem
import space.bunniesin.crescent.ui.composables.FloatingActionButtonWithOptions
import space.bunniesin.crescent.ui.composables.PeopleListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    viewmodel: HomeViewmodel,
    navigateToChat: (channel: Channel, user: User?) -> Unit,
    navigateToDebug: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToStartConversation: (String) -> Unit,
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .safeDrawingPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(title = {
                Text(
                    stringResource(R.string.app_name),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }, actions = {
                IconButton(onClick = { navigateToDebug() }) {
                    Icon(
                        painterResource(R.drawable.material_symbols_adb),
                        contentDescription = "Open Debug login screen"
                    )
                }
                IconButton(onClick = { navigateToSettings() }) {
                    Icon(
                        painterResource(R.drawable.material_symbols_settings),
                        contentDescription = null
                    )
                }
            }, scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButtonWithOptions(options = listOf(
                FloatingActionButtonListItem(
                    icon = {
                        Icon(painterResource(R.drawable.material_symbols_person_add), contentDescription = "Add")
                    },
                    onClick = { navigateToStartConversation("People") }
                ),
                FloatingActionButtonListItem(
                    icon = {
                        Icon(painterResource(R.drawable.material_symbols_group_add), contentDescription = "Add")
                    },
                    onClick = { Log.d("Debug", "hi") }
                ),
            ))
        }) { innerPadding ->
        LazyColumn(
            modifier = Modifier.consumeWindowInsets(innerPadding), contentPadding = innerPadding
        ) {
            items(viewmodel.channels) { channel ->
                when (channel) {
                    is Channel.DirectMessage -> {
                        if (channel.active) {
                            val recipientId = remember(channel.id) {
                                channel.recipients.find { it != viewmodel.stoat.currentSession?.userId }
                            }
                            var author by remember(recipientId) {
                                mutableStateOf(viewmodel.stoat.users[recipientId] as? User)
                            }

                            if (author?.flags != Flags.DELETED.ordinal) {
                                LaunchedEffect(recipientId) {
                                    if (author == null && recipientId != null) {
                                        try {
                                            author = viewmodel.stoat.fetchUser(recipientId)
                                        } catch (e: Exception) {
                                            Log.e("Home", "Failed to fetch user $recipientId", e)
                                        }
                                    }
                                }

                                if (author == null) {
                                    PeopleListItem(isLoading = true, callback = {})
                                } else {
                                    val user = author!!
                                    if (user.flags != Flags.DELETED.ordinal) {
                                        PeopleListItem(
                                            user = user,
                                            status = user.status,
                                            callback = { navigateToChat(channel, user) })
                                    }
                                }
                            }
                        }
                    }

                    is Channel.Group -> {
                        PeopleListItem(channel = channel, callback = { navigateToChat(channel, null) })
                    }
                    else -> {}
                }
            }
        }
    }
}