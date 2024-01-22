package earth.darkwhite.feature.estimate

import androidx.compose.runtime.Immutable
import earth.core.EstimateData
import earth.core.data.util.Constants.GAZ_DEFAULT_PCS_VALUE
import earth.core.database.ElectricityPMD
import earth.core.database.MenageType
import earth.core.database.StateSupport

@Immutable
data class EstimateUiState(
    val menageType: MenageType = MenageType.M,
    val stateSupport: StateSupport = StateSupport.NONE,
    val electricityPMD: ElectricityPMD = ElectricityPMD.MEDIUM_MONO_PHASE,
    val gasPcs: String = "",
    val electricityConsumption: String = "",
    val gasConsumption: String = ""
)

fun EstimateUiState.asEstimateData() = EstimateData(
    menageType = menageType,
    stateSupport = stateSupport,
    electricityPMD = electricityPMD,
    gasPcs = if (gasPcs.isEmpty()) GAZ_DEFAULT_PCS_VALUE else gasPcs.toBigDecimal(),
    electricityConsumption = if (electricityConsumption.isEmpty()) "0".toBigDecimal()
    else electricityConsumption.toBigDecimal(),
    gasConsumption = if (gasConsumption.isEmpty()) "0".toBigDecimal()
    else gasConsumption.toBigDecimal(),
)