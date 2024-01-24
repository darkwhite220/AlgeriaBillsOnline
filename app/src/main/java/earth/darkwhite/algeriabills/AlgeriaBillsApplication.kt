package earth.darkwhite.algeriabills

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dagger.hilt.android.HiltAndroidApp
import earth.core.data.SyncDataRepository
import earth.core.data.UserDataImplementation
import earth.darkwhite.algeriabills.worker.SyncDataWorker
import javax.inject.Inject

@HiltAndroidApp
class AlgeriaBillsApplication : Application(), Configuration.Provider {
    
    @Inject
    lateinit var customWorkerFactory: CustomWorkerFactory
    
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(customWorkerFactory)
            .build()
    
}

class CustomWorkerFactory @Inject constructor(
    private val syncDataRepository: SyncDataRepository,
    private val userDataImplementation: UserDataImplementation,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = SyncDataWorker(
        syncDataRepository = syncDataRepository,
        userDataImplementation = userDataImplementation,
        appContext = appContext,
        params = workerParameters
    )
}