package earth.darkwhite.feature.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import earth.core.designsystem.components.ButtonWithLoading
import earth.core.designsystem.components.MyWidthSpacer
import earth.core.designsystem.components.TextDescription
import earth.core.designsystem.components.TextTitleLarge
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.textfield.MyTextField
import earth.core.designsystem.components.textfield.SignInTextFieldTypes
import earth.core.designsystem.components.textfield.TextFieldEvent
import earth.core.designsystem.components.textfield.signInTextFieldMap
import earth.core.designsystem.components.topappbar.CenteredTopAppBar
import earth.core.designsystem.components.verticalSpacedBy
import earth.darkwhite.feature.signin.SignInEvent.OnSignInClick
import earth.darkwhite.feature.signin.uistate.SignInFormState
import earth.feature.signin.R

@Composable
fun SignInRoute(
    onBackClick: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val signInFormState by viewModel.signInFormState.collectAsStateWithLifecycle()
    
    SignInScreen(
        signInFormState = signInFormState,
        onSignInEvent = viewModel::onEvent,
        onBackClick = onBackClick,
    )
}

@Composable
fun SignInScreen(
    signInFormState: SignInFormState = SignInFormState(),
    onSignInEvent: (SignInEvent) -> Unit,
    onBackClick: () -> Unit,
) {
    Column(
        modifier = Modifier
    ) {
        CenteredTopAppBar(
            titleId = R.string.sign_in,
            onBackClick = onBackClick,
        )
        
        Row(modifier = Modifier.verticalScroll(rememberScrollState())) {
            MyWidthSpacer(largeDp * 2)
            Column(
                modifier = Modifier
//                .imePadding() // TODO CHECK IME PADDING
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = verticalSpacedBy(largeDp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val focusManager = LocalFocusManager.current
                
                TextTitleLarge(textId = R.string.sign_in_to_continue)
                TextDescription(textId = R.string.sign_in_desc)
                
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
                                newValue = newValue,
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
                    isLoading = false,//signupUiState == SignupUiState.Loading,
                    onClick = { onSignInEvent(OnSignInClick) },
                )
                
            }
            MyWidthSpacer(largeDp)
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