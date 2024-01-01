package earth.core.networkmodel

data class SignInResponse(
    val fullName: String,
    val address: String,
    val billNumber: String,
    val date: String,
    val trimester: String,
    val year: String,
    val amount: String?,
    val billUrl: String?,
)
