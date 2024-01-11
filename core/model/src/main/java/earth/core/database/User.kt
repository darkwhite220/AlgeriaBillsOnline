package earth.core.database

import earth.core.database.ElectricityPMD.HIGH_MONO_PHASE
import earth.core.database.ElectricityPMD.HIGH_TRI_PHASE
import earth.core.database.ElectricityPMD.LOW_MONO_PHASE
import earth.core.database.ElectricityPMD.LOW_TRI_PHASE
import earth.core.database.ElectricityPMD.MEDIUM_MONO_PHASE
import earth.core.database.ElectricityPMD.MEDIUM_TRI_PHASE
import earth.core.database.ElectricityPMD.VERY_HIGH_TRI_PHASE
import java.math.BigDecimal

data class User(
    val fullName: String = "",
    val reference: String,
    val address: String = "",
    val email: String = "",
    val password: String,
    val username: String,
    val directionDistribution: String = "",
    val businessAgency: String = "",
    val isHouse: Boolean = true,
    val isInState: Boolean = true,
    val electPMD: Int = 6,
    val gasPCS: BigDecimal = BigDecimal("0"),
    val lastBillNumber: String = "",
    val statistics: Statistics? = null,
    val billsPreview: List<BillPreview>? = null,
) {
    val electricityPMD: ElectricityPMD
        get() = when (electPMD) {
            LOW_MONO_PHASE.value -> LOW_MONO_PHASE
            HIGH_MONO_PHASE.value -> HIGH_MONO_PHASE
            LOW_TRI_PHASE.value -> LOW_TRI_PHASE
            MEDIUM_TRI_PHASE.value -> MEDIUM_TRI_PHASE
            HIGH_TRI_PHASE.value -> HIGH_TRI_PHASE
            VERY_HIGH_TRI_PHASE.value -> VERY_HIGH_TRI_PHASE
            else -> MEDIUM_MONO_PHASE
        }
    
    val menageType: MenageType
        get() = when {
            isHouse && !isInState -> MenageType.M_OUT_CITY
            !isHouse && isInState -> MenageType.NM
            !isHouse && !isInState -> MenageType.NM_OUT_CITY
            else -> MenageType.M
        }
}
