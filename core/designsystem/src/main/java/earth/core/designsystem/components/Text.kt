package earth.core.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource


@Composable
fun TextTitleLarge(@StringRes textId: Int) {
    Text(
        text = stringResource(textId),
        style = MaterialTheme.typography.titleLarge
    )
}