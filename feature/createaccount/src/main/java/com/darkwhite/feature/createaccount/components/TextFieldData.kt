package com.darkwhite.feature.createaccount.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType


enum class MyTextFieldTypes {
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

val textFieldMap = mapOf(
    MyTextFieldTypes.USERNAME to MyTextFieldItem(
        label = "Username",
        supportingText = "Min 8 alphanumeric characters",
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.None,
            autoCorrect = true
        ),
    ),
    MyTextFieldTypes.EMAIL to MyTextFieldItem(
        label = "Email",
        placeholder = "Example@gmail.com",
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email,
            autoCorrect = true,
        ),
    ),
    MyTextFieldTypes.REFERENCE to MyTextFieldItem(
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
    MyTextFieldTypes.PASSWORD to MyTextFieldItem(
        label = "Password",
        supportingText = "Wrong format",
        description = """   Password must contain:
        - 8 characters
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
    MyTextFieldTypes.REPEAT_PASSWORD to MyTextFieldItem(
        label = "Repeat password",
        supportingText = "Must be same as password",
        trailingIcon = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
    ),
    MyTextFieldTypes.CAPTCHA to MyTextFieldItem(
        label = "Image code",
        supportingText = "5 alphanumeric characters",
        description = "   Type what you see in the image",
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
            autoCorrect = false,
            capitalization = KeyboardCapitalization.None
        ),
    ),
)