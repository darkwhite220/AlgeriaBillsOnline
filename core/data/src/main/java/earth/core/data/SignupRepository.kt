package earth.core.data

import earth.core.networkmodel.SignupCaptcha
import earth.core.networkmodel.SignupRequestBody
import earth.core.networkmodel.SignupResponse

interface SignupRepository {
    
    suspend fun getSignupCaptcha(): SignupCaptcha
    
    suspend fun getSignupStatus(signupRequestBody: SignupRequestBody): SignupResponse
    
}