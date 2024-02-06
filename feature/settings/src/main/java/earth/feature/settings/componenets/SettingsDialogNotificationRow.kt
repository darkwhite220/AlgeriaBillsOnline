package earth.feature.settings.componenets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import earth.core.designsystem.components.horizontalSpacedBy
import earth.core.designsystem.components.mediumDp
import earth.core.designsystem.icon.AppIcons
import earth.feature.settings.R


@Composable
fun SettingsDialogNotificationRow(
    checkStatus: () -> Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalSpacedBy(10.dp),
        modifier = Modifier.padding(start = 10.dp, top = 2.dp, end = 0.dp, bottom = 2.dp)
    ) {
        Icon(
            painter = painterResource(AppIcons.Notification),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.app_notifications),
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checkStatus(),
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(end = mediumDp)
        )
    }
}