package earth.darkwhite.algeriabills.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import earth.core.data.SyncDataRepository
import earth.core.data.UserDataImplementation
import earth.core.throwablemodel.ConvertingPdfThrowable
import earth.core.throwablemodel.SignInThrowable
import kotlinx.coroutines.flow.first

@HiltWorker
class SyncDataWorker @AssistedInject constructor(
    @Assisted private val syncDataRepository: SyncDataRepository,
    @Assisted private val userDataImplementation: UserDataImplementation,
    @Assisted private val appContext: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    
    @SuppressLint("RestrictedApi")
    override suspend fun doWork(): Result {
        println("SyncDataWorker doWork")
        val notificationUtil = NotificationUtil(
            context = appContext,
            isNotificationEnabled = userDataImplementation.isNotificationEnabled.first()
        )
        notificationUtil.createNotificationChannel()
        
        try {
            
            val result = syncDataRepository.syncData()
            if (result != null && result) {
                notificationUtil.showNotification(isSuccessful = true)
            }
            return Result.success()
            
        } catch (e: Throwable) {
            
            println("Throwable: $e")
            if (e is SignInThrowable || e is ConvertingPdfThrowable) {
                notificationUtil.showNotification(isSuccessful = false)
            }
            return Result.Failure()
            
            
        } catch (e: Exception) {
            
            println("Exception: $e")
            notificationUtil.showNotification(isSuccessful = false)
            return Result.Failure()
            
        }
    }
    
}