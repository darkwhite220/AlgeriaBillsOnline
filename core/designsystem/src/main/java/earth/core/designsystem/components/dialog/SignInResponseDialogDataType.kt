package earth.core.designsystem.components.dialog

import earth.core.designsystem.utils.Constants.FAILED_LOTTIE_FILE
import earth.core.designsystem.utils.Constants.SUCCESSFUL_LOTTIE_FILE
import earth.core.designsystem.R

enum class SignInResponseDialogDataType(
    val dialogData: DialogData
) {
    SUCCESS(
        dialogData = DialogData(
            lottieFileName = SUCCESSFUL_LOTTIE_FILE,
            titleId = R.string.sign_in_successful,
            textId = null,
            buttonTextId = R.string.continue_button,
        )
    ),
    SUCCESS_CREDENTIAL_UPDATE(
        dialogData = DialogData(
            lottieFileName = SUCCESSFUL_LOTTIE_FILE,
            titleId = R.string.sign_in_successful_updated_credentials,
            textId = R.string.sign_in_successful_updated_credentials_desc,
            buttonTextId = R.string.continue_button,
        )
    ),
    FAILED_WRONG_USERNAME(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.sign_in_failed,
            textId = R.string.sign_in_failed_wrong_username_desc,
            buttonTextId = R.string.dismiss,
        )
    ),
    FAILED_WRONG_PASSWORD(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.sign_in_failed,
            textId = R.string.sign_in_failed_wrong_password_desc,
            buttonTextId = R.string.dismiss,
        )
    ),
    TEMPORARILY_LOCKED_ACCOUNT(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.sign_in_failed,
            textId = R.string.sign_in_failed_temporary_lock_desc,
            buttonTextId = R.string.dismiss,
        )
    ),
    FAILED(
        dialogData = DialogData(
            lottieFileName = FAILED_LOTTIE_FILE,
            titleId = R.string.sign_in_failed,
            textId = R.string.sign_in_failed_desc,
            buttonTextId = R.string.dismiss,
        )
    ),
}
