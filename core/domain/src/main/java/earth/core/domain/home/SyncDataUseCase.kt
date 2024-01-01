package earth.core.domain.home

import earth.core.data.SyncDataRepository
import earth.core.database.User
import javax.inject.Inject

class SyncDataUseCase @Inject constructor(
    private val syncDataRepository: SyncDataRepository
) {
    suspend operator fun invoke(referenceList: List<User>) {
        syncDataRepository.syncData(referenceList)
    }
}