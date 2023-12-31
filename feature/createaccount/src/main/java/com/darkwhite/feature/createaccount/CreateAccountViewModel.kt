package com.darkwhite.feature.createaccount

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkwhite.feature.createaccount.Util.isValidCaptcha
import com.darkwhite.feature.createaccount.Util.isValidEmail
import com.darkwhite.feature.createaccount.Util.isValidPassword
import com.darkwhite.feature.createaccount.Util.isValidReference
import com.darkwhite.feature.createaccount.Util.isValidUsername
import com.darkwhite.feature.createaccount.uistate.CaptchaUiState
import com.darkwhite.feature.createaccount.uistate.FormUiState
import com.darkwhite.feature.createaccount.uistate.SignupUiState
import com.darkwhite.feature.createaccount.uistate.asNewUser
import com.darkwhite.feature.createaccount.uistate.asSignupRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import earth.core.common.Result
import earth.core.common.ResultNoData
import earth.core.common.asResult
import earth.core.common.asResultNoData
import earth.core.data.util.NetworkMonitorRepository
import earth.core.domain.createaccount.GetSignupCaptchaUseCase
import earth.core.domain.createaccount.GetSignupStateUseCase
import earth.core.domain.createaccount.InsertNewUserUseCase
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    getSignupCaptchaUseCase: GetSignupCaptchaUseCase,
    getSignupStateUseCase: GetSignupStateUseCase,
    insertNewUserUseCase: InsertNewUserUseCase,
    private val network: NetworkMonitorRepository,
) : ViewModel() {
    
    // TODO Captcha error implement
    
    private val isOnline = MutableStateFlow(false)
    
    private var _formUiState = MutableStateFlow(FormUiState())
    val formUiState: StateFlow<FormUiState> = _formUiState.asStateFlow()
    
    private var startCaptchaRequest = MutableStateFlow(false)
    
    val captchaUiState: StateFlow<CaptchaUiState> =
        combine(isOnline, startCaptchaRequest) { isOnline, _ ->
            Pair(isOnline, Unit)
        }.flatMapLatest { (isOnline, _) ->
            if (isOnline) {
                getSignupCaptchaUseCase()
                    .asResult()
                    .map { result ->
                        when (result) {
                            is Result.Loading -> CaptchaUiState.Loading
                            is Result.Success -> CaptchaUiState.Success(result.data)
                            is Result.Error -> CaptchaUiState.Failed(result.exception)
                        }
                    }
            } else {
                flowOf(CaptchaUiState.Failed(NetworkErrorException("No connection")))
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CaptchaUiState.Loading
        )
    
    private var startSignupRequest = MutableStateFlow(false)
    
    val signupUiState: StateFlow<SignupUiState> = startSignupRequest.flatMapLatest { startRequest ->
        if (startRequest) {
            getSignupStateUseCase(_formUiState.value.asSignupRequestBody())
                .asResultNoData()
                .map { result ->
                    when (result) {
                        is ResultNoData.Loading -> SignupUiState.Loading
                        is ResultNoData.Success -> {
                            insertNewUserUseCase(_formUiState.value.asNewUser())
                            SignupUiState.Success
                        }
                        is ResultNoData.Error -> {
                            startCaptchaRequest.value = !startCaptchaRequest.value
                            SignupUiState.Failed(result.exception)
                        }
                    }
                }
        } else {
            flowOf(SignupUiState.InitialState)
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SignupUiState.InitialState
        )
    
    
    init {
        Log.d(TAG, "init: ")
        viewModelScope.launch {
            network.networkStatus.collect {
                Log.d(TAG, "is connected $it: ")
                isOnline.value = it
            }
        }
    }
    
    fun onCreateAccountEvent(event: CreateAccountEvent) {
        when (event) {
            is CreateAccountEvent.OnUserNameValueChange -> {
                updateUserName(event.value)
            }
            is CreateAccountEvent.OnEmailValueChange -> {
                updateEmail(event.value)
            }
            is CreateAccountEvent.OnReferenceValueChange -> {
                updateReference(event.value)
            }
            is CreateAccountEvent.OnPasswordValueChange -> {
                updatePassword(event.value)
            }
            is CreateAccountEvent.OnRepeatPasswordValueChange -> {
                updateRepeatPassword(event.value)
            }
            is CreateAccountEvent.OnCaptchaValueChange -> {
                updateCaptcha(event.value)
            }
            CreateAccountEvent.OnCreateAccountClick -> {
                onCreateAccountClick()
            }
        }
    }
    
    fun onFailedDialogClose() {
        viewModelScope.launch { startSignupRequest.value = false }
    }
    
    private fun onCreateAccountClick() {
        if (!isOnline.value) {
            return
        }
        checkFieldsValue()
        if (_formUiState.value.username.isNotEmpty() && _formUiState.value.usernameIsValid &&
            _formUiState.value.email.isNotEmpty() && _formUiState.value.emailIsValid &&
            _formUiState.value.reference.isNotEmpty() && _formUiState.value.referenceIsValid &&
            _formUiState.value.password.isNotEmpty() && _formUiState.value.passwordIsValid &&
            _formUiState.value.repeatPassword.isNotEmpty() && _formUiState.value.repeatPasswordIsValid &&
            _formUiState.value.captcha.isNotEmpty() && _formUiState.value.captchaIsValid
        ) {
            viewModelScope.launch {
                updateFormFieldEnabledState(false)
                startSignupRequest.value = true
                updateFormFieldEnabledState(true)
            }
        }
    }
    
    private fun updateFormFieldEnabledState(enabled: Boolean) {
        _formUiState.update {
            it.copy(
                enabled = enabled,
            )
        }
    }
    
    private fun checkFieldsValue() {
        if (_formUiState.value.username.isEmpty()) {
            _formUiState.update { it.copy(usernameIsValid = false) }
        }
        if (_formUiState.value.email.isEmpty()) {
            _formUiState.update { it.copy(emailIsValid = false) }
        }
        if (_formUiState.value.reference.isEmpty()) {
            _formUiState.update { it.copy(referenceIsValid = false) }
        }
        if (_formUiState.value.password.isEmpty()) {
            _formUiState.update { it.copy(passwordIsValid = false) }
        }
        if (_formUiState.value.repeatPassword.isEmpty()) {
            _formUiState.update { it.copy(repeatPasswordIsValid = false) }
        }
        if (_formUiState.value.captcha.isEmpty()) {
            _formUiState.update { it.copy(captchaIsValid = false) }
        }
    }
    
    private fun updateUserName(value: String) {
        _formUiState.update {
            it.copy(
                username = value.trim(),
                usernameIsValid = value.isValidUsername()
            )
        }
    }
    
    private fun updateEmail(value: String) {
        _formUiState.update {
            it.copy(
                email = value.trim(),
                emailIsValid = value.isValidEmail()
            )
        }
    }
    
    private fun updateReference(value: String) {
        if (value.trim().length <= MAX_REFERENCE_LENGTH) {
            _formUiState.update {
                it.copy(
                    reference = value.trim(),
                    referenceIsValid = value.isValidReference()
                )
            }
        }
    }
    
    private fun updatePassword(value: String) {
        _formUiState.update {
            it.copy(
                password = value.trim(),
                passwordIsValid = value.isValidPassword()
            )
        }
    }
    
    private fun updateRepeatPassword(value: String) {
        _formUiState.update {
            it.copy(
                repeatPassword = value.trim(),
                repeatPasswordIsValid = value == it.password
            )
        }
    }
    
    private fun updateCaptcha(value: String) {
        if (value.length <= CAPTCHA_LENGTH) {
            _formUiState.update {
                it.copy(
                    captcha = value.trim(),
                    captchaIsValid = value.isValidCaptcha()
                )
            }
        }
    }
    
    companion object {
        private const val TAG = "CreateAccountViewModel"
        const val MAX_REFERENCE_LENGTH = 15
        const val MIN_PASSWORD_LENGTH = 8
        const val CAPTCHA_LENGTH = 5
    }
}