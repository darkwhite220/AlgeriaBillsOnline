package earth.core.database

import java.math.BigDecimal

data class Bill(
    val reference: String,
    val isPaid: Boolean,
    
    val pdfByteArray: ByteArray?,
    val billNumber: String,
    val date: String,
    val trimester: String,
    val year: String,
    val ebp: String,
    val ebb: String,
    
    val electricityMeterNumber: String,
    val electNewValue: Int,
    val electOldValue: Int,
    val electConsumption: BigDecimal,
    val electConsumptionCost: BigDecimal,
    
    val gazMeterNumber: String,
    val gazNewValue: Int,
    val gazOldValue: Int,
    val gazConsumption: BigDecimal,
    val gazConsumptionCost: BigDecimal,
    
    val stateSupport: BigDecimal,
    // Taxe d'habitation (can be 600/300/150/75) +
    // Droit fixe sur consommation (0, 25, 50, 100) 70-190-390kWh
    val rightsAndTaxes: Int,
    // electConsumptionCost + gazConsumptionCost + rightsAndTaxes
    val totalHT: BigDecimal,
    val electricityTva: BigDecimal,
    val gazTva: BigDecimal,
    // 9% + 19% TAVs
    val totalTva: BigDecimal,
    
    val totalTTCNoTimbre: BigDecimal,
    // 1% of total rounded to Int
    val timbre: BigDecimal,
    // total + timbre
    val totalTTC: BigDecimal,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as Bill
        
        if (reference != other.reference) return false
        if (isPaid != other.isPaid) return false
        if (pdfByteArray != null) {
            if (other.pdfByteArray == null) return false
            if (!pdfByteArray.contentEquals(other.pdfByteArray)) return false
        } else if (other.pdfByteArray != null) return false
        if (billNumber != other.billNumber) return false
        if (date != other.date) return false
        if (trimester != other.trimester) return false
        if (year != other.year) return false
        if (ebp != other.ebp) return false
        if (ebb != other.ebb) return false
        if (electricityMeterNumber != other.electricityMeterNumber) return false
        if (electNewValue != other.electNewValue) return false
        if (electOldValue != other.electOldValue) return false
        if (electConsumption != other.electConsumption) return false
        if (electConsumptionCost != other.electConsumptionCost) return false
        if (gazMeterNumber != other.gazMeterNumber) return false
        if (gazNewValue != other.gazNewValue) return false
        if (gazOldValue != other.gazOldValue) return false
        if (gazConsumption != other.gazConsumption) return false
        if (gazConsumptionCost != other.gazConsumptionCost) return false
        if (stateSupport != other.stateSupport) return false
        if (rightsAndTaxes != other.rightsAndTaxes) return false
        if (totalHT != other.totalHT) return false
        if (electricityTva != other.electricityTva) return false
        if (gazTva != other.gazTva) return false
        if (totalTva != other.totalTva) return false
        if (totalTTCNoTimbre != other.totalTTCNoTimbre) return false
        if (timbre != other.timbre) return false
        return totalTTC == other.totalTTC
    }
    
    override fun hashCode(): Int {
        var result = reference.hashCode()
        result = 31 * result + isPaid.hashCode()
        result = 31 * result + (pdfByteArray?.contentHashCode() ?: 0)
        result = 31 * result + billNumber.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + trimester.hashCode()
        result = 31 * result + year.hashCode()
        result = 31 * result + ebp.hashCode()
        result = 31 * result + ebb.hashCode()
        result = 31 * result + electricityMeterNumber.hashCode()
        result = 31 * result + electNewValue
        result = 31 * result + electOldValue
        result = 31 * result + electConsumption.hashCode()
        result = 31 * result + electConsumptionCost.hashCode()
        result = 31 * result + gazMeterNumber.hashCode()
        result = 31 * result + gazNewValue
        result = 31 * result + gazOldValue
        result = 31 * result + gazConsumption.hashCode()
        result = 31 * result + gazConsumptionCost.hashCode()
        result = 31 * result + stateSupport.hashCode()
        result = 31 * result + rightsAndTaxes
        result = 31 * result + totalHT.hashCode()
        result = 31 * result + electricityTva.hashCode()
        result = 31 * result + gazTva.hashCode()
        result = 31 * result + totalTva.hashCode()
        result = 31 * result + totalTTCNoTimbre.hashCode()
        result = 31 * result + timbre.hashCode()
        result = 31 * result + totalTTC.hashCode()
        return result
    }
    
}

fun Bill.asBillDownload() = BillDownload(
    reference = reference,
    trimester = trimester,
    year = year,
    byteArray = pdfByteArray!!,
)
