package com.darkwhite.feature.createaccount.dialog

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import earth.core.designsystem.components.MyLottieAnimation
import earth.core.designsystem.components.lottieAnimationSize
import earth.feature.createaccount.R

const val SUCCESSFUL_LOTTIE_FILE = "lottie/successful.json"
const val FAILED_LOTTIE_FILE = "lottie/failed.json"

data class DialogData(
    val lottieFileName: String,
    @StringRes val titleId: Int,
    @StringRes val textId: Int?,
    @StringRes val buttonTextId: Int,
)

enum class DialogDataType(
    val dialogData: DialogData
) {
    SUCCESS(
        dialogData = DialogData(
            lottieFileName = SUCCESSFUL_LOTTIE_FILE,
            titleId = R.string.account_creation_successful,
            textId = null,
            buttonTextId = R.string.continue_button,
        )
    ),
    FAILED_SERVER_ERROR_TRY_LATER(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.account_creation_failed,
            textId = R.string.account_creation_failed_try_again_later,
            buttonTextId = R.string.dismiss,
        )
    ),
    FAILED_WRONG_CAPTCHA(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.account_creation_failed,
            textId = R.string.account_creation_failed_wrong_captcha,
            buttonTextId = R.string.dismiss,
        )
    ),
    FAILED_WRONG_REFERENCE(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.account_creation_failed,
            textId = R.string.account_creation_failed_wrong_reference,
            buttonTextId = R.string.dismiss,
        )
    ),
    FAILED_WRONG_EMAIL(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.account_creation_failed,
            textId = R.string.account_creation_failed_wrong_email,
            buttonTextId = R.string.dismiss,
        )
    ),
    FAILED_EXISTING_USERNAME(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.account_creation_failed,
            textId = R.string.account_creation_failed_existing_username,
            buttonTextId = R.string.dismiss,
        )
    ),
    FAILED(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.account_creation_failed,
            textId = R.string.account_creation_failed_desc,
            buttonTextId = R.string.dismiss,
        )
    ),
}

@Composable
fun ResponseDialog(
    modifier: Modifier = Modifier,
    dialogData: DialogData,
    onClick: () -> Unit
) {
    dialogData.apply {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = onClick,
            confirmButton = {
                Button(onClick = onClick) {
                    Text(text = stringResource(id = buttonTextId))
                }
            },
            icon = {
                MyLottieAnimation(
                    modifier = Modifier.size(lottieAnimationSize),
                    fileName = lottieFileName
                )
            },
            title = { DialogText(textId = titleId) },
            text = { DialogText(textId = textId) }
        )
    }
}

@Composable
private fun DialogText(@StringRes textId: Int?) {
    textId?.let {
        Text(
            text = stringResource(id = it),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun ResponseDialogSuccessfulPreview(modifier: Modifier = Modifier) {
    MaterialTheme {
        ResponseDialog(
            modifier = modifier,
            dialogData = DialogDataType.SUCCESS.dialogData,
            onClick = {})
    }
}

@Preview
@Composable
private fun ResponseDialogFailedPreview(modifier: Modifier = Modifier) {
    MaterialTheme {
        ResponseDialog(
            modifier = modifier,
            dialogData = DialogDataType.FAILED.dialogData,
            onClick = {})
    }
}