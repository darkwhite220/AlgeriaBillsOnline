package earth.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import earth.core.database.Bill
import java.math.BigDecimal

@Entity(tableName = "bill_table")
data class BillEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "bill_number")
    val billNumber: String,
    
    val reference: String,
    @ColumnInfo(name = "is_paid")
    val isPaid: Boolean,
    @ColumnInfo(name = "pdf_format_data")
    val pdfByteArray: ByteArray?,
    
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
    val electConsumption: String,
    @ColumnInfo(name = "elect_consumption_cost")
    val electConsumptionCost: String,
    
    @ColumnInfo(name = "gaz_meter_number")
    val gazMeterNumber: String,
    @ColumnInfo(name = "gaz_new_value")
    val gazNewValue: Int,
    @ColumnInfo(name = "gaz_old_value")
    val gazOldValue: Int,
    @ColumnInfo(name = "gaz_consumption")
    val gazConsumption: String,
    @ColumnInfo(name = "gaz_consumption_cost")
    val gazConsumptionCost: String,
    
    @ColumnInfo(name = "state_support")
    val stateSupport: String,
    // Taxe d'ahbitation (can be 600/300/150/75) +
    // Droit fixe sur consommation (0, 25, 50, 100) >70-190-390<kWh
    @ColumnInfo(name = "rights_and_taxes")
    val rightsAndTaxes: Int,
    // electConsumptionCost + gazConsumptionCost + rightsAndTaxes
    @ColumnInfo(name = "amount_ht")
    val totalHT: String,
    @ColumnInfo(name = "electricity_tva")
    val electricityTva: String,
    @ColumnInfo(name = "gaz_tva")
    val gazTva: String,
    // 9% + 19% TAVs
    val totalTva: String,
    
    val totalTTCNoTimbre: String,
    // 1% of total rounded to Int
    val timbre: String,
    // total + timbre
    @ColumnInfo(name = "total_ttc")
    val totalTTC: String,
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
    electConsumption = BigDecimal(electConsumption),
    electConsumptionCost = BigDecimal(electConsumptionCost),
    gazMeterNumber = gazMeterNumber,
    gazNewValue = gazNewValue,
    gazOldValue = gazOldValue,
    gazConsumption = BigDecimal(gazConsumption),
    gazConsumptionCost = BigDecimal(gazConsumptionCost),
    stateSupport = BigDecimal(stateSupport),
    rightsAndTaxes = rightsAndTaxes,
    totalHT = BigDecimal(totalHT),
    electricityTva = BigDecimal(electricityTva),
    gazTva = BigDecimal(gazTva),
    totalTva = BigDecimal(totalTva),
    totalTTCNoTimbre = BigDecimal(totalTTCNoTimbre),
    timbre = BigDecimal(timbre),
    totalTTC = BigDecimal(totalTTC),
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
    electConsumption = electConsumption.toString(),
    electConsumptionCost = electConsumptionCost.toString(),
    gazMeterNumber = gazMeterNumber,
    gazNewValue = gazNewValue,
    gazOldValue = gazOldValue,
    gazConsumption = gazConsumption.toString(),
    gazConsumptionCost = gazConsumptionCost.toString(),
    stateSupport = stateSupport.toString(),
    rightsAndTaxes = rightsAndTaxes,
    totalHT = totalHT.toString(),
    electricityTva = electricityTva.toString(),
    gazTva = gazTva.toString(),
    totalTva = totalTva.toString(),
    totalTTCNoTimbre = totalTTCNoTimbre.toString(),
    timbre = timbre.toString(),
    totalTTC = totalTTC.toString(),
)
