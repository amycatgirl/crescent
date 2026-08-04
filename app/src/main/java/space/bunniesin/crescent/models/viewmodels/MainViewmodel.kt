package space.bunniesin.crescent.models.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import space.bunniesin.crescent.api.ApiClient

@HiltViewModel
class MainViewmodel @Inject constructor(
    val stoat: ApiClient
) : ViewModel() {
    // lol
}