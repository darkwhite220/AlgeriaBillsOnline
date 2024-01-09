package earth.core.domain.home

import earth.core.data.SyncDataRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class SyncDataUseCase @Inject constructor(
    private val syncDataRepository: SyncDataRepository
) {
    
    operator fun invoke(): Flow<Boolean> = flow {
        val result = withContext(Dispatchers.IO) {
            syncDataRepository.syncData()
        }
        emit(result)
    }
    
}