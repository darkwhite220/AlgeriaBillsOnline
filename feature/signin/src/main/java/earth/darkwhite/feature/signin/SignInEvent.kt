package earth.darkwhite.feature.signin

sealed interface SignInEvent {
    
    data class OnUserNameValueChange(val value: String) : SignInEvent
    data class OnPasswordValueChange(val value: String) : SignInEvent
    
    data object OnSignInClick : SignInEvent
    
}