package com.darkwhite.feature.createaccount.uistate

import androidx.compose.runtime.Stable
import earth.core.designsystem.components.textfield.CreateAccountTextFieldTypes
import earth.core.database.User
import earth.core.networkmodel.SignupRequestBody


@Stable
data class FormUiState(
    val enabled: Boolean = true,
    
    val username: String = "",
    val usernameIsValid: Boolean = true,
    
    val email: String = "",
    val emailIsValid: Boolean = true,
    
    val reference: String = "",
    val referenceIsValid: Boolean = true,
    
    val password: String = "",
    val passwordIsValid: Boolean = true,
    
    val repeatPassword: String = "",
    val repeatPasswordIsValid: Boolean = true,
    
    val captcha: String = "",
    val captchaIsValid: Boolean = true,
) {
    fun currentValue(textFieldTypes: CreateAccountTextFieldTypes): String {
        return when (textFieldTypes) {
            CreateAccountTextFieldTypes.USERNAME -> {
                username
            }
            CreateAccountTextFieldTypes.EMAIL -> {
                email
            }
            CreateAccountTextFieldTypes.REFERENCE -> {
                reference
            }
            CreateAccountTextFieldTypes.PASSWORD -> {
                password
            }
            CreateAccountTextFieldTypes.REPEAT_PASSWORD -> {
                repeatPassword
            }
            CreateAccountTextFieldTypes.CAPTCHA -> {
                captcha
            }
        }
    }
    
    fun currentIsValid(textFieldTypes: CreateAccountTextFieldTypes): Boolean {
        return when (textFieldTypes) {
            CreateAccountTextFieldTypes.USERNAME -> {
                usernameIsValid
            }
            CreateAccountTextFieldTypes.EMAIL -> {
                emailIsValid
            }
            CreateAccountTextFieldTypes.REFERENCE -> {
                referenceIsValid
            }
            CreateAccountTextFieldTypes.PASSWORD -> {
                passwordIsValid
            }
            CreateAccountTextFieldTypes.REPEAT_PASSWORD -> {
                repeatPasswordIsValid
            }
            CreateAccountTextFieldTypes.CAPTCHA -> {
                captchaIsValid
            }
        }
    }
}

fun FormUiState.asSignupRequestBody(): SignupRequestBody = SignupRequestBody(
    nomprenom = "",
    email = email,
    telephone = "",
    username = username,
    reference = reference,
    newpass = password,
    cfnewpass = repeatPassword,
    captcha = captcha,
    btnAction = "",
)

fun FormUiState.asNewUser(): User = User(
    email = email,
    username = username,
    reference = reference,
    password = password,
)
