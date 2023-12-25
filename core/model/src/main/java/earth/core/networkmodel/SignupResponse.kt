package earth.core.networkmodel

data class SignupResponse(
    val headers: Set<Map.Entry<String, List<String>>>,
    val body: String
)
