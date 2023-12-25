package com.darkwhite.feature.createaccount

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
import com.darkwhite.feature.createaccount.uistate.toSignupRequestBody
import dagger.hilt.android.lifecycle.HiltViewModel
import earth.core.common.Result
import earth.core.common.ResultNoData
import earth.core.common.asResult
import earth.core.common.asResultNoData
import earth.core.data.SignupRepository
import earth.core.data.util.NetworkMonitorRepository
import earth.core.domain.GetSignupCaptchaUseCase
import earth.core.domain.GetSignupStateUseCase
import earth.core.throwablemodel.SignupThrowable
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
class CreateAccountViewModel @Inject constructor(
    getSignupCaptchaUseCase: GetSignupCaptchaUseCase,
    getSignupStateUseCase: GetSignupStateUseCase,
    private val network: NetworkMonitorRepository,
    private val signupRepository: SignupRepository,
) : ViewModel() {
    
    // TODO disable text fields on signup click
    // TODO show broken image + reload icon if captcha fails
    // TODO save to Database
    // TODO check network before
    
    private var _formUiState = MutableStateFlow(FormUiState())
    val formUiState: StateFlow<FormUiState> = _formUiState.asStateFlow()
    
    private var startCaptchaRequest = MutableStateFlow(false)
    
    val captchaUiState: StateFlow<CaptchaUiState> = startCaptchaRequest.flatMapLatest {
        getSignupCaptchaUseCase()
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Loading -> CaptchaUiState.Loading
                    is Result.Success -> CaptchaUiState.Success(result.data)
                    is Result.Error -> CaptchaUiState.Failed(result.exception)
                }
            }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CaptchaUiState.Loading
        )
    
    private var startSignupRequest = MutableStateFlow(false)
    
    val signupUiState: StateFlow<SignupUiState> = startSignupRequest.flatMapLatest { startRequest ->
        if (startRequest) {
            getSignupStateUseCase(_formUiState.value.toSignupRequestBody())
                .asResultNoData()
                .map { result ->
                    when (result) {
                        is ResultNoData.Loading -> SignupUiState.Loading
                        is ResultNoData.Success -> SignupUiState.Success
                        is ResultNoData.Error -> {
                            if (result.exception == SignupThrowable.WrongCaptchaException) {
                                startCaptchaRequest.emit(!startCaptchaRequest.value)
                            }
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
        viewModelScope.launch { startSignupRequest.emit(false) }
    }
    
    private fun onCreateAccountClick() {
        if (_formUiState.value.username.isNotEmpty() && _formUiState.value.usernameIsValid &&
            _formUiState.value.email.isNotEmpty() && _formUiState.value.emailIsValid &&
            _formUiState.value.reference.isNotEmpty() && _formUiState.value.referenceIsValid &&
            _formUiState.value.password.isNotEmpty() && _formUiState.value.passwordIsValid &&
            _formUiState.value.repeatPassword.isNotEmpty() && _formUiState.value.repeatPasswordIsValid &&
            _formUiState.value.captcha.isNotEmpty() && _formUiState.value.captchaIsValid
        ) {
            viewModelScope.launch {
                startSignupRequest.emit(true)
            }
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
        _formUiState.update {
            it.copy(
                captcha = value.trim(),
                captchaIsValid = value.isValidCaptcha()
            )
        }
    }
    
    companion object {
        private const val TAG = "CreateAccountViewModel"
        private const val MAX_REFERENCE_LENGTH = 15
    }
}