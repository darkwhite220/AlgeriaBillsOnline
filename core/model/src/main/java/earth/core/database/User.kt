package earth.core.database

data class User(
    val fullName: String = "",
    val reference: String,
    val address: String = "",
    val email: String,
    val password: String,
    val username: String,
    val directionDistribution: String = "",
    val businessAgency: String = "",
    // From bills data we know if its house or store
    val isHouse: Boolean = true,
    val statistics: Statistics? = null,
)
