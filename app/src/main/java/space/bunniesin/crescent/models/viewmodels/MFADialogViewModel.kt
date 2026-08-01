package space.bunniesin.crescent.models.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import space.bunniesin.crescent.api.ApiClient
import space.bunniesin.crescent.datastore.ConfigDataStoreKeys
import space.bunniesin.crescent.datastore.PreferenceDataStoreHelper

enum class SelectedMethod {
    TWO_FACTOR_AUTHENTICATION,
    RECOVERY_CODE
}

@HiltViewModel(assistedFactory = MFADialogViewModel.Factory::class)
class MFADialogViewModel @AssistedInject constructor(
    private val stoat: ApiClient,
    @Assisted val ticket: String,
    @Assisted context: Context) : ViewModel() {
        @AssistedFactory
        interface Factory {
            fun create(ticket: String, context: Context): MFADialogViewModel
        }
    private val preferenceDataStoreHelper = PreferenceDataStoreHelper(context)

    suspend fun handleMFAMethod(method: SelectedMethod, code: String? = null) {
        when (method) {
            SelectedMethod.TWO_FACTOR_AUTHENTICATION -> {
                require(code != null)
                val response = stoat.confirm2FA(ticket, code)
                val serializedSession = stoat.jsonDeserializer.encodeToString(response)
                preferenceDataStoreHelper.putPreference(
                    ConfigDataStoreKeys.SerializedCurrentSession,
                    serializedSession
                )
            }

            else -> Log.w("MFADialogVM", "I can't handle this method yet!")
        }
    }
}