package earth.core.database

data class Bill(
    val reference: String,
    val isPaid: Boolean,
    
    val pdfByteArray: ByteArray,
    val billNumber: String,
    val date: String,
    val trimester: String,
    val year: String,
    val ebp: String,
    val ebb: String,
    
    val electricityMeterNumber: String,
    val electNewValue: Int,
    val electOldValue: Int,
    val electConsumption: Int,
    val electConsumptionCost: Float,
    
    val gazMeterNumber: String,
    val gazNewValue: Int,
    val gazOldValue: Int,
    val gazConsumption: Int,
    val gazConsumptionCost: Float,
    
    val stateSupport: Float,
    // Taxe d'habitation (can be 600/300/150/75) +
    // Droit fixe sur consommation (can be 0) doesn't appear in online bill
    val rightsAndTaxes: Float,
    // electConsumptionCost + gazConsumptionCost + rightsAndTaxes
    val amountHT: Float,
    // 9% + 19% TAVs
    val tva: Float,
    
    val total: Float,
    // 1% of total rounded to Int
    val timbre: Float,
    // total + timbre
    val totalTTC: Float,
)
