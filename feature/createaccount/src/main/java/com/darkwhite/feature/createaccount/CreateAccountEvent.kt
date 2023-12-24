package com.darkwhite.feature.createaccount

sealed interface CreateAccountEvent {
    
    data class OnUserNameValueChange(val value: String) : CreateAccountEvent
    data class OnEmailValueChange(val value: String) : CreateAccountEvent
    data class OnPasswordValueChange(val value: String) : CreateAccountEvent
    data class OnRepeatPasswordValueChange(val value: String) : CreateAccountEvent
    data class OnCaptchaValueChange(val value: String) : CreateAccountEvent
    
    data object OnCreateAccountClick : CreateAccountEvent
    
}