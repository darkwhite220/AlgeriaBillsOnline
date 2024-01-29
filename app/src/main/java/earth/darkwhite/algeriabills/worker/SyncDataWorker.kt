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
import java.net.UnknownHostException
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
        val isNotificationEnabled = userDataImplementation.isNotificationEnabled.first()
        val notificationUtil = NotificationUtil(
            context = appContext,
            isNotificationEnabled = isNotificationEnabled
        )
        notificationUtil.createNotificationChannel()
        
        var attempts = 1
        while (attempts <= MAX_RETRY_ATTEMPTS) {
            try {
                
                val result = syncDataRepository.syncData()
                if (result == null) {
                    attempts++
                    Result.Retry()
                } else {
                    if (result) {
                        notificationUtil.showNotification(isSuccessful = true)
                    }
                    return Result.success()
                }
                
            } catch (e: Throwable) {
                
                println("Throwable: $e")
                if (e is UnknownHostException) {
                    return Result.Failure()
                }
                if (e is SignInThrowable || e is ConvertingPdfThrowable || attempts == MAX_RETRY_ATTEMPTS) {
                    notificationUtil.showNotification(isSuccessful = false)
                    return Result.Failure()
                }
                attempts++
                Result.Retry()
                
            } catch (e: Exception) {
                
                println("Exception: $e")
                if (attempts == MAX_RETRY_ATTEMPTS) {
                    notificationUtil.showNotification(isSuccessful = false)
                    return Result.Failure()
                }
                attempts++
                Result.Retry()
                
            }
        }
        
        return Result.success()
    }
    
    companion object {
        private const val MAX_RETRY_ATTEMPTS = 3
    }
}