package earth.core.designsystem.components.dialog

import earth.core.designsystem.utils.Constants
import earth.core.designsystem.R

enum class HomeScreenFailedResponseDialog(
    val dialogData: DialogData
) {
    FAILED_WRONG_USERNAME(
        dialogData = DialogData(
            lottieFileName = Constants.FAILED_LOTTIE_FILE,
            titleId = R.string.sync_failed,
            textId = R.string.sync_failed_sync_wrong_username_desc,
            buttonTextId = R.string.dismiss,
        )
    ),
    FAILED_WRONG_PASSWORD(
        dialogData = DialogData(
            lottieFileName = Constants.FAILED_LOTTIE_FILE,
            titleId = R.string.sync_failed,
            textId = R.string.sync_failed_sync_wrong_password_desc,
            buttonTextId = R.string.dismiss,
        )
    ),
    TEMPORARILY_LOCKED_ACCOUNT(
        dialogData = DialogData(
            lottieFileName = Constants.FAILED_LOTTIE_FILE,
            titleId = R.string.sync_failed,
            textId = R.string.sync_failed_sync_temporary_lock_desc,
            buttonTextId = R.string.dismiss,
        )
    ),
    PDF_TEXT_EXTRACTOR(
        dialogData = DialogData(
            lottieFileName = Constants.FAILED_LOTTIE_FILE,
            titleId = R.string.sync_failed,
            textId = R.string.sync_failed_sync_pdf_extractor_desc,
            buttonTextId = R.string.dismiss,
        )
    ),
    FAILED(
        dialogData = DialogData(
            lottieFileName = Constants.FAILED_LOTTIE_FILE,
            titleId = R.string.sync_failed,
            textId = R.string.sync_failed_desc,
            buttonTextId = R.string.dismiss,
        )
    ),
}