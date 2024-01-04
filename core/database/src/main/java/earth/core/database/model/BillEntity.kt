package earth.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import earth.core.database.Bill

@Entity(tableName = "bill_table")
data class BillEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "bill_number")
    val billNumber: String,
    
    val reference: String,
    @ColumnInfo(name = "is_paid")
    val isPaid: Boolean,
    @ColumnInfo(name = "pdf_format_data")
    val pdfByteArray: ByteArray,
    
    val date: String,
    val trimester: String,
    val year: String,
    val ebp: String,
    val ebb: String,
    
    @ColumnInfo(name = "electricity_meter_number")
    val electricityMeterNumber: String,
    @ColumnInfo(name = "elect_new_value")
    val electNewValue: Int,
    @ColumnInfo(name = "elect_old_value")
    val electOldValue: Int,
    @ColumnInfo(name = "elect_consumption")
    val electConsumption: Int,
    @ColumnInfo(name = "elect_consumption_cost")
    val electConsumptionCost: Float,
    
    @ColumnInfo(name = "gaz_meter_number")
    val gazMeterNumber: String,
    @ColumnInfo(name = "gaz_new_value")
    val gazNewValue: Int,
    @ColumnInfo(name = "gaz_old_value")
    val gazOldValue: Int,
    @ColumnInfo(name = "gaz_consumption")
    val gazConsumption: Int,
    @ColumnInfo(name = "gaz_consumption_cost")
    val gazConsumptionCost: Float,
    
    @ColumnInfo(name = "state_support")
    val stateSupport: Float,
    // Taxe d'ahbitation (can be 600/300/150/75) +
    // Droit fixe sur consommation (can be 0) doesn't appear in online bill
    @ColumnInfo(name = "rights_and_taxes")
    val rightsAndTaxes: Float,
    // electConsumptionCost + gazConsumptionCost + rightsAndTaxes
    @ColumnInfo(name = "amount_ht")
    val amountHT: Float,
    // 9% + 19% TAVs
    val tva: Float,
    
    val total: Float,
    // 1% of total rounded to Int
    val timbre: Float,
    // total + timbre
    @ColumnInfo(name = "total_ttc")
    val totalTTC: Float,
)

fun BillEntity.asExternalModel() = Bill(
    reference = reference,
    isPaid = isPaid,
    pdfByteArray = pdfByteArray,
    billNumber = billNumber,
    date = date,
    trimester = trimester,
    year = year,
    ebp = ebp,
    ebb = ebb,
    electricityMeterNumber = electricityMeterNumber,
    electNewValue = electNewValue,
    electOldValue = electOldValue,
    electConsumption = electConsumption,
    electConsumptionCost = electConsumptionCost,
    gazMeterNumber = gazMeterNumber,
    gazNewValue = gazNewValue,
    gazOldValue = gazOldValue,
    gazConsumption = gazConsumption,
    gazConsumptionCost = gazConsumptionCost,
    stateSupport = stateSupport,
    rightsAndTaxes = rightsAndTaxes,
    amountHT = amountHT,
    tva = tva,
    total = total,
    timbre = timbre,
    totalTTC = totalTTC,
)

fun Bill.asEntity() = BillEntity(
    reference = reference,
    isPaid = isPaid,
    pdfByteArray = pdfByteArray,
    billNumber = billNumber,
    date = date,
    trimester = trimester,
    year = year,
    ebp = ebp,
    ebb = ebb,
    electricityMeterNumber = electricityMeterNumber,
    electNewValue = electNewValue,
    electOldValue = electOldValue,
    electConsumption = electConsumption,
    electConsumptionCost = electConsumptionCost,
    gazMeterNumber = gazMeterNumber,
    gazNewValue = gazNewValue,
    gazOldValue = gazOldValue,
    gazConsumption = gazConsumption,
    gazConsumptionCost = gazConsumptionCost,
    stateSupport = stateSupport,
    rightsAndTaxes = rightsAndTaxes,
    amountHT = amountHT,
    tva = tva,
    total = total,
    timbre = timbre,
    totalTTC = totalTTC,
)
