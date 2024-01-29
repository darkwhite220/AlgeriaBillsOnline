package earth.darkwhite.feature.signin

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import earth.core.common.Result
import earth.core.common.asResult
import earth.core.data.NetworkMonitorRepository
import earth.core.designsystem.utils.Constants.VIEW_MODEL_SUBSCRIPTION_TIME
import earth.core.designsystem.utils.Util.isValidSignInPassword
import earth.core.domain.SetLastFetchTimeUseCase
import earth.core.domain.signin.SignInUseCase
import earth.darkwhite.feature.signin.uistate.SignInFormState
import earth.darkwhite.feature.signin.uistate.SignInUiState
import earth.darkwhite.feature.signin.uistate.asSignInCredentials
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SignInViewModel @Inject constructor(
    signInUseCase: SignInUseCase,
    private val network: NetworkMonitorRepository,
    private val setLastFetchTimeUseCase: SetLastFetchTimeUseCase,
) : ViewModel() {
    
    private val isOnline = MutableStateFlow(false)
    var isPreviousFail by mutableStateOf(false)
        private set
    
    private val _signInFormState = MutableStateFlow(SignInFormState())
    val signInFormState: StateFlow<SignInFormState> = _signInFormState.asStateFlow()
    
    private val startSignInRequest = MutableStateFlow(false)
    
    val signInUiState: StateFlow<SignInUiState> = startSignInRequest.flatMapLatest {
        if (it) {
            signInUseCase.invoke(_signInFormState.value.asSignInCredentials())
                .asResult()
                .map { result ->
                    when (result) {
                        Result.Loading -> SignInUiState.Loading
                        is Result.Success -> {
                            updateLastFetchTime()
                            SignInUiState.Success(result.data)
                        }
                        is Result.Error -> {
                            isPreviousFail = true
                            SignInUiState.Failed(result.exception)
                        }
                    }
                }
        } else {
            flowOf(SignInUiState.InitialState)
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(VIEW_MODEL_SUBSCRIPTION_TIME),
            initialValue = SignInUiState.InitialState
        )
    
    init {
        Log.d(TAG, "init: ")
        viewModelScope.launch {
            network.networkStatus.collect {
                isOnline.value = it
            }
        }
    }
    
    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.OnUserNameValueChange -> {
                updateUserName(event.value)
            }
            is SignInEvent.OnPasswordValueChange -> {
                updatePassword(event.value)
            }
            SignInEvent.OnSignInClick -> {
                onSignInClick()
            }
        }
    }
    
    fun onFailedDialogClose() {
        viewModelScope.launch { startSignInRequest.update { false } }
    }
    
    private fun updateUserName(value: String) {
        isPreviousFail = false
        _signInFormState.update {
            it.copy(
                username = value,
                usernameIsValid = value.isNotEmpty()
            )
        }
    }
    
    private fun updatePassword(value: String) {
        isPreviousFail = false
        _signInFormState.update {
            it.copy(
                password = value,
                passwordIsValid = value.isValidSignInPassword(),
            )
        }
    }
    
    private fun onSignInClick() {
        if (!isOnline.value || isPreviousFail) {
            return
        }
        checkFieldsValue()
        if (_signInFormState.value.usernameIsValid && _signInFormState.value.passwordIsValid) {
            updateFormFieldEnabledState(false)
            startSignInRequest.update { true }
            updateFormFieldEnabledState(true)
        }
    }
    
    private fun checkFieldsValue() {
        if (_signInFormState.value.username.isEmpty()) {
            _signInFormState.update { it.copy(usernameIsValid = false) }
        }
        if (_signInFormState.value.password.isEmpty()) {
            _signInFormState.update { it.copy(passwordIsValid = false) }
        }
    }
    
    private fun updateFormFieldEnabledState(newValue: Boolean) {
        _signInFormState.update { it.copy(enabled = newValue) }
    }
    
    private fun updateLastFetchTime() = viewModelScope.launch {
        setLastFetchTimeUseCase.invoke(0L)
    }
    
    companion object {
        private const val TAG = "SignInViewModel"
    }
}