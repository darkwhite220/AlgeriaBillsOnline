package com.darkwhite.feature.createaccount

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.darkwhite.feature.createaccount.CreateAccountEvent.OnCreateAccountClick
import com.darkwhite.feature.createaccount.components.CaptchaUi
import com.darkwhite.feature.createaccount.dialog.ReferenceDetailDialog
import com.darkwhite.feature.createaccount.uistate.CaptchaUiState
import com.darkwhite.feature.createaccount.uistate.FormUiState
import com.darkwhite.feature.createaccount.uistate.SignupUiState
import earth.core.designsystem.components.ButtonWithLoading
import earth.core.designsystem.components.MyHeightSpacer
import earth.core.designsystem.components.TextTitleLarge
import earth.core.designsystem.components.TextWithEmphasise
import earth.core.designsystem.components.dialog.ResponseDialog
import earth.core.designsystem.components.dialog.SignUpResponseDialogDataType.FAILED
import earth.core.designsystem.components.dialog.SignUpResponseDialogDataType.FAILED_EXISTING_USERNAME
import earth.core.designsystem.components.dialog.SignUpResponseDialogDataType.FAILED_SERVER_ERROR_TRY_LATER
import earth.core.designsystem.components.dialog.SignUpResponseDialogDataType.FAILED_WRONG_CAPTCHA
import earth.core.designsystem.components.dialog.SignUpResponseDialogDataType.FAILED_WRONG_EMAIL
import earth.core.designsystem.components.dialog.SignUpResponseDialogDataType.FAILED_WRONG_REFERENCE
import earth.core.designsystem.components.dialog.SignUpResponseDialogDataType.SUCCESS
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.mediumDp
import earth.core.designsystem.components.textfield.CreateAccountTextFieldTypes
import earth.core.designsystem.components.textfield.MyTextField
import earth.core.designsystem.components.textfield.TextFieldEvent
import earth.core.designsystem.components.textfield.createAccountTextFieldMap
import earth.core.designsystem.components.topappbar.CenteredTopAppBar
import earth.core.designsystem.utils.Util
import earth.core.throwablemodel.SignInThrowable
import earth.core.throwablemodel.SignupThrowable
import earth.feature.createaccount.R
import java.net.UnknownHostException


@Composable
internal fun CreateAccountRoute(
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
    Column {
        CenteredTopAppBar(
            titleId = R.string.create_account,
            onBackClick = onBackClick,
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = largeDp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val focusManager = LocalFocusManager.current
            
            MyHeightSpacer(largeDp)
            TextTitleLarge(textId = R.string.register_to_see_your_sonalgaz_bills)
            MyHeightSpacer(largeDp)
            
            createAccountTextFieldMap.entries.forEachIndexed { index, item ->
                val focusRequester = remember { FocusRequester() }
                LaunchedEffect(Unit) {
                    if (index == 0) focusRequester.requestFocus()
                }
                
                CaptchaUi(
                    textFieldType = item.key,
                    captchaUiState = captchaUiState
                )
                
                MyTextField(
                    focusRequester = focusRequester,
                    fieldType = item.key.name,
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
                
                TextWithEmphasise(
                    textId = item.value.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                
                MyHeightSpacer(mediumDp)
            }
            
            ButtonWithLoading(
                textId = R.string.create_account,
                isEnabled = signupUiState != SignupUiState.Loading,
                isLoading = signupUiState == SignupUiState.Loading,
                onClick = { onCreateAccountEvent(OnCreateAccountClick) },
            )
            
            MyHeightSpacer(largeDp)
        }
    }
}

private fun onFormEventValueChange(
    newValue: String,
    textFieldTypes: CreateAccountTextFieldTypes,
    onCreateAccountEvent: (CreateAccountEvent) -> Unit,
) {
    onCreateAccountEvent(
        when (textFieldTypes) {
            CreateAccountTextFieldTypes.USERNAME -> {
                CreateAccountEvent.OnUserNameValueChange(newValue)
            }
            CreateAccountTextFieldTypes.EMAIL -> {
                CreateAccountEvent.OnEmailValueChange(newValue)
            }
            CreateAccountTextFieldTypes.REFERENCE -> {
                CreateAccountEvent.OnReferenceValueChange(newValue)
            }
            CreateAccountTextFieldTypes.PASSWORD -> {
                CreateAccountEvent.OnPasswordValueChange(newValue)
            }
            CreateAccountTextFieldTypes.REPEAT_PASSWORD -> {
                CreateAccountEvent.OnRepeatPasswordValueChange(newValue)
            }
            CreateAccountTextFieldTypes.CAPTCHA -> {
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
                dialogData = SUCCESS.dialogData,
                onDismissClick = onSuccessDialogClose
            )
        }
        is SignupUiState.Failed -> {
            println("ShowSignupDialog SignupUiState.Failed: ${signupUiState.throwable}")
            if (signupUiState.throwable is UnknownHostException) {
                Util.noInternetConnection(LocalContext.current)
                return
            }
            val signUpResponseDialogDataType = when (signupUiState.throwable) {
                SignupThrowable.FailedTryLaterException, SignupThrowable.ServerOffline ->
                    FAILED_SERVER_ERROR_TRY_LATER
                SignupThrowable.WrongCaptchaException -> FAILED_WRONG_CAPTCHA
                SignupThrowable.WrongReferenceException -> FAILED_WRONG_REFERENCE
                SignupThrowable.WrongEmailException -> FAILED_WRONG_EMAIL
                SignupThrowable.ExistingUsernameException -> FAILED_EXISTING_USERNAME
                else -> FAILED
                
            }
            ResponseDialog(
                dialogData = signUpResponseDialogDataType.dialogData,
                onDismissClick = onFailDialogClose,
            )
        }
        else -> { /* No op */
        }
    }
}
