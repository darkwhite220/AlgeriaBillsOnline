package earth.core.data

import earth.core.data.util.SignInUtil
import earth.core.database.User
import earth.core.network.AppNetwork
import earth.core.networkmodel.SignInCredentials
import javax.inject.Inject

class SignInImplementation @Inject constructor(
    private val network: AppNetwork,
    private val userRepository: UserRepository,
) : SignInRepository {
    
    override suspend fun getSignInStatus(signInCredentials: SignInCredentials): Boolean {
        val signInResponse = SignInUtil.extractSignInPageData(
            network.signIn(
                username = signInCredentials.username,
                password = signInCredentials.password
            )
        )
        
        val user: User? = userRepository.getUser(signInResponse.reference)
        
        if (user == null) {
            userRepository.insertUser(
                User(
                    reference = signInResponse.reference,
                    username = signInCredentials.username,
                    password = signInCredentials.password,
                )
            )
        } else {
            userRepository.insertUser(
                user.copy(
                    username = signInCredentials.username,
                    password = signInCredentials.password,
                )
            )
        }
        
        return user == null
    }
}