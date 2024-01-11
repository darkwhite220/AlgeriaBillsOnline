package earth.core.designsystem.components.dialog

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


data class DialogData(
    val lottieFileName: String,
    @StringRes val titleId: Int,
    @StringRes val textId: Int?,
    @StringRes val buttonTextId: Int,
)

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
            dialogData = SignUpResponseDialogDataType.SUCCESS.dialogData,
            onClick = {})
    }
}

@Preview
@Composable
private fun ResponseDialogFailedPreview(modifier: Modifier = Modifier) {
    MaterialTheme {
        ResponseDialog(
            modifier = modifier,
            dialogData = SignUpResponseDialogDataType.FAILED.dialogData,
            onClick = {})
    }
}