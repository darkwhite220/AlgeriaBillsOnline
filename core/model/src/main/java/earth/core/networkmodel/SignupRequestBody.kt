package earth.core.networkmodel

data class SignupRequestBody(
    val nomprenom: String,
    val email: String,
    val telephone: String,
    val username: String,
    val reference: String,
    val newpass: String,
    val cfnewpass: String,
    val captcha: String,
    val btnAction: String,
)


