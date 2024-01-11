package earth.core.domain.signin

import earth.core.data.SignInRepository
import earth.core.networkmodel.SignInCredentials
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class SignInUseCase @Inject constructor(
    private val signInRepository: SignInRepository,
) {
    
    operator fun invoke(signInCredentials: SignInCredentials): Flow<Boolean> = flow {
        val response = withContext(Dispatchers.IO) {
            signInRepository.getSignInStatus(signInCredentials)
        }
        emit(response)
    }
    
}