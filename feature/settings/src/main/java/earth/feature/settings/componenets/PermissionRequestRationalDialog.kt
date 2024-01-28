package earth.feature.settings.componenets

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import earth.core.designsystem.components.TextTitleLarge
import earth.core.designsystem.components.mediumDp
import earth.feature.settings.R

@Preview
@Composable
fun PermissionRequestRationalDialog(
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onClick,
        title = {
            TextTitleLarge(
                textId = R.string.permission_denied_title,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                Divider()
                Text(
                    text = stringResource(R.string.enable_notification_from_settings),
                    modifier = Modifier.padding(mediumDp)
                )
                Divider()
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.confirm),
                modifier = Modifier.clickable {
                    navigateToAppNotification(context)
                    onClick()
                }
            )
        },
        dismissButton = {
            Text(
                text = stringResource(R.string.dismiss),
                modifier = Modifier.clickable { onClick() }
            )
        }
    )
}

private fun navigateToAppNotification(context: Context) {
    val intent = Intent()
    intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    
    intent.putExtra("app_package", context.packageName)
    intent.putExtra("app_uid", context.applicationInfo.uid)
    intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
    
    context.startActivity(intent)
}
