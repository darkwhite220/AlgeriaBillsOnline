package earth.darkwhite.algeriabills.worker

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import earth.core.designsystem.icon.AppIcons
import earth.darkwhite.algeriabills.MainActivity
import earth.darkwhite.algeriabills.R

class NotificationUtil(
    private val context: Context,
    private val isNotificationEnabled: Boolean
) {
    
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                context.getString(R.string.sync_bills),
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = context.getString(R.string.sync_new_bills)
            }
            
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    @SuppressLint("MissingPermission")
    fun showNotification(isSuccessful: Boolean) {
        if (notificationEnabled()) {
            val builder = if (isSuccessful) {
                notification(R.string.sync_successful_new_bill_found)
            } else {
                notification(R.string.sync_failed_contact_support)
            }
            with(NotificationManagerCompat.from(context)) {
                notify(NOTIFICATION_ID, builder.build())
            }
        }
    }
    
    private fun notificationEnabled(): Boolean {
        val isDeviceNotificationEnabled =
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            isNotificationEnabled && isDeviceNotificationEnabled &&
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
        } else {
            isNotificationEnabled && isDeviceNotificationEnabled
        }
    }
    
    private fun notification(@StringRes body: Int): NotificationCompat.Builder {
        val intent: Intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE)
        
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
            .setSmallIcon(AppIcons.NotificationIcon)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(body))
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SOCIAL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }
    
    companion object {
        const val TAG = "MessagingService"
        const val NOTIFICATION_CHANNEL = "9870123"
        const val REQUEST_CODE = 2220
        const val NOTIFICATION_ID = 20011
    }
}