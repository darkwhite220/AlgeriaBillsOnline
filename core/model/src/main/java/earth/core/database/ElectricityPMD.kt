package earth.core.database

import java.math.BigDecimal

enum class ElectricityPMD(val price: BigDecimal, val value: Int) {
    LOW_MONO_PHASE(BigDecimal("52.40"), 4),
    MEDIUM_MONO_PHASE(BigDecimal("78.66"), 6),
    HIGH_MONO_PHASE(BigDecimal("157.30"), 12),
    LOW_TRI_PHASE(BigDecimal("262.20"), 20),
    MEDIUM_TRI_PHASE(BigDecimal("524.40"), 40),
    HIGH_TRI_PHASE(BigDecimal("786.60"), 60),
    VERY_HIGH_TRI_PHASE(BigDecimal("1048.80"), 80),
}