package earth.core

import earth.core.database.ElectricityPMD
import earth.core.database.MenageType
import earth.core.database.StateSupport
import java.math.BigDecimal

data class EstimateData(
    val menageType: MenageType,
    val stateSupport: StateSupport,
    val electricityPMD: ElectricityPMD,
    val gasPcs: BigDecimal,
    val electricityConsumption: BigDecimal,
    val gasConsumption: BigDecimal
)
