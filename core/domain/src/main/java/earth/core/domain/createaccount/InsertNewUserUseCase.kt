package earth.core.domain.createaccount

import earth.core.data.UserRepository
import earth.core.database.User
import javax.inject.Inject

class InsertNewUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    
    suspend operator fun invoke(user: User) {
        userRepository.insertUser(user)
    }
}