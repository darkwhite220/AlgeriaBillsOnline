package earth.core.data

import earth.core.networkmodel.SignInCredentials

interface SignInRepository {
    
    suspend fun getSignInStatus(signInCredentials: SignInCredentials): Boolean
    
}