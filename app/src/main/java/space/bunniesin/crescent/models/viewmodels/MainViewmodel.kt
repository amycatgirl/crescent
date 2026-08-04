package space.bunniesin.crescent.models.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import space.bunniesin.crescent.api.ApiClient
import space.bunniesin.crescent.models.api.websocket.ReadyEvent
import space.bunniesin.crescent.utilities.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewmodel @Inject constructor(
    val stoat: ApiClient
) : ViewModel() {
    // lol
}