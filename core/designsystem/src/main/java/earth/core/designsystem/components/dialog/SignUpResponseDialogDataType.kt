package earth.core.designsystem.components.dialog

import earth.core.designsystem.Constants.FAILED_LOTTIE_FILE
import earth.core.designsystem.Constants.SUCCESSFUL_LOTTIE_FILE
import earth.core.designsystem.R

enum class SignUpResponseDialogDataType(
    val dialogData: DialogData
) {
    SUCCESS(
        dialogData = DialogData(
            lottieFileName = SUCCESSFUL_LOTTIE_FILE,
            titleId = R.string.account_creation_successful,
            textId = null,
            buttonTextId = R.string.continue_button,
        )
    ),
    FAILED_SERVER_ERROR_TRY_LATER(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.account_creation_failed,
            textId = R.string.account_creation_failed_try_again_later,
            buttonTextId = R.string.dismiss,
        )
    ),
    FAILED_WRONG_CAPTCHA(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.account_creation_failed,
            textId = R.string.account_creation_failed_wrong_captcha,
            buttonTextId = R.string.dismiss,
        )
    ),
    FAILED_WRONG_REFERENCE(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.account_creation_failed,
            textId = R.string.account_creation_failed_wrong_reference,
            buttonTextId = R.string.dismiss,
        )
    ),
    FAILED_WRONG_EMAIL(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.account_creation_failed,
            textId = R.string.account_creation_failed_wrong_email,
            buttonTextId = R.string.dismiss,
        )
    ),
    FAILED_EXISTING_USERNAME(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.account_creation_failed,
            textId = R.string.account_creation_failed_existing_username,
            buttonTextId = R.string.dismiss,
        )
    ),
    FAILED(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.account_creation_failed,
            textId = R.string.account_creation_failed_desc,
            buttonTextId = R.string.dismiss,
        )
    ),
}
