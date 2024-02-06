package earth.feature.settings.componenets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import earth.core.designsystem.components.horizontalSpacedBy
import earth.core.designsystem.icon.AppIcons


@Composable
fun SettingsDialogRow(
    @DrawableRes startingIcon: Int,
    @DrawableRes endIcon: Int = AppIcons.ArrowDown,
    @StringRes title: Int,
    @StringRes selected: Int = 0,
    angle: () -> Float = { 0f },
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalSpacedBy(10.dp),
        modifier = Modifier
            .clickable { onClick() }
            .padding(start = 10.dp, top = 2.dp, end = 0.dp, bottom = 2.dp)
    ) {
        Icon(
            painter = painterResource(startingIcon),
            contentDescription = null
        )
        Text(
            text = stringResource(title),
            modifier = Modifier.weight(1f)
        )
        if (selected != 0) {
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = stringResource(selected)
            )
        }
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(endIcon),
                contentDescription = null,
                modifier = Modifier.graphicsLayer { rotationZ = angle() }
            )
        }
    }
}
