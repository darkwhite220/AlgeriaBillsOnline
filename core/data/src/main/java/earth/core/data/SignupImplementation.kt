package earth.core.data

import earth.core.network.AppNetworkDataSource
import earth.core.networkmodel.SignupCaptcha
import earth.core.networkmodel.SignupRequestBody
import earth.core.networkmodel.SignupResponse
import javax.inject.Inject


class SignupImplementation @Inject constructor(
    private val appNetwork: AppNetworkDataSource
) : SignupRepository {
    
    override suspend fun getSignupCaptcha(): SignupCaptcha {
        return appNetwork.fetchSignupCaptcha()
    }
    
    override suspend fun getSignupStatus(signupRequestBody: SignupRequestBody): SignupResponse {
        return appNetwork.requestSignup(signupRequestBody)
    }
}