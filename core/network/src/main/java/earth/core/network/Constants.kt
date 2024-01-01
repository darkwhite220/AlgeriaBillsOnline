package earth.core.network

object Constants {
    const val BASE_URL = "https://consulter-factures.elit.dz"
    const val SIGNUP_FORM_URL = "$BASE_URL/enregistrement.jsp"
    const val SIGNUP_CAPTCHA_URL = "$BASE_URL/simpleCaptcha.png"
    const val SIGNUP_URL = "$BASE_URL/verifenregistrement.jsp"
    const val LOGIN_AUTH_URL = "$BASE_URL/fconsultation/authentifier.jsp"
    const val LOGIN_CONSULT_URL = "$BASE_URL/fconsultation/Consult_Facture.jsp"
    const val CONSULTING_URL = "$BASE_URL/fconsultation/contenu_index.jsp"
    
    const val ACCEPT_ALL =
        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"
    const val ACCEPT_IMAGE = "image/avif,image/webp,*/*"
    
    // Signup request body
    const val NO_VALUE = ""
    const val NAME = "nomprenom"
    const val EMAIL = "email"
    const val PHONE_NUMBER = "telephone"
    const val USERNAME = "login"
    const val REFERENCE_NUMBER = "utilisateur"
    const val PASSWORD = "newpass"
    const val DUPLICATE_PASSWORD = "cfnewpass"
    const val CAPTCHA = "answer"
    const val ACTION_BUTTON = "btnAction"
    const val ACTION_BUTTON_VALUE = "      Valider      "
    
    // Login
    const val LOGIN_USERNAME = "username"
    const val LOGIN_PASSWORD = "password"
    const val LOGIN_BUTTON_X = "button.x"
    const val LOGIN_BUTTON_Y = "button.y"
}