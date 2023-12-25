package earth.core.domain

import earth.core.data.SignupRepository
import earth.core.networkmodel.SignupRequestBody
import earth.core.networkmodel.SignupResponse
import earth.core.throwablemodel.SignupThrowable
import earth.core.throwablemodel.SignupThrowableConstants.BAD_SERVER_RESPONSE
import earth.core.throwablemodel.SignupThrowableConstants.FAILED_TRY_LATER
import earth.core.throwablemodel.SignupThrowableConstants.REFERENCE_NONE_EXISTENT
import earth.core.throwablemodel.SignupThrowableConstants.REFERENCE_NONE_VALID
import earth.core.throwablemodel.SignupThrowableConstants.WRONG_CAPTCHA
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
                response.body.contains(FAILED_TRY_LATER) -> {
                    throw SignupThrowable.FailedTryLaterException
                }
                response.body.contains(WRONG_CAPTCHA) -> {
                    throw SignupThrowable.WrongCaptchaException
                }
                response.body.contains(REFERENCE_NONE_VALID) ||
                    response.body.contains(REFERENCE_NONE_EXISTENT) -> {
                    throw SignupThrowable.WrongReferenceException
                }
                response.body.contains(BAD_SERVER_RESPONSE) -> {
                    throw SignupThrowable.BadServerResponseException
                }
                else -> {
                    response.body
                }
            }
        )
    }
}