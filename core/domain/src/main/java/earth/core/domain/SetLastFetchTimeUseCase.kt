package earth.core.domain

import earth.core.data.UserDataRepository
import javax.inject.Inject

class SetLastFetchTimeUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository
) {
    
    suspend operator fun invoke(newTime: Long) {
        userDataRepository.setLastFetchTime(newTime)
    }
    
}