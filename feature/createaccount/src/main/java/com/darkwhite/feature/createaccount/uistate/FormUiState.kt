package com.darkwhite.feature.createaccount.uistate

import androidx.compose.runtime.Stable
import com.darkwhite.feature.createaccount.components.MyTextFieldTypes


@Stable
data class FormUiState(
    val username: String = "",
    val usernameIsValid: Boolean = true,
    
    val email: String = "",
    val emailIsValid: Boolean = true,
    
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