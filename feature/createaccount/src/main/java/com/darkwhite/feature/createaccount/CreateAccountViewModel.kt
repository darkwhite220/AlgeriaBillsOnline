package com.darkwhite.feature.createaccount

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkwhite.feature.createaccount.Util.isValidCaptcha
import com.darkwhite.feature.createaccount.Util.isValidEmail
import com.darkwhite.feature.createaccount.Util.isValidPassword
import com.darkwhite.feature.createaccount.Util.isValidUsername
import com.darkwhite.feature.createaccount.uistate.CreateAccountUiState
import com.darkwhite.feature.createaccount.uistate.FormUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import earth.core.data.util.NetworkMonitorRepository
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val network: NetworkMonitorRepository
) : ViewModel() {
    
    private var _formUiState = MutableStateFlow(FormUiState())
    val formUiState: StateFlow<FormUiState> = _formUiState.asStateFlow()
    
    private var startRequest = MutableStateFlow(false)
    
    val uiState: StateFlow<CreateAccountUiState> = startRequest.flatMapLatest {
        if (it) {
            createAccountUiState().flatMapLatest { result ->
                when(result) {
                    is CreateAccountUiState.Failed -> {
                        // TODO update the isValid values
                    }
                    else -> { /* No op*/ }
                }
                flowOf(result)
            }
        } else {
            flowOf(CreateAccountUiState.InitialState)
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CreateAccountUiState.InitialState
        )
    
    init {
        Log.d(TAG, "init: ")
//        val passwordsToTest = listOf(
//            "ValidPass123#",
//            "Shot12#",
//            "NoSpecialCharacter123",
//            "NoDigitUpperCaseLowercase#",
//            "Valid@Password123",
//            "AnotherValidP@ssword",
//            "12345678", // No letters
//            "OnlyLettersNoDigits", // No digits
//            "NoUppercase#lowercase123", // No uppercase letter
//            "NoLowercase#UPPERCASE123" // No lowercase letter
//        )
//        for (password in passwordsToTest) {
//            val isValid = validatePassword(password)
//            println("$password is ${if (isValid) "valid" else "not valid"}")
//        }
//        val testStrings = listOf(
//            "Abcd1234",     // Valid: meets all conditions
//            "AbCdEfGh",     // Valid: meets all conditions
//            "12345678",     // Valid: meets all conditions
//            "Special!@#",   // Invalid: contains special characters
//            "short",        // Invalid: less than 8 characters
//            "With Space",   // Invalid: contains whitespace
//            "UPPERCASE123", // Valid: meets all conditions
//            "lowercase",    // Valid: meets all conditions
//            "mix123!@#",    // Invalid: contains special characters
//            " OnlyDigits123456789" // Invalid: contains only digits
//        )
//
//        for (testString in testStrings) {
//            val isValid = validateUsername(testString)
//            println("'$testString' is ${if (isValid) "valid" else "not valid"}")
//        }
//        val emailTestCases = listOf(
//            "john.doe@example.com",            // Valid
//            "user123@mail.domain",             // Valid
//            "alice.smith123@company.co.uk",    // Valid
//            "invalid-email",                   // Invalid: Missing '@' symbol
//            "missing-at-symbol.com",           // Invalid: Missing '@' symbol
//            "no-domain@.com",                  // Invalid: Missing domain name
//            "special!characters@example.com",  // Invalid: Contains special characters
//            "spaces not@allowed.com",          // Invalid: Contains spaces
//            "@startwithatsymbol.com",          // Invalid: Starts with '@' symbol
//            "endwithatsymbol@",                // Invalid: Ends with '@' symbol
//            "double@@atsymbol.com",            // Invalid: Double '@' symbols
//            "john.doe@domain@company.com"      // Invalid: Double '@' symbols
//        )
//        for (email in emailTestCases) {
//            val isValid = email.isValidEmail()
//            println("'$email' is ${if (isValid) "valid" else "not valid"}")
//        }
    }
    
    fun onCreateAccountEvent(event: CreateAccountEvent) {
        when (event) {
            is CreateAccountEvent.OnUserNameValueChange -> {
                updateUserName(event.value)
            }
            is CreateAccountEvent.OnEmailValueChange -> {
                updateEmail(event.value)
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
        viewModelScope.launch { startRequest.emit(false) }
    }
    
    private fun onCreateAccountClick() {
//        if (_formUiState.value.username.isNotEmpty() && _formUiState.value.usernameIsValid &&
//            _formUiState.value.email.isNotEmpty() && _formUiState.value.emailIsValid &&
//            _formUiState.value.password.isNotEmpty() && _formUiState.value.passwordIsValid &&
//            _formUiState.value.repeatPassword.isNotEmpty() && _formUiState.value.repeatPasswordIsValid &&
//            _formUiState.value.captcha.isNotEmpty() && _formUiState.value.captchaIsValid
//        ) {
        viewModelScope.launch {
            startRequest.emit(true)
        }
//        }
    }
    
    private fun updateUserName(value: String) {
        _formUiState.update {
            it.copy(
                username = value.trimEnd(),
                usernameIsValid = value.isValidUsername()
            )
        }
    }
    
    private fun updateEmail(value: String) {
        _formUiState.update {
            it.copy(
                email = value.trimEnd(),
                emailIsValid = value.isValidEmail()
            )
        }
    }
    
    private fun updatePassword(value: String) {
        _formUiState.update {
            it.copy(
                password = value.trimEnd(),
                passwordIsValid = value.isValidPassword()
            )
        }
    }
    
    private fun updateRepeatPassword(value: String) {
        _formUiState.update {
            it.copy(
                repeatPassword = value.trimEnd(),
                repeatPasswordIsValid = value == it.password
            )
        }
    }
    
    private fun updateCaptcha(value: String) {
        _formUiState.update {
            it.copy(
                captcha = value.trimEnd(),
                captchaIsValid = value.isValidCaptcha()
            )
        }
    }
    
    companion object {
        private const val TAG = "CreateAccountViewModel"
    }
}


private fun createAccountUiState(): Flow<CreateAccountUiState> =
    flow {
        println("createAccountUiState")
//        startRequest.flatMapLatest {
//            println("startRequest 2 ${startRequest.value}")
//            flowOf(
//                if (it) {
        emit(CreateAccountUiState.Loading)
        delay(2000)
        println("createAccountUiState 2000 delay")
        emit(CreateAccountUiState.Success)
        println("createAccountUiState Success")
//                }
//            )
//        }
//        println("startRequest 3 ${startRequest.value}")
    }

