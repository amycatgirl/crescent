package space.bunniesin.crescent.models.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import space.bunniesin.crescent.api.ApiClient
import space.bunniesin.crescent.models.api.websocket.ReadyEvent
import space.bunniesin.crescent.utilities.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewmodel : ViewModel() {
    var messageList = mutableStateListOf<Any>()
        private set

    init {
        viewModelScope.launch {
            EventBus.subscribe<Any> { ev ->
                Log.d("EventBus", "$ev")
                messageList.add(ev)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            EventBus.subscribe<ReadyEvent> { event ->
                event.users.forEach {
                    ApiClient.cache[it.id] = it
                }
            }
        }

    }
}