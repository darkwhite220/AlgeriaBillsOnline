package com.darkwhite.feature.createaccount.uistate

import androidx.compose.runtime.Stable
import com.darkwhite.feature.createaccount.components.MyTextFieldTypes
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
    fun currentValue(textFieldTypes: MyTextFieldTypes): String {
        return when (textFieldTypes) {
            MyTextFieldTypes.USERNAME -> {
                username
            }
            MyTextFieldTypes.EMAIL -> {
                email
            }
            MyTextFieldTypes.REFERENCE -> {
                reference
            }
            MyTextFieldTypes.PASSWORD -> {
                password
            }
            MyTextFieldTypes.REPEAT_PASSWORD -> {
                repeatPassword
            }
            MyTextFieldTypes.CAPTCHA -> {
                captcha
            }
        }
    }
    
    fun currentIsValid(textFieldTypes: MyTextFieldTypes): Boolean {
        return when (textFieldTypes) {
            MyTextFieldTypes.USERNAME -> {
                usernameIsValid
            }
            MyTextFieldTypes.EMAIL -> {
                emailIsValid
            }
            MyTextFieldTypes.REFERENCE -> {
                referenceIsValid
            }
            MyTextFieldTypes.PASSWORD -> {
                passwordIsValid
            }
            MyTextFieldTypes.REPEAT_PASSWORD -> {
                repeatPasswordIsValid
            }
            MyTextFieldTypes.CAPTCHA -> {
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
