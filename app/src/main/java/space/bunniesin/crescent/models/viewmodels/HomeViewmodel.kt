package space.bunniesin.crescent.models.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import space.bunniesin.crescent.api.ApiClient
import space.bunniesin.crescent.models.api.channels.Channel
import space.bunniesin.crescent.models.api.websocket.ReadyEvent
import space.bunniesin.crescent.utilities.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewmodel @Inject constructor(
    val stoat: ApiClient
) : ViewModel() {
    var channels = mutableStateListOf<Channel>()
    init {
        CoroutineScope(Dispatchers.IO).launch { // TODO: should be managed by the client
            EventBus.subscribe<ReadyEvent> {
                viewModelScope.launch {
                    channels.addAll(fetchChannels())
                }
            }
        }
    }

    private suspend fun fetchChannels(): List<Channel> {
        return stoat.getDirectMessages()
    }
}