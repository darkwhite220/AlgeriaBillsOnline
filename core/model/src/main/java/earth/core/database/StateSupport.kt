package earth.core.database

import java.math.BigDecimal

enum class StateSupport(
    val value: BigDecimal
) {
    NONE(value = "0".toBigDecimal()),
    HIGH_PLATEAU(value = "0.10".toBigDecimal()),
    SOUTH_NONE_AGRICULTURE(value = "0.25".toBigDecimal()),
    SOUTH_AGRICULTURE(value = "0.65".toBigDecimal()),
}
