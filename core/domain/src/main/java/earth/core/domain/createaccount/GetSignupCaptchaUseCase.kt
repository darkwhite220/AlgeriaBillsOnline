package earth.core.domain.createaccount

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import earth.core.data.SignupRepository
import earth.core.networkmodel.SignupCaptcha
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Returns a Flow of Bitmap
 */
class GetSignupCaptchaUseCase @Inject constructor(
    private val signupRepository: SignupRepository,
) {
    
    operator fun invoke(): Flow<Bitmap> = flow {
        emit(
            signupRepository.getSignupCaptcha()
                .toBitmap()
        )
    }
    
}

private fun SignupCaptcha.toBitmap() =
    BitmapFactory.decodeByteArray(this.imageByteArray, 0, this.length)