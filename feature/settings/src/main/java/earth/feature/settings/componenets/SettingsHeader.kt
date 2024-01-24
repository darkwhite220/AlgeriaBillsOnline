package earth.feature.settings.componenets

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import earth.core.designsystem.components.MyHeightSpacer
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.icon.AppIcons
import earth.feature.settings.R


@Composable
fun SettingsHeader() {
    val shape = remember { RoundedCornerShape(largeDp) }
    Image(
        modifier = Modifier
            .size(90.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = .2f),
                shape = shape
            )
            .clip(shape),
        painter = painterResource(AppIcons.AppIcon),
        contentDescription = null
    )
    MyHeightSpacer()
    Text(text = stringResource(R.string.app_version))
}
