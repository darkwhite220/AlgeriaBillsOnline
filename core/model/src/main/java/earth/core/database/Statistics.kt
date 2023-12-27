package earth.core.database

data class Statistics(
    val yearAgv: Float,
    val trimesterAgv: Float,
    val dayAvg: Float,
    val maxPay: Float,
    val maxPayElect: Float,
    val maxPayGaz: Float,
    val minPay: Float,
    val minPayElect: Float,
    val minPayGaz: Float,
)
