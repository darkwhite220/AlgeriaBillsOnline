package earth.core.designsystem.components.textfield

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import earth.core.designsystem.components.horizontalSpacedBy
import earth.core.designsystem.components.lowDimAlpha
import earth.core.designsystem.components.mediumDimAlpha
import earth.core.designsystem.icon.AppIcons


@Preview(showBackground = true)
@Composable
fun UserInput(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = { },
    caption: String? = null,
    @DrawableRes vectorImageId: Int? = AppIcons.ConsumptionUp,
    tint: Color = LocalContentColor.current
) {
    BaseUserInput(
        modifier = modifier,
        onClick = onClick,
        caption = caption,
        vectorImageId = vectorImageId,
        tintIcon = { text.isNotEmpty() },
        tint = tint
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(color = tint)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditableUserInput(
    hint: String = "",
    caption: String? = null,
    @DrawableRes vectorImageId: Int? = AppIcons.ConsumptionUp,
    onInputChanged: (String) -> Unit = {},
    tint: Color = LocalContentColor.current
) {
    var textFieldState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue()
        )
    }
    BaseUserInput(
        caption = caption,
        tintIcon = {
            textFieldState.text.isNotEmpty()
        },
        showCaption = {
            textFieldState.text.isNotEmpty()
        },
        vectorImageId = vectorImageId
    ) {
        BasicTextField(
            value = textFieldState,
            onValueChange = {
                textFieldState = it
                onInputChanged(textFieldState.text)
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            cursorBrush = SolidColor(tint),
            decorationBox = { innerTextField ->
                if (hint.isNotEmpty() && textFieldState.text.isEmpty()) {
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = tint.copy(alpha = mediumDimAlpha)
                        )
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
private fun BaseUserInput(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    caption: String? = null,
    @DrawableRes vectorImageId: Int? = null,
    showCaption: () -> Boolean = { true },
    tintIcon: () -> Boolean,
    tint: Color = LocalContentColor.current,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            Modifier.padding(all = 12.dp),
            horizontalArrangement = horizontalSpacedBy()
        ) {
            if (vectorImageId != null) {
                Icon(
                    modifier = Modifier.size(24.dp, 24.dp),
                    painter = painterResource(id = vectorImageId),
                    tint = if (tintIcon()) tint else tint.copy(alpha = lowDimAlpha),
                    contentDescription = null
                )
            }
            if (caption != null && showCaption()) {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = caption,
                )
            }
            Row(
                Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                content()
            }
        }
    }
}
