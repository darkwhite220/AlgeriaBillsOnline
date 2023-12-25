package com.darkwhite.feature.createaccount.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.darkwhite.feature.createaccount.TextFieldEvent
import com.darkwhite.feature.createaccount.TextFieldEvent.OnKeyboardDoneActions
import com.darkwhite.feature.createaccount.TextFieldEvent.OnKeyboardNextActions
import com.darkwhite.feature.createaccount.TextFieldEvent.OnKeyboardPreviousActions
import earth.feature.createaccount.R


@Composable
fun TextFieldCreateAccount(
    focusRequester: FocusRequester,
    fieldType: MyTextFieldTypes,
    fieldValues: MyTextFieldItem,
    value: String,
    isValid: Boolean,
    onValueChange: (String) -> Unit,
    onTextFieldEvent: (TextFieldEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    
    fieldValues.apply {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth(.8f)
                .focusRequester(focusRequester),
            value = value,
            onValueChange = { onValueChange(it) },
            label = { Text(text = label) },
            placeholder = { PlaceHolder() },
            trailingIcon = {
                when (fieldType) {
                    MyTextFieldTypes.PASSWORD, MyTextFieldTypes.REPEAT_PASSWORD -> {
                        PasswordTrailingIcon(
                            passwordVisibility = passwordVisibility,
                            onClick = { passwordVisibility = !passwordVisibility })
                    }
                    MyTextFieldTypes.REFERENCE -> {
                        ReferenceTrailingIcon(onClick = { onTextFieldEvent(TextFieldEvent.OnReferenceIconClick) })
                    }
                    else -> {}
                }
            },
            supportingText = {
                SupportingText(isError = isValid.not())
            },
            isError = isValid.not(),
            visualTransformation =
            if (!trailingIcon || fieldType == MyTextFieldTypes.REFERENCE || passwordVisibility)
                VisualTransformation.None
            else PasswordVisualTransformation(),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(
                onPrevious = { onTextFieldEvent(OnKeyboardPreviousActions) },
                onNext = { onTextFieldEvent(OnKeyboardNextActions) },
                onDone = { onTextFieldEvent(OnKeyboardDoneActions) },
            ),
        )
    }
}

@Composable
private fun MyTextFieldItem.PlaceHolder() {
    placeholder?.let { Text(text = it) }
}

@Composable
private fun MyTextFieldItem.SupportingText(isError: Boolean) {
    if (supportingText.isNotBlank() && isError) {
        Text(
            text = supportingText,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun MyTextFieldItem.PasswordTrailingIcon(
    passwordVisibility: Boolean,
    onClick: () -> Unit
) {
    if (trailingIcon) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(
                    id = if (passwordVisibility) R.drawable.eye_off_outline
                    else R.drawable.eye_outline
                ),
                contentDescription = stringResource(R.string.password_visibility_icon)
            )
        }
    }
}

@Composable
private fun MyTextFieldItem.ReferenceTrailingIcon(
    onClick: () -> Unit
) {
    if (trailingIcon) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(R.string.reference_detail_icon)
            )
        }
    }
}
