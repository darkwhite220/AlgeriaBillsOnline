package earth.darkwhite.feature.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import earth.core.designsystem.components.ButtonWithLoading
import earth.core.designsystem.components.TextDisplaySmall
import earth.core.designsystem.components.TextWithEmphasise
import earth.core.designsystem.components.dialog.ResponseDialog
import earth.core.designsystem.components.dialog.SignInResponseDialogDataType
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.textfield.MyTextField
import earth.core.designsystem.components.textfield.SignInTextFieldTypes
import earth.core.designsystem.components.textfield.TextFieldEvent
import earth.core.designsystem.components.textfield.signInTextFieldMap
import earth.core.designsystem.components.topappbar.CenteredTopAppBar
import earth.core.designsystem.components.verticalSpacedBy
import earth.core.designsystem.utils.Util
import earth.core.designsystem.utils.Util.sendEmail
import earth.core.throwablemodel.ConvertingPdfThrowable
import earth.core.throwablemodel.SignInThrowable
import earth.darkwhite.feature.signin.SignInEvent.OnSignInClick
import earth.darkwhite.feature.signin.uistate.SignInFormState
import earth.darkwhite.feature.signin.uistate.SignInUiState
import earth.feature.signin.R
import java.net.UnknownHostException

@Composable
fun SignInRoute(
    onBackClick: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val signInFormState by viewModel.signInFormState.collectAsStateWithLifecycle()
    val signInUiState by viewModel.signInUiState.collectAsStateWithLifecycle()
    
    SignInScreen(
        signInFormState = signInFormState,
        signInUiState = signInUiState,
        isPreviousFail = viewModel.isPreviousFail,
        onSignInEvent = viewModel::onEvent,
        onBackClick = onBackClick,
    )
    
    // SignIn Dialog
    ShowSignInDialog(
        signInUiState = signInUiState,
        onSuccessDialogClose = onBackClick,
        onFailDialogClose = viewModel::onFailedDialogClose
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignInScreen(
    signInFormState: SignInFormState = SignInFormState(),
    signInUiState: SignInUiState = SignInUiState.InitialState,
    isPreviousFail: Boolean = false,
    onSignInEvent: (SignInEvent) -> Unit,
    onBackClick: () -> Unit,
) {
    Column {
        CenteredTopAppBar(
            titleId = null,
            onBackClick = onBackClick,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = largeDp),
            verticalArrangement = verticalSpacedBy(largeDp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val focusManager = LocalFocusManager.current
            
            TextDisplaySmall(
                textId = R.string.sign_in,
                modifier = Modifier.padding(vertical = largeDp)
            )
            TextWithEmphasise(
                modifier = Modifier.fillMaxWidth(),
                textId = R.string.sign_in_desc
            )
            
            signInTextFieldMap.entries.forEachIndexed { index, item ->
                val focusRequester = remember { FocusRequester() }
                LaunchedEffect(Unit) {
                    if (index == 0) focusRequester.requestFocus()
                }
                
                MyTextField(
                    focusRequester = focusRequester,
                    fieldType = item.key.name,
                    fieldValues = item.value,
                    value = signInFormState.currentValue(item.key),
                    isValid = signInFormState.currentIsValid(item.key),
                    enabled = signInFormState.enabled,
                    onValueChange = { newValue ->
                        onFormEventValueChange(
                            newValue = newValue.trim(),
                            signInTextFieldTypes = item.key,
                            onSignInEvent = onSignInEvent,
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
                                onSignInEvent(OnSignInClick)
                            }
                            else -> {}
                        }
                    },
                )
            }
            
            ButtonWithLoading(
                textId = R.string.sign_in,
                isEnabled = !isPreviousFail && signInUiState != SignInUiState.Loading,
                isLoading = signInUiState == SignInUiState.Loading,
                onClick = { onSignInEvent(OnSignInClick) },
            )
            
        }
    }
}

private fun onFormEventValueChange(
    newValue: String,
    signInTextFieldTypes: SignInTextFieldTypes,
    onSignInEvent: (SignInEvent) -> Unit,
) {
    onSignInEvent(
        when (signInTextFieldTypes) {
            SignInTextFieldTypes.USERNAME -> {
                SignInEvent.OnUserNameValueChange(newValue)
            }
            SignInTextFieldTypes.PASSWORD -> {
                SignInEvent.OnPasswordValueChange(newValue)
            }
        }
    )
}

@Composable
private fun ShowSignInDialog(
    signInUiState: SignInUiState,
    onSuccessDialogClose: () -> Unit,
    onFailDialogClose: () -> Unit,
) {
    when (signInUiState) {
        is SignInUiState.Success -> {
            val dialogDataType = if (signInUiState.data) {
                SignInResponseDialogDataType.SUCCESS
            } else {
                SignInResponseDialogDataType.SUCCESS_CREDENTIAL_UPDATE
            }
            ResponseDialog(
                dialogData = dialogDataType.dialogData,
                onDismissClick = onSuccessDialogClose,
            )
        }
        is SignInUiState.Failed -> {
            println("ShowSignInDialog SignInUiState.Failed: ${signInUiState.throwable}")
            val context = LocalContext.current
            if (signInUiState.throwable is UnknownHostException ||
                signInUiState.throwable is SignInThrowable.ServerOffline
            ) {
                return
            }
            var supportMessage: String? = null
            val dialogDataType = when (signInUiState.throwable) {
                SignInThrowable.BadUsername -> {
                    SignInResponseDialogDataType.FAILED_WRONG_USERNAME
                }
                SignInThrowable.BadPassword -> {
                    SignInResponseDialogDataType.FAILED_WRONG_PASSWORD
                }
                SignInThrowable.TemporarilyLockedAccount -> {
                    SignInResponseDialogDataType.TEMPORARILY_LOCKED_ACCOUNT
                }
                else -> {
                    if (signInUiState.throwable is ConvertingPdfThrowable.UnhandledSignInResponse) {
                        supportMessage = signInUiState.throwable.message
                    }
                    SignInResponseDialogDataType.FAILED
                }
            }
            ResponseDialog(
                dialogData = dialogDataType.dialogData,
                supportMessage = supportMessage,
                onDismissClick = onFailDialogClose,
                onContactSupportClick = {
                    sendEmail(
                        context = context,
                        titleId = R.string.sign_in_failed_error,
                        message = supportMessage
                    )
                    onFailDialogClose()
                },
            )
        }
        else -> { /* No op */
        }
    }
}
