package earth.core.network

import earth.core.networkmodel.BillResponse
import earth.core.networkmodel.SignInResponse
import earth.core.networkmodel.SignupCaptcha
import earth.core.networkmodel.SignupRequestBody
import earth.core.networkmodel.SignupResponse

interface AppNetworkDataSource {
    
    suspend fun fetchSignupCaptcha(): SignupCaptcha
    
    suspend fun requestSignup(signupRequestBody: SignupRequestBody): SignupResponse
    
    suspend fun signIn(username: String, password: String): SignInResponse
    
    suspend fun fetchBill(urlEndpoint: String): BillResponse
    
    suspend fun logOut()
}