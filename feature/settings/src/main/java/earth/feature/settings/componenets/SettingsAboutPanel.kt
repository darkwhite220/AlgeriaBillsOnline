package earth.feature.settings.componenets

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import earth.core.designsystem.utils.Util
import earth.core.designsystem.components.MyDivider
import earth.core.designsystem.components.customContentSizeAnimation
import earth.core.designsystem.icon.AppIcons
import earth.feature.settings.R


@Composable
fun SettingsAboutPanel() {
    val context = LocalContext.current
    Card(
        modifier = Modifier.customContentSizeAnimation(),
        shape = MaterialTheme.shapes.large
    ) {
        Column {
            // ShareApp
            SettingsDialogRow(
                startingIcon = AppIcons.ShareApp,
                endIcon = AppIcons.ArrowRight,
                title = R.string.share_app,
                onClick = { Util.shareApp(context) }
            )
            MyDivider()
            // RateApp
            SettingsDialogRow(
                startingIcon = AppIcons.RateApp,
                endIcon = AppIcons.ArrowRight,
                title = R.string.rate_us,
                onClick = { Util.rateApp(context) }
            )
            MyDivider()
            // ContactUs
            SettingsDialogRow(
                startingIcon = AppIcons.ContactUs,
                endIcon = AppIcons.ArrowRight,
                title = R.string.contact_support,
                onClick = { Util.sendEmail(context, titleId = R.string.contact_support) }
            )
        }
    }
}
