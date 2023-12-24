package com.darkwhite.feature.createaccount.dialog

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import earth.feature.createaccount.R

data class DialogData(
    val icon: String,
    @StringRes val titleId: Int,
    @StringRes val textId: Int,
    @StringRes val buttonTextId: Int,
)

enum class DialogDataType(
    val dialogData: DialogData
) {
    SUCCESS(
        dialogData = DialogData(
            icon = "",
            titleId = R.string.account_creation_successful,
            textId = R.string.create_account, // TODO
            buttonTextId = R.string.continue_button,
        )
    ),
    FAILED(
        dialogData = DialogData(
            icon = "",
            titleId = R.string.account_creation_failed,
            textId = R.string.create_account, // TODO
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
//    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("lottie/$animationName.json"))
    // TODO add lottie
    
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
//            LottieAnimation(
//                modifier = Modifier.size(180.dp),
//                composition = composition,
//                iterations = 3,
//                reverseOnRepeat = true
//            )
            },
            title = {
                Text(
                    text = stringResource(id = titleId),
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    text = stringResource(id = textId),
                    textAlign = TextAlign.Center
                )
            }
        )
    }
}

@Preview
@Composable
fun ResponseDialogSuccessfulPreview(modifier: Modifier = Modifier) {
    MaterialTheme {
        ResponseDialog(dialogData = DialogDataType.SUCCESS.dialogData, onClick = {})
    }
}

@Preview
@Composable
fun ResponseDialogFailedPreview(modifier: Modifier = Modifier) {
    MaterialTheme {
        ResponseDialog(dialogData = DialogDataType.FAILED.dialogData, onClick = {})
    }
}