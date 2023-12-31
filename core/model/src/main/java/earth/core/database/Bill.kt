package earth.core.database

data class Bill(
    val reference: String,
    val isPaid: Boolean,
    
    val pdfFormatData: ByteArray,
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
    val electConsumptionCost: Int,
    
    val gazMeterNumber: String,
    val gazNewValue: Int,
    val gazOldValue: Int,
    val gazConsumption: Int,
    val gazConsumptionCost: Int,
    
    val stateSupport: Int,
    // Taxe d'habitation (can be 600/300/150/75) +
    // Droit fixe sur consommation (can be 0) doesn't appear in online bill
    val rightsAndTaxes: Int,
    // electConsumptionCost + gazConsumptionCost + rightsAndTaxes
    val amountHT: Int,
    // 9% + 19% TAVs
    val tva: Int,
    
    val total: Int,
    // 1% of total rounded to Int
    val timbre: Int,
    // total + timbre
    val totalTTC: Int,
)
