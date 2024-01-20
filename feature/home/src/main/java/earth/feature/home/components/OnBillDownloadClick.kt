package earth.feature.home.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat


@Composable
fun OnBillDownloadClick(
    askPermission: ManagedActivityResultLauncher<String, Boolean>,
    onClick: () -> Unit
) {
    if (permissionCheck(askPermission, LocalContext.current))
        onClick()
}

private fun permissionCheck(
    askPermission: ManagedActivityResultLauncher<String, Boolean>,
    context: Context
): Boolean {
    return if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) || hasPermission(context)) {
//        Log.d(TAG, "onCreate: HAS PERMISSIONS")
        true
    } else {
//        Log.d(TAG, "onCreate: REQUEST PERMISSION")
        askPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        false
    }
}

private fun hasPermission(context: Context): Boolean =
    ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
        PackageManager.PERMISSION_GRANTED
