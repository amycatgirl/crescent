package space.bunniesin.crescent.models.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import space.bunniesin.crescent.api.ApiClient
import javax.inject.Inject

@HiltViewModel
class SettingsViewmodel @Inject constructor(
    val stoat: ApiClient
) : ViewModel() {
    suspend fun dropSession() = stoat.dropSession()
}