package earth.core.throwablemodel

sealed class SignupThrowable : Throwable() {
    data object FailedTryLaterException : Throwable()
    data object WrongCaptchaException : Throwable()
    data object WrongReferenceException : Throwable()
    data object WrongEmailException : Throwable()
    data object ExistingUsernameException : Throwable()
    data object BadServerResponseException : Throwable()
    data object ServerOffline : Throwable()
}

object SignupThrowableConstants {
    const val REDIRECT_STATUS_CODE = 302
    const val FAILED_TRY_LATER = "Impossible de se connecter à la base de données"
    const val REFERENCE_NONE_VALID = "référence non valide"
    const val REFERENCE_NONE_EXISTENT =
        "Nom utilisateur incorrect ou référence non encore chargée dans le système"
    const val WRONG_CAPTCHA_FIRST = "Saisissez le mot corretement"
    const val WRONG_CAPTCHA_SECOND = "Saisissez le mot une autre fois"
    const val BAD_SERVER_RESPONSE = "<title>Erreur</title>"
    const val EXISTING_USERNAME_RESPONSE = "Un compte existe déjà avec ce nom utilisateur"
}