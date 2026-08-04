package space.bunniesin.crescent.models.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import space.bunniesin.crescent.api.ApiClient
import space.bunniesin.crescent.models.api.websocket.PartialMessage
import kotlinx.coroutines.launch
import space.bunniesin.crescent.api.InstanceConfig
import space.bunniesin.crescent.api.gateway.ofType
import space.bunniesin.crescent.models.api.User
import space.bunniesin.crescent.models.api.channels.Channel
import space.bunniesin.crescent.utilities.EventBus

data class ChatState(
    val messages: List<PartialMessage> = listOf<PartialMessage>(),
    val loading: Boolean = true,
    val user: User? = null,
    val channel: Channel? = null,
    val currentMessageContent: String = ""
)

@HiltViewModel(assistedFactory = ChatViewmodel.Factory::class)
class ChatViewmodel @AssistedInject constructor(
    val client: ApiClient,
    @Assisted private val user: User?,
    @Assisted private val channel: Channel,
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(user: User?, channel: Channel): ChatViewmodel
    }

    private val _uiState = MutableStateFlow(ChatState())

    val state: StateFlow<ChatState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            curr -> curr.copy(channel = channel, user = user)
        }

        viewModelScope.launch {
            initialFetch()
        }

        observeIncomingMessages()
    }
    suspend fun getMessages(channel: String): List<PartialMessage> {
        return client.getChannelMessages(channel).toMutableList()
    }

    fun addMessage(message: PartialMessage) {
        _uiState.update {
            currentState ->
            currentState.copy(messages = currentState.messages.plus(message))
        }
    }

    suspend fun sendMessage(where: Channel, content: String) {
        client.sendMessage(where, content)
    }

    private fun observeIncomingMessages() {
        viewModelScope.launch {
            client.gateway.events.ofType<PartialMessage>().collect {
                message ->
                _uiState.update {
                    curr ->
                    if (message.channelId == channel.id) {
                        curr.copy(messages = listOf(message) + curr.messages)
                    } else {
                        curr
                    }
                }
            }
        }
    }

    suspend fun initialFetch() {
        _uiState.update {
            currentState ->
            currentState.copy(messages = getMessages(channel.id), loading = false)
        }
    }

    fun getConfig(): InstanceConfig = client.config
    fun isSelf(id: String?): Boolean = client.currentSession?.userId == id
}