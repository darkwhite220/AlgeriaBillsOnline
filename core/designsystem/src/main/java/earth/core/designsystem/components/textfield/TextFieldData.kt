package earth.core.designsystem.components.textfield

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType


enum class CreateAccountTextFieldTypes {
    USERNAME, EMAIL, REFERENCE, PASSWORD, REPEAT_PASSWORD, CAPTCHA
}

@Immutable
data class MyTextFieldItem(
    val label: String = "",
    val placeholder: String? = null,
    val description: String? = null,
    val supportingText: String = "",
    val trailingIcon: Boolean = false,
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
)

val createAccountTextFieldMap = mapOf(
    CreateAccountTextFieldTypes.USERNAME to MyTextFieldItem(
        label = "Username",
        supportingText = "Min 8 alphanumeric characters",
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.None,
            autoCorrect = true
        ),
    ),
    CreateAccountTextFieldTypes.EMAIL to MyTextFieldItem(
        label = "Email",
        placeholder = "Example@gmail.com",
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email,
            autoCorrect = true,
        ),
    ),
    CreateAccountTextFieldTypes.REFERENCE to MyTextFieldItem(
        label = "Reference",
        placeholder = "000000000000000",
        supportingText = "Must be 15 digits",
        trailingIcon = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number,
            autoCorrect = false,
        ),
    ),
    CreateAccountTextFieldTypes.PASSWORD to MyTextFieldItem(
        label = "Password",
        supportingText = "Wrong format",
        description = """   Password must contain:
        - at least 8 characters
        - at least one uppercase
        - at least one lowercase
        - at least one special character (_-*/,\$!?.")
        """.trimMargin(),
        trailingIcon = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
    ),
    CreateAccountTextFieldTypes.REPEAT_PASSWORD to MyTextFieldItem(
        label = "Repeat password",
        supportingText = "Must be same as password",
        trailingIcon = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
    ),
    CreateAccountTextFieldTypes.CAPTCHA to MyTextFieldItem(
        label = "Image code",
        supportingText = "5 alphanumeric characters",
//        description = "   Type what you see in the image",
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text,
            autoCorrect = false,
            capitalization = KeyboardCapitalization.None
        ),
    ),
)

enum class SignInTextFieldTypes {
    USERNAME, PASSWORD
}

val signInTextFieldMap = mapOf(
    SignInTextFieldTypes.USERNAME to MyTextFieldItem(
        label = "Username",
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.None,
            autoCorrect = true
        ),
    ),
    SignInTextFieldTypes.PASSWORD to MyTextFieldItem(
        label = "Password",
        supportingText = "Wrong format",
        trailingIcon = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
            autoCorrect = false,
        ),
    ),
)