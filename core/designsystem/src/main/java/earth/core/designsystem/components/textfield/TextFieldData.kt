package earth.core.designsystem.components.textfield

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import earth.core.designsystem.R


enum class CreateAccountTextFieldTypes {
    USERNAME, EMAIL, REFERENCE, PASSWORD, REPEAT_PASSWORD, CAPTCHA
}

@Immutable
data class MyTextFieldItem(
    @StringRes val label: Int,
    @StringRes val placeholder: Int? = null,
    @StringRes val description: Int? = null,
    @StringRes val supportingText: Int? = null,
    val trailingIcon: Boolean = false,
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
)

val createAccountTextFieldMap = mapOf(
    CreateAccountTextFieldTypes.USERNAME to MyTextFieldItem(
        label = R.string.username,
        supportingText = R.string.min_username_length,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.None,
            autoCorrect = true
        ),
    ),
    CreateAccountTextFieldTypes.EMAIL to MyTextFieldItem(
        label = R.string.email,
        placeholder = R.string.example_gmail,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email,
            autoCorrect = true,
        ),
    ),
    CreateAccountTextFieldTypes.REFERENCE to MyTextFieldItem(
        label = R.string.reference,
        placeholder = R.string.example_reference,
        supportingText = R.string.reference_length,
        trailingIcon = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number,
            autoCorrect = false,
        ),
    ),
    CreateAccountTextFieldTypes.PASSWORD to MyTextFieldItem(
        label = R.string.password,
        supportingText = R.string.password_wrong_format,
        description = R.string.password_desc,
        trailingIcon = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
    ),
    CreateAccountTextFieldTypes.REPEAT_PASSWORD to MyTextFieldItem(
        label = R.string.repeat_password,
        supportingText = R.string.repeat_password_desc,
        trailingIcon = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
    ),
    CreateAccountTextFieldTypes.CAPTCHA to MyTextFieldItem(
        label = R.string.image_code,
        supportingText = R.string.image_code_desc,
//        description = "   Type what you see in the image",
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
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
        label = R.string.username,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.None,
            autoCorrect = true
        ),
    ),
    SignInTextFieldTypes.PASSWORD to MyTextFieldItem(
        label = R.string.password,
        supportingText = R.string.password_wrong_format,
        trailingIcon = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
            autoCorrect = false,
        ),
    ),
)