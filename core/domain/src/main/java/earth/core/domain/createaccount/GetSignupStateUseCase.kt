package earth.core.domain.createaccount

import earth.core.data.SignupRepository
import earth.core.networkmodel.SignupRequestBody
import earth.core.networkmodel.SignupResponse
import earth.core.throwablemodel.SignupThrowable
import earth.core.throwablemodel.SignupThrowableConstants
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetSignupStateUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    
    operator fun invoke(signupRequestBody: SignupRequestBody): Flow<String> = flow {
        val response: SignupResponse = signupRepository.getSignupStatus(signupRequestBody)
        
        emit(
            when {
                response.body.contains(SUCCESSFUL_SIGNUP_MESSAGE) -> {
                    ""
                }
                response.body.contains(SignupThrowableConstants.FAILED_TRY_LATER) -> {
                    throw SignupThrowable.FailedTryLaterException
                }
                response.body.contains(SignupThrowableConstants.WRONG_CAPTCHA_FIRST) ||
                    response.body.contains(SignupThrowableConstants.WRONG_CAPTCHA_SECOND) -> {
                    throw SignupThrowable.WrongCaptchaException
                }
                response.body.contains(SignupThrowableConstants.REFERENCE_NONE_VALID) ||
                    response.body.contains(SignupThrowableConstants.REFERENCE_NONE_EXISTENT) -> {
                    throw SignupThrowable.WrongReferenceException
                }
                response.body.contains(SignupThrowableConstants.EXISTING_USERNAME_RESPONSE) -> {
                    throw SignupThrowable.ExistingUsernameException
                }
                response.responseCode == SignupThrowableConstants.REDIRECT_STATUS_CODE -> {
                    // Server sends a redirect url when email is wrong (example@a.a)
                    // Or response.body.contains("Document moved")
                    throw SignupThrowable.WrongEmailException
                }
                response.body.contains(SignupThrowableConstants.BAD_SERVER_RESPONSE) -> {
                    throw SignupThrowable.BadServerResponseException
                }
                else -> {
                    response.body
                }
            }
        )
    }
    
    companion object {
        private const val SUCCESSFUL_SIGNUP_MESSAGE =
            "Enregistrement fait avec succès,vous pouvez vous connecter en utilisant votre nom utlisateur et votre mot de passe"
    }
}