package com.darkwhite.feature.createaccount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.darkwhite.feature.createaccount.CreateAccountEvent.OnCreateAccountClick
import com.darkwhite.feature.createaccount.components.ButtonCreateAccount
import com.darkwhite.feature.createaccount.components.CaptchaUi
import com.darkwhite.feature.createaccount.components.MyTextFieldTypes
import com.darkwhite.feature.createaccount.components.TextFieldCreateAccount
import com.darkwhite.feature.createaccount.components.TextFieldDescription
import com.darkwhite.feature.createaccount.components.textFieldMap
import com.darkwhite.feature.createaccount.dialog.DialogDataType
import com.darkwhite.feature.createaccount.dialog.ReferenceDetailDialog
import com.darkwhite.feature.createaccount.dialog.ResponseDialog
import com.darkwhite.feature.createaccount.uistate.CaptchaUiState
import com.darkwhite.feature.createaccount.uistate.FormUiState
import com.darkwhite.feature.createaccount.uistate.SignupUiState
import earth.core.designsystem.components.MyHeightSpacer
import earth.core.designsystem.components.MyWidthSpacer
import earth.core.designsystem.components.TextTitleLarge
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.mediumDp
import earth.core.designsystem.components.topappbar.CenteredTopAppBar
import earth.core.throwablemodel.SignupThrowable
import earth.feature.createaccount.R


@Composable
internal fun CreateAccountRoute(
    onAccountCreated: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: CreateAccountViewModel = hiltViewModel()
) {
    val captchaUiState by viewModel.captchaUiState.collectAsStateWithLifecycle()
    val signupUiState by viewModel.signupUiState.collectAsStateWithLifecycle()
    val formUiState by viewModel.formUiState.collectAsStateWithLifecycle()
    var showReferenceDetailDialog by remember { mutableStateOf(false) }
    
    CreateAccountScreen(
        signupUiState = signupUiState,
        captchaUiState = captchaUiState,
        formUiState = formUiState,
        onCreateAccountEvent = viewModel::onCreateAccountEvent,
        onBackClick = onBackClick,
        onShowReferenceDetailClick = { showReferenceDetailDialog = true },
    )
    
    // Signup Dialog
    ShowSignupDialog(
        signupUiState = signupUiState,
        onSuccessDialogClose = onBackClick,
        onFailDialogClose = viewModel::onFailedDialogClose
    )
    
    // Reference detail dialog
    if (showReferenceDetailDialog) {
        ReferenceDetailDialog(onDismiss = { showReferenceDetailDialog = false })
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateAccountScreen(
    signupUiState: SignupUiState = SignupUiState.InitialState,
    captchaUiState: CaptchaUiState = CaptchaUiState.Loading,
    formUiState: FormUiState = FormUiState(),
    onCreateAccountEvent: (CreateAccountEvent) -> Unit = {},
    onBackClick: () -> Unit = {},
    onShowReferenceDetailClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
    ) {
        CenteredTopAppBar(
            titleId = R.string.create_account,
            onBackClick = onBackClick,
        )
        
        Row(modifier = Modifier.verticalScroll(rememberScrollState())) {
            MyWidthSpacer(largeDp)
            Column(
                modifier = Modifier
//                .imePadding() // TODO CHECK IME PADDING
                    .fillMaxSize()
                    .weight(1f),
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
                    
                    CaptchaUi(
                        textFieldType = item.key,
                        captchaUiState = captchaUiState
                    )
                    
                    TextFieldCreateAccount(
                        focusRequester = focusRequester,
                        fieldType = item.key,
                        fieldValues = item.value,
                        value = formUiState.currentValue(item.key),
                        isValid = formUiState.currentIsValid(item.key),
                        enabled = formUiState.enabled,
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
                                TextFieldEvent.OnReferenceIconClick -> {
                                    onShowReferenceDetailClick()
                                }
                            }
                        },
                    )
                    
                    TextFieldDescription(description = item.value.description)
                    
                    MyHeightSpacer(mediumDp)
                }
                
                ButtonCreateAccount(
                    textId = R.string.create_account,
                    isLoading = signupUiState == SignupUiState.Loading,
                    onClick = { onCreateAccountEvent(OnCreateAccountClick) },
                )
                
                MyHeightSpacer(largeDp)
            }
            MyWidthSpacer(largeDp)
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
            MyTextFieldTypes.REFERENCE -> {
                CreateAccountEvent.OnReferenceValueChange(newValue)
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
private fun ShowSignupDialog(
    signupUiState: SignupUiState,
    onSuccessDialogClose: () -> Unit,
    onFailDialogClose: () -> Unit,
) {
    when (signupUiState) {
        SignupUiState.Success -> {
            ResponseDialog(
                dialogData = DialogDataType.SUCCESS.dialogData,
                onClick = onSuccessDialogClose
            )
        }
        is SignupUiState.Failed -> {
            println("ShowSignupDialog SignupUiState.Failed: ${signupUiState.exception}")
            val dialogDataType = when (signupUiState.exception) {
                SignupThrowable.FailedTryLaterException -> {
                    DialogDataType.FAILED_SERVER_ERROR_TRY_LATER
                }
                SignupThrowable.WrongCaptchaException -> {
                    DialogDataType.FAILED_WRONG_CAPTCHA
                }
                SignupThrowable.WrongReferenceException -> {
                    DialogDataType.FAILED_WRONG_REFERENCE
                }
                SignupThrowable.WrongEmailException -> {
                    DialogDataType.FAILED_WRONG_EMAIL
                }
                SignupThrowable.ExistingUsernameException -> {
                    DialogDataType.FAILED_EXISTING_USERNAME
                }
                else -> {
                    DialogDataType.FAILED
                }
            }
            ResponseDialog(
                dialogData = dialogDataType.dialogData,
                onClick = onFailDialogClose
            )
        }
        else -> { /* No op */
        }
    }
}
