package earth.core.network

object Constants {
    const val BASE_URL = "https://consulter-factures.elit.dz"
    const val SIGNUP_FORM_URL = "$BASE_URL/enregistrement.jsp"
    const val SIGNUP_CAPTCHA_URL = "$BASE_URL/simpleCaptcha.png"
    const val SIGNUP_URL = "$BASE_URL/verifenregistrement.jsp"
    const val LOGIN_AUTH_URL = "$BASE_URL/fconsultation/authentifier.jsp"
    const val LOGIN_CONSULT_URL = "$BASE_URL/fconsultation/Consult_Facture.jsp"
    const val HEADER_CONSULT_URL = "$BASE_URL/fconsultation/contenu_index.jsp"
    //    https://consulter-factures.elit.dz/fconsultation/fact.jsp?num_fac=459231108543&mtt_ttc=328.940&filial=SDC
    const val CONSULTING_BILL_URL = "$BASE_URL/fconsultation/"
    
    const val ACCEPT_ALL =
        "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"
    const val ACCEPT_IMAGE = "image/avif,image/webp,*/*"
    
    const val DEFAULT_VALUE = "N/A"
    
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
    const val SIGN_IN_USERNAME = "username"
    const val SIGN_IN_PASSWORD = "password"
    const val SIGN_IN_BUTTON_X = "button.x"
    const val SIGN_IN_BUTTON_Y = "button.y"
}