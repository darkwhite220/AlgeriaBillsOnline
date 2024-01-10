package earth.darkwhite.feature.signin

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import earth.core.data.util.NetworkMonitorRepository
import earth.core.designsystem.Util.isValidSignInPassword
import earth.darkwhite.feature.signin.uistate.SignInFormState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val network: NetworkMonitorRepository,
) : ViewModel() {
    // TODO uiState
    // TODO signIn logic, AppNetwork
    // TODO response
    // TODO forget password logic
    // TODO save new user
    // TODO get uiState to disable submit button
    private val isOnline = MutableStateFlow(false)
    
    private val _signInFormState = MutableStateFlow(SignInFormState())
    val signInFormState: StateFlow<SignInFormState> = _signInFormState.asStateFlow()
    
    private val startSignInRequest = MutableStateFlow(false)
    
    init {
        Log.d(TAG, "init: ")
        viewModelScope.launch {
            network.networkStatus.collect {
                Log.d(TAG, "isOnline $it: ")
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
    
    private fun updateUserName(value: String) {
        _signInFormState.update {
            it.copy(
                username = value.trim(),
                usernameIsValid = value.trim().isNotEmpty()
            )
        }
    }
    
    private fun updatePassword(value: String) {
        _signInFormState.update {
            it.copy(
                password = value.trim(),
                passwordIsValid = value.trim().isValidSignInPassword()
            )
        }
    }
    
    private fun onSignInClick() {
        if (!isOnline.value) {
            return
        }
        checkFieldsValue()
        if (_signInFormState.value.usernameIsValid && _signInFormState.value.passwordIsValid) {
            updateFormFieldEnabledState(false)
            startSignInRequest.value = true
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
    
    companion object {
        private const val TAG = "SignInViewModel"
    }
}