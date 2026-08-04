package space.bunniesin.crescent.models.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import space.bunniesin.crescent.api.ApiClient
import space.bunniesin.crescent.datastore.ConfigDataStoreKeys
import space.bunniesin.crescent.datastore.PreferenceDataStoreHelper
import space.bunniesin.crescent.models.api.authentication.SessionResponse
import kotlinx.coroutines.launch
import space.bunniesin.crescent.models.routes.ScreenKey
import space.bunniesin.crescent.nav.AppNavigator

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordShown: Boolean = false,
    val loading: Boolean = true,
)

@HiltViewModel(assistedFactory = LoginViewmodel.Factory::class)
class LoginViewmodel @AssistedInject constructor(
    private val client: ApiClient,
    private val navigator: AppNavigator,
    @Assisted context: Context
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(context: Context): LoginViewmodel
    }
    private val preferenceDataStoreHelper = PreferenceDataStoreHelper(context)
    private val _uiState = MutableStateFlow(LoginUiState())
    val state = _uiState.asStateFlow()

    // TODO: this too, should be moved into global app state, dumbass
    init {
        viewModelScope.launch {
            checkSession()
        }
    }

    // TODO: Move this to global app state, this shouldn't be inside the login page
    private suspend fun checkSession() {
        var currentSession: String = ""
        currentSession = preferenceDataStoreHelper.getFirstPreference(
            ConfigDataStoreKeys.SerializedCurrentSession,
            ""
        )

        Log.d("Login", currentSession)

        if (currentSession.isNotEmpty()) {
            Log.d("Login", "SerializedSession exists, attempting deserialization.")
            val availableSession = client.crescentJson.decodeFromString<SessionResponse.Success>(
                currentSession
            )
            client.currentSession = availableSession

            client.startSession(availableSession)
            navigator.navigate(ScreenKey.ConversationList)
        } else {
            Log.d("Login", "SerializedSession does not exist.")
            _uiState.update {
                it.copy(loading = false)
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(loading = true)
            }
            when (val response = client.loginWithPassword(state.value.email, state.value.password)) {
                is SessionResponse.NeedsMultiFactorAuth ->
                    navigator.navigate(
                        ScreenKey.LoginMFA(response.ticket)
                    )

                is SessionResponse.AccountDisabled -> {
                    _uiState.update {
                        it.copy(loading = false)
                    }
                    println("Account has been disabled")
                }
                is SessionResponse.Success -> {
                    val serializedSession = client.crescentJson.encodeToString(response)
                    Log.d("Preferences", "Login Completed, saving current session")
                    preferenceDataStoreHelper.putPreference(
                        ConfigDataStoreKeys.SerializedCurrentSession,
                        serializedSession
                    )
                    navigator.navigate(ScreenKey.ConversationList)
                }
            }
        }
    }

    fun toggleShowPassword() {
        _uiState.update {
            it.copy(isPasswordShown = !it.isPasswordShown)
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(
                password = password
            )
        }
    }

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email
            )
        }
    }
}
