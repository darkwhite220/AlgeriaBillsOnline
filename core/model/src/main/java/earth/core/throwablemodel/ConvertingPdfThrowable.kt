package earth.core.throwablemodel

sealed class ConvertingPdfThrowable : Throwable() {
    
    data class UnhandledSignInResponse(val data: String) : Throwable()
    data class UnhandledPdfFormat(val data: List<String>) : Throwable()
    data class BadPdfFormat(val data: List<String>) : Throwable()
    data class BadPdfFormatWithStateSupport(val data: List<String>) : Throwable()
    
    data object BadUsername : Throwable()
    data object BadPassword : Throwable()
}

object ConvertingPdfThrowableConstants {
    const val WRONG_USERNAME = "Format utilisateur incorrect"
    const val WRONG_PASSWORD = "Mot de passe incorrect"
    const val WRONG_USERNAME_TWO = "Utilisateur non enregistré ou votre compte a éxpriré, " +
        "veuillez vous enregistrer en cliquant sur le lien en dessous de la section ou vous ètes authentifiés"
}