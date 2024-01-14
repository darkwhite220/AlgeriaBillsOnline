package earth.core.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import earth.core.designsystem.R

@Preview
@Composable
fun ButtonWithLoading(
    @StringRes textId: Int = R.string.password_visibility_icon,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(.8f),
        enabled = isEnabled
    ) {
        Text(
            text = stringResource(id = textId)
        )
        
        Spacer(modifier = Modifier.width(largeDp))
        
        if (isLoading) {
            MyCircularProgressBar()
        }
    }
}
