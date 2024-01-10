package earth.darkwhite.feature.signin.uistate

import androidx.compose.runtime.Stable
import earth.core.designsystem.components.textfield.SignInTextFieldTypes

@Stable
data class SignInFormState(
    val enabled: Boolean = true,
    
    val username: String = "",
    val usernameIsValid: Boolean = true,
    
    val password: String = "",
    val passwordIsValid: Boolean = true,
) {
    fun currentValue(textFieldTypes: SignInTextFieldTypes): String {
        return when (textFieldTypes) {
            SignInTextFieldTypes.USERNAME -> {
                username
            }
            SignInTextFieldTypes.PASSWORD -> {
                password
            }
        }
    }
    
    fun currentIsValid(textFieldTypes: SignInTextFieldTypes): Boolean {
        return when (textFieldTypes) {
            SignInTextFieldTypes.USERNAME -> {
                usernameIsValid
            }
            SignInTextFieldTypes.PASSWORD -> {
                passwordIsValid
            }
        }
    }
}
