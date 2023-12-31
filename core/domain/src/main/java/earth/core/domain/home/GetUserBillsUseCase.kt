package earth.core.domain.home

import earth.core.data.UserRepository
import earth.core.database.User
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetUserBillsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    
    operator fun invoke(): Flow<List<User>> =
        userRepository.getUsers()
    
}