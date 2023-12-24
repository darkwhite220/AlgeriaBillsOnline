package com.darkwhite.feature.createaccount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.darkwhite.feature.createaccount.CreateAccountEvent.OnCreateAccountClick
import com.darkwhite.feature.createaccount.components.ButtonCreateAccount
import com.darkwhite.feature.createaccount.components.MyTextFieldTypes
import com.darkwhite.feature.createaccount.components.TextFieldCreateAccount
import com.darkwhite.feature.createaccount.components.textFieldMap
import com.darkwhite.feature.createaccount.dialog.DialogDataType
import com.darkwhite.feature.createaccount.dialog.ResponseDialog
import com.darkwhite.feature.createaccount.uistate.CreateAccountUiState
import com.darkwhite.feature.createaccount.uistate.FormUiState
import earth.core.designsystem.components.ImageCaptcha
import earth.core.designsystem.components.MyHeightSpacer
import earth.core.designsystem.components.TextTitleLarge
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.mediumDp
import earth.core.designsystem.components.topappbar.CenteredTopAppBar
import earth.feature.createaccount.R

enum class ShowDialog {
    SUCCESS, FAILED, NONE
}

@Composable
internal fun CreateAccountRoute(
    onAccountCreated: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: CreateAccountViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formUiState by viewModel.formUiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(ShowDialog.NONE) }
    
    CreateAccountScreen(
        uiState = uiState,
        formUiState = formUiState,
        onBackClick = onBackClick,
        onCreateAccountEvent = viewModel::onCreateAccountEvent
    )
    
    // Dialog
    when (uiState) {
        CreateAccountUiState.Success -> {
            ResponseDialog(dialogData = DialogDataType.SUCCESS.dialogData, onClick = onBackClick)
        }
        is CreateAccountUiState.Failed -> {
            ResponseDialog(
                dialogData = DialogDataType.FAILED.dialogData,
                onClick = viewModel::onFailedDialogClose
            )
        }
        else -> { /* No op */
        }
    }
    
//    when (showDialog) {
//        ShowDialog.SUCCESS -> {
//            ResponseDialog(dialogData = DialogDataType.SUCCESS.dialogData, onClick = onBackClick)
//        }
//        ShowDialog.FAILED -> {
//            ResponseDialog(
//                dialogData = DialogDataType.FAILED.dialogData,
//                onClick = viewModel::onFailedDialogClose
//            )
//        }
//        ShowDialog.NONE -> { /* No op */
//        }
//    }
    
}

//@Preview(showBackground = true)
@Composable
private fun CreateAccountScreen(
    uiState: CreateAccountUiState = CreateAccountUiState.InitialState,
    formUiState: FormUiState = FormUiState(),
    onCreateAccountEvent: (CreateAccountEvent) -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
    ) {
        CenteredTopAppBar(
            titleId = R.string.create_account,
            onBackClick = onBackClick,
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = largeDp)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val focusManager = LocalFocusManager.current
            
            MyHeightSpacer(largeDp)
            TextTitleLarge(textId = R.string.register_to_see_your_sonalgaz_bills)
            MyHeightSpacer(largeDp)
            
            textFieldMap.entries.forEachIndexed { index, item ->
                val focusRequester = remember { FocusRequester() }
                LaunchedEffect(Unit) {
                    if (index == 0) focusRequester.requestFocus()
                }
                
                if (item.key == MyTextFieldTypes.CAPTCHA) {
                    ImageCaptcha(url = "") // TODO
                    MyHeightSpacer(largeDp)
                }
                
                TextFieldCreateAccount(
                    focusRequester = focusRequester,
                    fieldValues = item.value,
                    value = formUiState.currentValue(item.key),
                    isValid = formUiState.currentIsValid(item.key),
                    onValueChange = { newValue ->
                        onFormEventValueChange(
                            newValue = newValue,
                            textFieldTypes = item.key,
                            onCreateAccountEvent = onCreateAccountEvent,
                        )
                    },
                    onTextFieldEvent = { event ->
                        when (event) {
                            TextFieldEvent.OnKeyboardPreviousActions -> {
                                focusManager.moveFocus(FocusDirection.Previous)
                            }
                            TextFieldEvent.OnKeyboardNextActions -> {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                            TextFieldEvent.OnKeyboardDoneActions -> {
                                focusManager.clearFocus(true)
                            }
                        }
                    },
                )
                
                TextFieldDescription(description = item.value.description)
                
                MyHeightSpacer(mediumDp)
            }
            
            ButtonCreateAccount(
                textId = R.string.create_account,
                isLoading = uiState == CreateAccountUiState.Loading,
                onClick = { onCreateAccountEvent(OnCreateAccountClick) },
            )
            
            MyHeightSpacer(largeDp)
        }
    }
}

private fun onFormEventValueChange(
    newValue: String,
    textFieldTypes: MyTextFieldTypes,
    onCreateAccountEvent: (CreateAccountEvent) -> Unit,
) {
    onCreateAccountEvent(
        when (textFieldTypes) {
            MyTextFieldTypes.USERNAME -> {
                CreateAccountEvent.OnUserNameValueChange(newValue)
            }
            MyTextFieldTypes.EMAIL -> {
                CreateAccountEvent.OnEmailValueChange(newValue)
            }
            MyTextFieldTypes.PASSWORD -> {
                CreateAccountEvent.OnPasswordValueChange(newValue)
            }
            MyTextFieldTypes.REPEAT_PASSWORD -> {
                CreateAccountEvent.OnRepeatPasswordValueChange(newValue)
            }
            MyTextFieldTypes.CAPTCHA -> {
                CreateAccountEvent.OnCaptchaValueChange(newValue)
            }
        }
    )
}

@Composable
private fun TextFieldDescription(description: String? = null) {
    description?.let {
        CompositionLocalProvider(
            LocalContentColor provides LocalContentColor.current.copy(alpha = .6f)
        ) {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
