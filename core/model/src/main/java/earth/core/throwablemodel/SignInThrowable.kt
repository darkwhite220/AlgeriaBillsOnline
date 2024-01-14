package earth.core.throwablemodel

sealed class SignInThrowable : Throwable() {
    data object BadUsername : Throwable()
    data object BadPassword : Throwable()
    data object TemporarilyLockedAccount : Throwable()
}

object SignInThrowableConstants {
    const val WRONG_USERNAME = "Format utilisateur incorrect"
    const val WRONG_PASSWORD = "Mot de passe incorrect"
    const val WRONG_USERNAME_TWO = "Utilisateur non enregistré ou votre compte a éxpriré, " +
        "veuillez vous enregistrer en cliquant sur le lien en dessous de la section ou vous ètes authentifiés"
    const val TEMPORARILY_LOCKED_ACCOUNT_KEY = "location"
    const val TEMPORARILY_LOCKED_ACCOUNT_VALUE =
        "https://consulter-factures.elit.dz/fconsultation/muliti_connect.html"
    
}