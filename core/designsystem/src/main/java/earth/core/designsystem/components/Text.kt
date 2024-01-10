package earth.core.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign


@Composable
fun TextTitleLarge(@StringRes textId: Int) {
    Text(
        text = stringResource(textId),
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun TextDescription(@StringRes textId: Int) {
    CompositionLocalProvider(
        LocalContentColor provides LocalContentColor.current.copy(alpha = .74f)
    ) {
        Text(
            text = stringResource(textId),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TextFieldDescription(description: String? = null) {
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