package earth.core.designsystem.components.textfield

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import earth.core.designsystem.components.MyWidthSpacer
import earth.core.designsystem.components.iconSize
import earth.core.designsystem.components.lowDimAlpha
import earth.core.designsystem.components.mediumDimAlpha
import earth.core.designsystem.components.mediumDp
import earth.core.designsystem.icon.AppIcons


@Composable
fun UserInput(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = { },
    caption: String? = null,
    @DrawableRes vectorImageId: Int? = null,
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

@Composable
fun EditableUserInput(
    modifier: Modifier = Modifier,
    text: String = "",
    hint: String = "",
    caption: String? = null,
    @DrawableRes vectorImageId: Int? = null,
    focusRequester: FocusRequester = FocusRequester(),
    imeAction: ImeAction = ImeAction.Done,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = imeAction
    ),
    onInputChanged: (String) -> Unit = {},
    onImeAction: () -> Unit = {},
    tint: Color = LocalContentColor.current
) {
    BaseUserInput(
        modifier = modifier,
        caption = caption,
        tintIcon = {
            text.isNotEmpty()
        },
        showCaption = {
            text.isNotEmpty()
        },
        vectorImageId = vectorImageId,
    ) {
        BasicTextField(
            modifier = Modifier.focusRequester(focusRequester),
            value = text,
            onValueChange = onInputChanged,
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions { onImeAction() },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = tint),
            cursorBrush = SolidColor(tint),
            decorationBox = { innerTextField ->
                if (hint.isNotEmpty() && text.isEmpty()) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
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
        Row(modifier = Modifier.padding(all = 12.dp)) {
            // Remove resizing in Ui when no icon & first text character added
            Box(modifier = Modifier.height(iconSize)) {
                if (vectorImageId != null) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        painter = painterResource(id = vectorImageId),
                        tint = if (tintIcon()) tint else tint.copy(alpha = lowDimAlpha),
                        contentDescription = null
                    )
                    MyWidthSpacer(iconSize + mediumDp)
                }
            }
            if (caption != null && showCaption()) {
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "$caption: ",
                    style = MaterialTheme.typography.bodyMedium.copy(color = tint),
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
