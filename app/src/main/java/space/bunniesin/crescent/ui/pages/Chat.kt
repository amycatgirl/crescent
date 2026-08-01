package space.bunniesin.crescent.ui.pages

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import space.bunniesin.crescent.R
import space.bunniesin.crescent.api.ApiClient
import space.bunniesin.crescent.models.api.User
import space.bunniesin.crescent.models.api.channels.Channel
import space.bunniesin.crescent.models.api.websocket.PartialMessage
import space.bunniesin.crescent.models.viewmodels.ChatState
import space.bunniesin.crescent.models.viewmodels.ChatViewmodel
import space.bunniesin.crescent.ui.composables.ChatBubble
import space.bunniesin.crescent.ui.composables.CustomTextField
import space.bunniesin.crescent.ui.composables.ProfileImage
import space.bunniesin.crescent.ui.composables.SystemMessageDisplay
import space.bunniesin.crescent.utilities.EventBus

// TODO: Currently it's buggy and might crash.

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ChatPage(
    viewmodel: ChatViewmodel,
    navigateToUserProfile: () -> Unit = {},
    goBack: () -> Unit
) {
    var messageValue by remember { mutableStateOf("") }
    val navigator = rememberSupportingPaneScaffoldNavigator()
    val state by viewmodel.state.collectAsState()

    val scope = rememberCoroutineScope()

    // TODO: Move to background thread, should be inside viewmodel anyway
    LaunchedEffect(state.channel?.id) {
        EventBus.subscribe<PartialMessage> {
            if (it.channelId == state.channel?.id) {
                viewmodel.addMessage(it)
            }
        }
    }

    BackHandler(navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    val icon = if (state.user != null) {
                        state.user!!.avatar?.let { "${viewmodel.getConfig().cdn}/avatars/${it.id}?max_side=256" }
                    } else if (state.channel is Channel.Group) {
                        (state.channel as Channel.Group).icon?.let { "${viewmodel.getConfig().cdn}/icons/${it.id}?max_side=256" }
                    } else {
                        null
                    }

                    val name = if (state.user != null) {
                        state.user!!.displayName
                            ?: "${state.user!!.username}#${state.user!!.discriminator}"
                    } else if (state.channel is Channel.Group) {
                        (state.channel as Channel.Group).name
                    } else {
                        "Unknown"
                    }
                    ChatHeaderTitle(name, icon)
                },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.ui_go_back)
                        )
                    }
                })
        }, bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .imePadding()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navigateToUserProfile() },
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                        .width(IntrinsicSize.Min),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                    )
                ) {
                    Icon(
                        painterResource(R.drawable.material_symbols_library_add),
                        "",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(bottom = 2.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(end = 7.dp)
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomTextField(
                        value = messageValue,
                        placeholder = stringResource(R.string.chat_send_message),
                        onValueChange = { messageValue = it },
                        singleLine = false,
                        modifier = Modifier
                            .height(IntrinsicSize.Min)
                            .weight(1f)
                            .heightIn(0.dp, 100.dp)
                    )
                    AnimatedVisibility(
                        visible = messageValue.isNotBlank(),
                        enter = scaleIn(animationSpec = tween(300)),
                        exit = scaleOut(animationSpec = tween(200))
                    ) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (state.channel != null) {
                                        viewmodel.sendMessage(
                                            state.channel as Channel,
                                            messageValue
                                        )
                                        messageValue = ""
                                    } else {
                                        Log.d("ChatPage", "What the fuck, channel is null?????")
                                    }
                                }
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .weight(1f),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                painterResource(R.drawable.material_symbols_send),
                                "",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }

    ) {
        ChatContent(viewmodel.client, state, { id -> viewmodel.isSelf(id) }, it)
    }
}

@Composable
fun ChatHeaderTitle(
    name: String,
    icon: String?,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProfileImage(
            fallback = name, url = icon, size = 26.dp
        )
        Text(
            text = name, maxLines = 1, overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ChatContent(
    stoat: ApiClient,
    state: ChatState,
    isSelf: (String?) -> Boolean,
    padding: PaddingValues
) {
    Box(modifier = Modifier
        .padding(padding)
        .padding(horizontal = 10.dp)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            reverseLayout = true
        ) {
            items(state.messages) { message ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    when (message.system != null) {
                        true -> SystemMessageDisplay(message.system)
                        false -> {
                            val author: User = stoat.cache[message.authorId].let {
                                (it
                                    ?: runBlocking {
                                        stoat.fetchUser(message.authorId!!)
                                    }) as User
                            }
                            ChatBubble(
                                author,
                                message,
                                modifier = if (isSelf(message.authorId))
                                    Modifier.align(Alignment.BottomEnd)
                                else
                                    Modifier.align(Alignment.BottomStart),
                            )
                        }
                    }
                }
            }

        }
    }
}