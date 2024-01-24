package earth.core.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow


@Composable
fun TextDisplaySmall(@StringRes textId: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(textId),
        style = MaterialTheme.typography.displaySmall,
        modifier = modifier
    )
}

@Composable
fun TextTitleLarge(
    @StringRes textId: Int,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = stringResource(textId),
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier,
        textAlign = TextAlign.Center,
        fontWeight = fontWeight
    )
}

@Composable
fun TextTitleMedium(
    @StringRes textId: Int,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.SemiBold
) {
    Text(
        text = stringResource(textId),
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier,
        textAlign = TextAlign.Center,
        fontWeight = fontWeight
    )
}

@Composable
fun TextWithEmphasise(
    modifier: Modifier = Modifier,
    @StringRes textId: Int? = null,
    text: String? = null,
    style: TextStyle = MaterialTheme.typography.titleSmall,
    textAlign: TextAlign = TextAlign.Center,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    alpha: Float = mediumDimAlpha,
) {
    val textValue = when {
        textId != null -> stringResource(textId)
        text != null -> text
        else -> ""
    }
    CompositionLocalProvider(
        LocalContentColor provides LocalContentColor.current.copy(alpha = alpha)
    ) {
        Text(
            text = textValue,
            style = style,
            modifier = modifier,
            textAlign = textAlign,
            maxLines = maxLines,
            overflow = overflow,
        )
    }
}

@Composable
fun TextFieldDescription(description: Int? = null) {
    description?.let {
        CompositionLocalProvider(
            LocalContentColor provides LocalContentColor.current.copy(alpha = mediumDimAlpha)
        ) {
            Text(
                text = stringResource(it),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SettingsDialogSectionTitle(@StringRes textId: Int) {
    Text(
        text = stringResource(id = textId),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(mediumDp)
            .fillMaxWidth()
    )
}