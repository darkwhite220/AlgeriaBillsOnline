package earth.core.data.util

import earth.core.data.util.Constants.ELECT_MENAGE_FIRST_TRANCHE
import earth.core.data.util.Constants.ELECT_MENAGE_FIRST_TRANCHE_PRICE
import earth.core.data.util.Constants.ELECT_MENAGE_FOURTH_TRANCHE_PRICE
import earth.core.data.util.Constants.ELECT_MENAGE_FULL_FIRST_TRANCHE_PRICE
import earth.core.data.util.Constants.ELECT_MENAGE_FULL_SECOND_TRANCHE_PRICE
import earth.core.data.util.Constants.ELECT_MENAGE_FULL_THIRD_TRANCHE_PRICE
import earth.core.data.util.Constants.ELECT_MENAGE_SECOND_TRANCHE
import earth.core.data.util.Constants.ELECT_MENAGE_SECOND_TRANCHE_PRICE
import earth.core.data.util.Constants.ELECT_MENAGE_THIRD_TRANCHE
import earth.core.data.util.Constants.ELECT_MENAGE_THIRD_TRANCHE_PRICE
import earth.core.data.util.Constants.ELECT_NONE_MENAGE_FIRST_TRANCHE
import earth.core.data.util.Constants.ELECT_NONE_MENAGE_FIRST_TRANCHE_PRICE
import earth.core.data.util.Constants.ELECT_NONE_MENAGE_FULL_FIRST_TRANCHE_PRICE
import earth.core.data.util.Constants.ELECT_NONE_MENAGE_FULL_SECOND_TRANCHE_PRICE
import earth.core.data.util.Constants.ELECT_NONE_MENAGE_SECOND_TRANCHE
import earth.core.data.util.Constants.ELECT_NONE_MENAGE_SECOND_TRANCHE_PRICE
import earth.core.data.util.Constants.ELECT_NONE_MENAGE_THIRD_TRANCHE_PRICE
import earth.core.data.util.Constants.FIRST_TAV
import earth.core.data.util.Constants.GAZ_DEFAULT_CONSUMPTION_VALUE
import earth.core.data.util.Constants.GAZ_MENAGE_FIRST_TRANCHE
import earth.core.data.util.Constants.GAZ_MENAGE_FIRST_TRANCHE_PRICE
import earth.core.data.util.Constants.GAZ_MENAGE_FOURTH_TRANCHE_PRICE
import earth.core.data.util.Constants.GAZ_MENAGE_FULL_FIRST_TRANCHE_PRICE
import earth.core.data.util.Constants.GAZ_MENAGE_FULL_SECOND_AND_THIRD_TRANCHE_PRICE
import earth.core.data.util.Constants.GAZ_MENAGE_FULL_SECOND_TRANCHE_PRICE
import earth.core.data.util.Constants.GAZ_MENAGE_FULL_THIRD_TRANCHE_PRICE
import earth.core.data.util.Constants.GAZ_MENAGE_SECOND_TRANCHE
import earth.core.data.util.Constants.GAZ_MENAGE_SECOND_TRANCHE_PRICE
import earth.core.data.util.Constants.GAZ_MENAGE_THIRD_TRANCHE
import earth.core.data.util.Constants.GAZ_MENAGE_THIRD_TRANCHE_PRICE
import earth.core.data.util.Constants.GAZ_NONE_MENAGE_FIRST_TRANCHE
import earth.core.data.util.Constants.GAZ_NONE_MENAGE_FIRST_TRANCHE_PRICE
import earth.core.data.util.Constants.GAZ_NONE_MENAGE_FULL_FIRST_AND_SECOND_TRANCHE_PRICE
import earth.core.data.util.Constants.GAZ_NONE_MENAGE_FULL_FIRST_TRANCHE_PRICE
import earth.core.data.util.Constants.GAZ_NONE_MENAGE_FULL_SECOND_TRANCHE_PRICE
import earth.core.data.util.Constants.GAZ_NONE_MENAGE_SECOND_TRANCHE
import earth.core.data.util.Constants.GAZ_NONE_MENAGE_SECOND_TRANCHE_PRICE
import earth.core.data.util.Constants.GAZ_NONE_MENAGE_THIRD_TRANCHE_PRICE
import earth.core.data.util.Constants.PMD_MARGIN
import earth.core.data.util.Constants.RIGHTS_FIRST_TRANCHE
import earth.core.data.util.Constants.RIGHTS_FIRST_TRANCHE_PRICE
import earth.core.data.util.Constants.RIGHTS_SECOND_TRANCHE
import earth.core.data.util.Constants.RIGHTS_SECOND_TRANCHE_PRICE
import earth.core.data.util.Constants.RIGHTS_THIRD_TRANCHE
import earth.core.data.util.Constants.RIGHTS_THIRD_TRANCHE_PRICE
import earth.core.data.util.Constants.SECOND_TAV
import earth.core.database.Bill
import earth.core.database.ElectricityPMD
import earth.core.database.MenageType
import earth.core.network.Constants
import java.math.BigDecimal


object ExtractionUtil {
    
    fun extractMainBillData(
        dataSource: List<String>,
        pdfByteArray: ByteArray,
        isPaid: Boolean,
        onDone: (String, String, MenageType, ElectricityPMD, BigDecimal) -> Unit,
    ): Bill {
        var mutableList: MutableList<String> = dataSource.toMutableList()
        
        var directionDistribution = mutableList.removeFirst()
        directionDistribution =
            directionDistribution.removePrefix("Direction Distribution : ")
                .substringBeforeLast(" ").trim()
                .replace(Regex("\\s+"), " ")
        
        var agence = mutableList.removeFirst()
        agence = agence.removePrefix("Agence Commerciale : ").substringBeforeLast(" ")
            .trim().replace(Regex("\\s+"), " ")
        
        val reference = mutableList.removeFirst().removePrefix("Référence : ")
        mutableList = mutableList.subList(2, mutableList.size)
        val billNumber = mutableList.removeFirst().removePrefix("Facture N° : ")
        var dateData = mutableList.removeFirst()
        dateData = dateData.removePrefix("Date facture: ")
        val date = dateData.takeWhile { it != ' ' }
        dateData = dateData.removePrefix(date).trim().removePrefix("Période : ")
        val trimester = dateData.take(1)
        val year = dateData.takeLast(4)
        
        val ebp = mutableList.removeFirst()
        
        val electRow = mutableList.removeFirst().split(" ")
        val electricityMeterNumber = electRow[0]
        val electNewValue = electRow[1].toInt()
        val electOldValue = electRow[2].toInt()
        val electConsumption = electRow[3]
        val electConsumptionCost = electRow[4].replace(",", ".") // Min 78,66
        
        val ebb = mutableList.removeFirst()
        
        val gazRow = mutableList.removeFirst().split(" ")
        val gazMeterNumber = gazRow[0]
        val gazNewValue = gazRow[1].toInt()
        val gazOldValue = gazRow[2].toInt()
        val gazConsumption = gazRow[3]
        val gazConsumptionCost = gazRow[4].replace(",", ".") // Min 85,50
        
        var stateSupport = "0.00"
        val total: String
        if (mutableList[0].contains("Soutien de l'état:")) {
            total = mutableList.removeFirst().removePrefix("Soutien de l'état: ")
                .replace(",", ".")
            stateSupport = mutableList.removeFirst().replace(",", ".")
        } else {
            total = mutableList.removeFirst().replace(",", ".")
        }
        
        val timbre = mutableList.removeFirst().removePrefix("Droits & Taxes: ")
            .replace(",", ".")
        
        val rightsAndTaxes = mutableList.removeFirst().takeWhile { it != ',' }.toInt()
        
        val amountHT = mutableList.removeFirst().removePrefix("Montant HT: ")
            .replace(",", ".")
        
        val tva = mutableList.removeFirst().replace(",", ".")
        
        val totalTTC = mutableList.removeFirst().replace(",", ".")
        
        val menageType = menageType(
            electConsumption = electConsumption.toInt(),
            previousRightsAndTaxes = rightsAndTaxes,
        )
        
        val electricityPMD = extractElectricityPMD(
            consumption = electConsumption.toBigDecimal(),
            consumptionCost = electConsumptionCost.toBigDecimal(),
            menageType = menageType,
        )
        
        val gazPCS = extractGazPCS(
            consumptionCubicM = gazConsumption.toBigDecimal(),
            consumptionCostMinusDefault = gazConsumptionCost.toBigDecimal() - GAZ_DEFAULT_CONSUMPTION_VALUE,
            menageType = menageType,
        )
        
        val electricity = calculateElectricity(
            consumption = electConsumption.toBigDecimal(),
            menageType = menageType,
            defaultValue = electricityPMD.price,
        )
        val gaz = calculateGaz(
            consumptionTH = gazConsumption.toBigDecimal() * gazPCS,
            menageType = menageType,
        )
        
        onDone(directionDistribution, agence, menageType, electricityPMD, gazPCS)
        
        return Bill(
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
            electConsumption = electConsumption.toBigDecimal(),
            electConsumptionCost = electConsumptionCost.toBigDecimal(),
            gazMeterNumber = gazMeterNumber,
            gazNewValue = gazNewValue,
            gazOldValue = gazOldValue,
            gazConsumption = gazConsumption.toBigDecimal(),
            gazConsumptionCost = gazConsumptionCost.toBigDecimal(),
            stateSupport = stateSupport.toBigDecimal(),
            rightsAndTaxes = rightsAndTaxes,
            totalHT = amountHT.toBigDecimal(),
            electricityTva = electricity.totalTVA,
            gazTva = gaz.totalTVA,
            totalTva = tva.toBigDecimal(),
            totalTTCNoTimbre = BigDecimal(total),
            timbre = BigDecimal(timbre),
            totalTTC = BigDecimal(totalTTC)
        )
    }
    
    fun extractPreviousBillsData(
        dataSource: List<String>,
        mainBill: Bill,
        menageType: MenageType,
        electricityPMD: ElectricityPMD,
        gazPCS: BigDecimal,
        isPaid: Boolean,
        onDone: (BigDecimal) -> Unit,
    ): List<Bill> {
        var dataAsListOfString = dataSource
        val billsList = mutableListOf<Bill>()
        
        (0 until 4).forEach { index ->
//            Log.d(TAG, "extractPreviousBillsData: bill $index")
            billsList.add(
                extractPreviousBillData(
                    dataSource = dataAsListOfString,
                    mainBill = mainBill,
                    electOldMeterValue = billsList.getOrNull(index - 1)?.electOldValue
                        ?: mainBill.electOldValue,
                    gazOldMeterValue = billsList.getOrNull(index - 1)?.gazOldValue
                        ?: mainBill.gazOldValue,
                    menageType = menageType,
                    electricityPMD = electricityPMD,
                    gazPCS = gazPCS,
                    isPaid = isPaid,
                    onDone = onDone
                )
            )
            
            dataAsListOfString = dataAsListOfString.first { it.contains("Trimestre") }
                .startsWith("1").let {
                    if (it) {
                        dataAsListOfString.subList(4, dataAsListOfString.size)
                    } else {
                        dataAsListOfString.subList(5, dataAsListOfString.size)
                    }
                }
        }
        
        return billsList
    }
    
    fun menageType(
        electConsumption: Int,
        previousRightsAndTaxes: Int
    ): MenageType {
        val rights = when {
            previousRightsAndTaxes == MenageType.NM.price ||
                previousRightsAndTaxes == MenageType.NM_OUT_CITY.price -> 0
            electConsumption in RIGHTS_FIRST_TRANCHE..RIGHTS_SECOND_TRANCHE -> RIGHTS_FIRST_TRANCHE_PRICE
            electConsumption in RIGHTS_SECOND_TRANCHE..RIGHTS_THIRD_TRANCHE -> RIGHTS_SECOND_TRANCHE_PRICE
            electConsumption > RIGHTS_THIRD_TRANCHE -> RIGHTS_THIRD_TRANCHE_PRICE
            else -> 0
        }
//        Log.d(TAG, rights.toString())
        return when (previousRightsAndTaxes - rights) {
            MenageType.M.price -> MenageType.M
            MenageType.M_OUT_CITY.price -> MenageType.M_OUT_CITY
            MenageType.NM.price -> MenageType.NM
            MenageType.NM_OUT_CITY.price -> MenageType.NM_OUT_CITY
            else -> MenageType.M
        }
    }
    
    fun extractElectricityPMD(
        consumption: BigDecimal,
        consumptionCost: BigDecimal,
        menageType: MenageType
    ): ElectricityPMD {
        val calculationHolder = calculateElectricity(consumption, menageType)
        
        return when (consumptionCost - calculationHolder.totalHT) {
            ElectricityPMD.LOW_MONO_PHASE.price - PMD_MARGIN..ElectricityPMD.LOW_MONO_PHASE.price + PMD_MARGIN -> ElectricityPMD.LOW_MONO_PHASE
            ElectricityPMD.HIGH_MONO_PHASE.price - PMD_MARGIN..ElectricityPMD.HIGH_MONO_PHASE.price + PMD_MARGIN -> ElectricityPMD.HIGH_MONO_PHASE
            ElectricityPMD.LOW_TRI_PHASE.price - PMD_MARGIN..ElectricityPMD.LOW_TRI_PHASE.price + PMD_MARGIN -> ElectricityPMD.LOW_TRI_PHASE
            ElectricityPMD.MEDIUM_TRI_PHASE.price - PMD_MARGIN..ElectricityPMD.MEDIUM_TRI_PHASE.price + PMD_MARGIN -> ElectricityPMD.MEDIUM_TRI_PHASE
            ElectricityPMD.HIGH_TRI_PHASE.price - PMD_MARGIN..ElectricityPMD.HIGH_TRI_PHASE.price + PMD_MARGIN -> ElectricityPMD.HIGH_TRI_PHASE
            ElectricityPMD.VERY_HIGH_TRI_PHASE.price - PMD_MARGIN..ElectricityPMD.VERY_HIGH_TRI_PHASE.price + PMD_MARGIN -> ElectricityPMD.VERY_HIGH_TRI_PHASE
            else -> ElectricityPMD.MEDIUM_MONO_PHASE
        }
    }
    
    fun calculateElectricity(
        consumption: BigDecimal,
        menageType: MenageType,
        defaultValue: BigDecimal? = null,
    ): CalculationHolder {
        return if (menageType in listOf(MenageType.M, MenageType.M_OUT_CITY)) {
            calculateElectMenage(consumption, defaultValue)
        } else {
            calculateElectNoneMenage(consumption, defaultValue)
        }
    }
    
    private fun calculateElectMenage(
        consumption: BigDecimal,
        defaultValue: BigDecimal? = null
    ): CalculationHolder {
        val firstTotalHT: BigDecimal
        var firstTVA = "0".toBigDecimal()
        var firstTotalTTC = "0".toBigDecimal()
        val secondTotalHT: BigDecimal
        val secondTVA: BigDecimal
        
        return when {
            // 1000+
            consumption > ELECT_MENAGE_THIRD_TRANCHE -> {
                if (defaultValue != null) {
                    firstTotalHT = ELECT_MENAGE_FULL_SECOND_TRANCHE_PRICE + defaultValue
                    firstTVA = (firstTotalHT * FIRST_TAV).round()
                    firstTotalTTC = firstTotalHT + firstTVA
                } else {
                    firstTotalHT = ELECT_MENAGE_FULL_SECOND_TRANCHE_PRICE
                }
                secondTotalHT = ((consumption - ELECT_MENAGE_THIRD_TRANCHE) *
                    ELECT_MENAGE_FOURTH_TRANCHE_PRICE).round() + ELECT_MENAGE_FULL_THIRD_TRANCHE_PRICE
                secondTVA = (secondTotalHT * SECOND_TAV).round()
                CalculationHolder(
                    firstTotalHT = firstTotalHT,
                    firstTotalTTC = firstTotalTTC,
                    firstTVA = firstTVA,
                    secondTotalHT = secondTotalHT,
                    secondTotalTTC = secondTotalHT + secondTVA,
                    secondTVA = secondTVA,
                    totalHT = firstTotalHT + secondTotalHT,
                    totalTVA = firstTVA + secondTVA,
                )
            }
            
            // 250-1000
            consumption <= ELECT_MENAGE_THIRD_TRANCHE && consumption > ELECT_MENAGE_SECOND_TRANCHE -> {
                if (defaultValue != null) {
                    firstTotalHT = ELECT_MENAGE_FULL_SECOND_TRANCHE_PRICE + defaultValue
                    firstTVA = (firstTotalHT * FIRST_TAV).round()
                    firstTotalTTC = firstTotalHT + firstTVA
                } else {
                    firstTotalHT = ELECT_MENAGE_FULL_SECOND_TRANCHE_PRICE
                }
                secondTotalHT = ((consumption - ELECT_MENAGE_SECOND_TRANCHE) *
                    ELECT_MENAGE_THIRD_TRANCHE_PRICE).round()
                secondTVA = (secondTotalHT * SECOND_TAV).round()
                CalculationHolder(
                    firstTotalHT = firstTotalHT,
                    firstTotalTTC = firstTotalTTC,
                    firstTVA = firstTVA,
                    secondTotalHT = secondTotalHT,
                    secondTotalTTC = secondTotalHT + secondTVA,
                    secondTVA = secondTVA,
                    totalHT = firstTotalHT + secondTotalHT,
                    totalTVA = firstTVA + secondTVA,
                )
            }
            
            // 125-250
            consumption <= ELECT_MENAGE_SECOND_TRANCHE && consumption > ELECT_MENAGE_FIRST_TRANCHE -> {
                if (defaultValue != null) {
                    firstTotalHT = ((consumption - ELECT_MENAGE_FIRST_TRANCHE) *
                        ELECT_MENAGE_SECOND_TRANCHE_PRICE).round() +
                        ELECT_MENAGE_FULL_FIRST_TRANCHE_PRICE +
                        defaultValue
                    firstTVA = (firstTotalHT * FIRST_TAV).round()
                    firstTotalTTC = firstTotalHT + firstTVA
                } else {
                    firstTotalHT = ((consumption - ELECT_MENAGE_FIRST_TRANCHE) *
                        ELECT_MENAGE_SECOND_TRANCHE_PRICE).round() + ELECT_MENAGE_FULL_FIRST_TRANCHE_PRICE
                }
                CalculationHolder(
                    firstTotalHT = firstTotalHT,
                    firstTotalTTC = firstTotalTTC,
                    firstTVA = firstTVA,
                    totalHT = firstTotalHT,
                    totalTVA = firstTVA,
                )
            }
            
            // 125-
            else -> {
                if (defaultValue != null) {
                    firstTotalHT =
                        (consumption * ELECT_MENAGE_FIRST_TRANCHE_PRICE).round() + defaultValue
                    firstTVA = (firstTotalHT * FIRST_TAV).round()
                    firstTotalTTC = firstTotalHT + firstTVA
                } else {
                    firstTotalHT = (consumption * ELECT_MENAGE_FIRST_TRANCHE_PRICE).round()
                }
                CalculationHolder(
                    firstTotalHT = firstTotalHT,
                    firstTotalTTC = firstTotalTTC,
                    firstTVA = firstTVA,
                    totalHT = firstTotalHT,
                    totalTVA = firstTVA,
                )
            }
        }
    }
    
    private fun calculateElectNoneMenage(
        consumption: BigDecimal,
        defaultValue: BigDecimal? = null
    ): CalculationHolder {
        val firstTotalHT: BigDecimal
        var firstTVA = "0".toBigDecimal()
        var firstTotalTTC = "0".toBigDecimal()
        val secondTotalHT: BigDecimal
        val secondTVA: BigDecimal
        
        return when {
            // 1000+
            consumption > ELECT_NONE_MENAGE_SECOND_TRANCHE -> {
                if (defaultValue != null) {
                    firstTotalHT = ELECT_NONE_MENAGE_FULL_FIRST_TRANCHE_PRICE + defaultValue
                    firstTVA = (firstTotalHT * FIRST_TAV).round()
                    firstTotalTTC = firstTotalHT + firstTVA
                } else {
                    firstTotalHT = ELECT_NONE_MENAGE_FULL_FIRST_TRANCHE_PRICE
                }
                secondTotalHT = ((consumption - ELECT_NONE_MENAGE_SECOND_TRANCHE) *
                    ELECT_NONE_MENAGE_THIRD_TRANCHE_PRICE).round() +
                    ELECT_NONE_MENAGE_FULL_SECOND_TRANCHE_PRICE
                secondTVA = (secondTotalHT * SECOND_TAV).round()
                CalculationHolder(
                    firstTotalHT = firstTotalHT,
                    firstTotalTTC = firstTotalTTC,
                    firstTVA = firstTVA,
                    secondTotalHT = secondTotalHT,
                    secondTotalTTC = secondTotalHT + secondTVA,
                    secondTVA = secondTVA,
                    totalHT = firstTotalHT + secondTotalHT,
                    totalTVA = firstTVA + secondTVA,
                )
            }
            
            // 250-1000
            consumption <= ELECT_NONE_MENAGE_SECOND_TRANCHE && consumption > ELECT_NONE_MENAGE_FIRST_TRANCHE -> {
                if (defaultValue != null) {
                    firstTotalHT = ELECT_NONE_MENAGE_FULL_FIRST_TRANCHE_PRICE + defaultValue
                    firstTVA = (firstTotalHT * FIRST_TAV).round()
                    firstTotalTTC = firstTotalHT + firstTVA
                } else {
                    firstTotalHT = ELECT_NONE_MENAGE_FULL_FIRST_TRANCHE_PRICE
                }
                secondTotalHT = ((consumption - ELECT_NONE_MENAGE_FIRST_TRANCHE) *
                    ELECT_NONE_MENAGE_SECOND_TRANCHE_PRICE).round()
                secondTVA = (secondTotalHT * SECOND_TAV).round()
                CalculationHolder(
                    firstTotalHT = firstTotalHT,
                    firstTotalTTC = firstTotalTTC,
                    firstTVA = firstTVA,
                    secondTotalHT = secondTotalHT,
                    secondTotalTTC = secondTotalHT + secondTVA,
                    secondTVA = secondTVA,
                    totalHT = firstTotalHT + secondTotalHT,
                    totalTVA = firstTVA + secondTVA,
                )
            }
            
            // 250-
            else -> {
                if (defaultValue != null) {
                    firstTotalHT =
                        (consumption * ELECT_NONE_MENAGE_FIRST_TRANCHE_PRICE).round() + defaultValue
                    firstTVA = (firstTotalHT * FIRST_TAV).round()
                    firstTotalTTC = firstTotalHT + firstTVA
                } else {
                    firstTotalHT = (consumption * ELECT_NONE_MENAGE_FIRST_TRANCHE_PRICE).round()
                }
                CalculationHolder(
                    firstTotalHT = firstTotalHT,
                    firstTotalTTC = firstTotalTTC,
                    firstTVA = firstTVA,
                    totalHT = firstTotalHT,
                    totalTVA = firstTVA,
                )
            }
        }
    }
    
    fun extractGazPCS(
        consumptionCubicM: BigDecimal,
        consumptionCostMinusDefault: BigDecimal,
        menageType: MenageType
    ): BigDecimal {
        return if (consumptionCubicM == "0".toBigDecimal()) {
            "0".toBigDecimal()
        } else if (menageType in listOf(MenageType.M, MenageType.M_OUT_CITY)) {
            extractGazPCSMenage(consumptionCostMinusDefault, consumptionCubicM)
        } else {
            extractGazPCSNoneMenage(consumptionCostMinusDefault, consumptionCubicM)
        }
    }
    
    private fun extractGazPCSMenage(
        consumptionCostMinusDefault: BigDecimal,
        consumptionCubicM: BigDecimal
    ) = when {
        // 7500+
        consumptionCostMinusDefault > GAZ_MENAGE_FULL_SECOND_AND_THIRD_TRANCHE_PRICE -> {
            val quantity =
                ((consumptionCostMinusDefault - GAZ_MENAGE_FULL_SECOND_AND_THIRD_TRANCHE_PRICE) /
                    GAZ_MENAGE_FOURTH_TRANCHE_PRICE).roundDown()
            ((quantity + GAZ_MENAGE_THIRD_TRANCHE) / consumptionCubicM).roundDown()
        }
        
        // 2500-7500
        consumptionCostMinusDefault <= GAZ_MENAGE_FULL_SECOND_AND_THIRD_TRANCHE_PRICE &&
            consumptionCostMinusDefault > GAZ_MENAGE_FULL_SECOND_TRANCHE_PRICE -> {
            val quantity =
                ((consumptionCostMinusDefault - GAZ_MENAGE_FULL_SECOND_TRANCHE_PRICE) /
                    GAZ_MENAGE_THIRD_TRANCHE_PRICE).roundDown()
            ((quantity + GAZ_MENAGE_SECOND_TRANCHE) / consumptionCubicM).roundDown()
        }
        
        // 1125-2500
        consumptionCostMinusDefault <= GAZ_MENAGE_FULL_SECOND_TRANCHE_PRICE &&
            consumptionCostMinusDefault > GAZ_MENAGE_FULL_FIRST_TRANCHE_PRICE -> {
            val quantity =
                ((consumptionCostMinusDefault - GAZ_MENAGE_FULL_FIRST_TRANCHE_PRICE) /
                    GAZ_MENAGE_SECOND_TRANCHE_PRICE).roundDown()
            ((quantity + GAZ_MENAGE_FIRST_TRANCHE) / consumptionCubicM).roundDown()
        }
        
        // 1125-
        else -> {
            val quantity =
                (consumptionCostMinusDefault / GAZ_MENAGE_FIRST_TRANCHE_PRICE).roundDown()
            (quantity / consumptionCubicM).roundDown()
        }
    }
    
    private fun extractGazPCSNoneMenage(
        consumptionCostMinusDefault: BigDecimal,
        consumptionCubicM: BigDecimal
    ) = when {
        // 7500+
        consumptionCostMinusDefault > GAZ_NONE_MENAGE_FULL_FIRST_AND_SECOND_TRANCHE_PRICE -> {
            val quantity =
                ((consumptionCostMinusDefault - GAZ_NONE_MENAGE_FULL_FIRST_AND_SECOND_TRANCHE_PRICE) /
                    GAZ_NONE_MENAGE_THIRD_TRANCHE_PRICE).roundDown()
            ((quantity + GAZ_NONE_MENAGE_SECOND_TRANCHE) / consumptionCubicM).roundDown()
        }
        
        // 2500-7500
        consumptionCostMinusDefault <= GAZ_NONE_MENAGE_FULL_FIRST_AND_SECOND_TRANCHE_PRICE &&
            consumptionCostMinusDefault > GAZ_NONE_MENAGE_FULL_FIRST_TRANCHE_PRICE -> {
            val quantity =
                ((consumptionCostMinusDefault - GAZ_NONE_MENAGE_FULL_FIRST_TRANCHE_PRICE) /
                    GAZ_NONE_MENAGE_SECOND_TRANCHE_PRICE).roundDown()
            ((quantity + GAZ_NONE_MENAGE_FIRST_TRANCHE) / consumptionCubicM).roundDown()
        }
        
        // 2500-
        else -> {
            val quantity =
                (consumptionCostMinusDefault / GAZ_NONE_MENAGE_FIRST_TRANCHE_PRICE).roundDown()
            (quantity / consumptionCubicM).roundDown()
        }
    }
    
    fun calculateGaz(
        consumptionTH: BigDecimal,
        menageType: MenageType
    ): CalculationHolder {
        return if (menageType in listOf(MenageType.M, MenageType.M_OUT_CITY)) {
            calculateGazMenage(consumptionTH)
        } else {
            calculateGazNoneMenage(consumptionTH)
        }
    }
    
    private fun calculateGazMenage(consumptionTH: BigDecimal): CalculationHolder {
        val firstTotalHT: BigDecimal
        val firstTVA: BigDecimal
        val secondTotalHT: BigDecimal
        val secondTVA: BigDecimal
        
        return when {
            // 7500+
            consumptionTH > GAZ_MENAGE_THIRD_TRANCHE -> {
                firstTotalHT = GAZ_MENAGE_FULL_SECOND_TRANCHE_PRICE + GAZ_DEFAULT_CONSUMPTION_VALUE
                firstTVA = (firstTotalHT * FIRST_TAV).round()
                secondTotalHT = ((consumptionTH - GAZ_MENAGE_THIRD_TRANCHE) *
                    GAZ_MENAGE_FOURTH_TRANCHE_PRICE).round() + GAZ_MENAGE_FULL_THIRD_TRANCHE_PRICE
                secondTVA = (secondTotalHT * SECOND_TAV).round()
                CalculationHolder(
                    firstTotalHT = firstTotalHT,
                    firstTotalTTC = firstTotalHT + firstTVA,
                    firstTVA = firstTVA,
                    secondTotalHT = secondTotalHT,
                    secondTotalTTC = secondTotalHT + secondTVA,
                    secondTVA = secondTVA,
                    totalTVA = firstTVA + secondTVA,
                    totalHT = firstTotalHT + secondTotalHT,
                )
            }
            
            // 2500-7500
            consumptionTH <= GAZ_MENAGE_THIRD_TRANCHE && consumptionTH > GAZ_MENAGE_SECOND_TRANCHE -> {
                firstTotalHT = GAZ_MENAGE_FULL_SECOND_TRANCHE_PRICE + GAZ_DEFAULT_CONSUMPTION_VALUE
                firstTVA = (firstTotalHT * FIRST_TAV).round()
                secondTotalHT = ((consumptionTH - GAZ_MENAGE_SECOND_TRANCHE) *
                    GAZ_MENAGE_THIRD_TRANCHE_PRICE).round()
                secondTVA = (secondTotalHT * SECOND_TAV).round()
                CalculationHolder(
                    firstTotalHT = firstTotalHT,
                    firstTotalTTC = firstTotalHT + firstTVA,
                    firstTVA = firstTVA,
                    secondTotalHT = secondTotalHT,
                    secondTotalTTC = secondTotalHT + secondTVA,
                    secondTVA = secondTVA,
                    totalTVA = firstTVA + secondTVA,
                    totalHT = firstTotalHT + secondTotalHT,
                )
            }
            
            // 1125-2500
            consumptionTH <= GAZ_MENAGE_SECOND_TRANCHE && consumptionTH > GAZ_MENAGE_FIRST_TRANCHE -> {
                firstTotalHT = ((consumptionTH - GAZ_MENAGE_FIRST_TRANCHE) *
                    GAZ_MENAGE_SECOND_TRANCHE_PRICE).round() +
                    GAZ_MENAGE_FULL_FIRST_TRANCHE_PRICE +
                    GAZ_DEFAULT_CONSUMPTION_VALUE
                firstTVA = (firstTotalHT * FIRST_TAV).round()
                CalculationHolder(
                    firstTotalHT = firstTotalHT,
                    firstTotalTTC = firstTotalHT + firstTVA,
                    firstTVA = firstTVA,
                    totalTVA = firstTVA,
                    totalHT = firstTotalHT,
                )
            }
            
            // 1125-
            else -> {
                firstTotalHT = (consumptionTH * GAZ_MENAGE_FIRST_TRANCHE_PRICE).round() +
                    GAZ_DEFAULT_CONSUMPTION_VALUE
                firstTVA = (firstTotalHT * FIRST_TAV).round()
                CalculationHolder(
                    firstTotalHT = firstTotalHT,
                    firstTotalTTC = firstTotalHT + firstTVA,
                    firstTVA = firstTVA,
                    totalTVA = firstTVA,
                    totalHT = firstTotalHT,
                )
            }
        }
    }
    
    private fun calculateGazNoneMenage(consumption: BigDecimal): CalculationHolder {
        val firstTotalHT: BigDecimal
        val firstTVA: BigDecimal
        val secondTotalHT: BigDecimal
        val secondTVA: BigDecimal
        
        return when {
            // 1000+
            consumption > GAZ_NONE_MENAGE_SECOND_TRANCHE -> {
                firstTotalHT =
                    GAZ_NONE_MENAGE_FULL_FIRST_TRANCHE_PRICE + GAZ_DEFAULT_CONSUMPTION_VALUE
                firstTVA = (firstTotalHT * FIRST_TAV).round()
                secondTotalHT = ((consumption - GAZ_NONE_MENAGE_SECOND_TRANCHE) *
                    GAZ_NONE_MENAGE_THIRD_TRANCHE_PRICE).round() +
                    GAZ_NONE_MENAGE_FULL_SECOND_TRANCHE_PRICE
                secondTVA = (secondTotalHT * SECOND_TAV).round()
                CalculationHolder(
                    firstTotalHT = firstTotalHT,
                    firstTotalTTC = firstTotalHT + firstTVA,
                    firstTVA = firstTVA,
                    secondTotalHT = secondTotalHT,
                    secondTotalTTC = secondTotalHT + secondTVA,
                    secondTVA = secondTVA,
                    totalHT = firstTotalHT + secondTotalHT,
                    totalTVA = firstTVA + secondTVA,
                )
            }
            
            // 2500-1000
            consumption <= GAZ_NONE_MENAGE_SECOND_TRANCHE && consumption > GAZ_NONE_MENAGE_FIRST_TRANCHE -> {
                firstTotalHT =
                    GAZ_NONE_MENAGE_FULL_FIRST_TRANCHE_PRICE + GAZ_DEFAULT_CONSUMPTION_VALUE
                firstTVA = (firstTotalHT * FIRST_TAV).round()
                secondTotalHT = ((consumption - GAZ_NONE_MENAGE_FIRST_TRANCHE) *
                    GAZ_NONE_MENAGE_SECOND_TRANCHE_PRICE).round()
                secondTVA = (secondTotalHT * SECOND_TAV).round()
                CalculationHolder(
                    firstTotalHT = firstTotalHT,
                    firstTotalTTC = firstTotalHT + firstTVA,
                    firstTVA = firstTVA,
                    secondTotalHT = secondTotalHT,
                    secondTotalTTC = secondTotalHT + secondTVA,
                    secondTVA = secondTVA,
                    totalHT = firstTotalHT + secondTotalHT,
                    totalTVA = firstTVA + secondTVA,
                )
            }
            
            // 2500-
            else -> {
                firstTotalHT = (consumption * GAZ_NONE_MENAGE_FIRST_TRANCHE_PRICE).round() +
                    GAZ_DEFAULT_CONSUMPTION_VALUE
                firstTVA = (firstTotalHT * FIRST_TAV).round()
                CalculationHolder(
                    firstTotalHT = firstTotalHT,
                    firstTotalTTC = firstTotalHT + firstTVA,
                    firstTVA = firstTVA,
                    totalHT = firstTotalHT,
                    totalTVA = firstTVA,
                )
            }
        }
    }
    
    private fun extractPreviousBillData(
        dataSource: List<String>,
        mainBill: Bill,
        electOldMeterValue: Int,
        gazOldMeterValue: Int,
        menageType: MenageType,
        electricityPMD: ElectricityPMD,
        gazPCS: BigDecimal,
        isPaid: Boolean,
        onDone: (BigDecimal) -> Unit
    ): Bill {
        val mutableList: MutableList<String> = dataSource.toMutableList()
        @Suppress("NAME_SHADOWING") var gazPCS = gazPCS
        
        val electConsumption = mutableList[0].substringAfterLast(" ").toInt()
        val electConsumptionCost = mutableList.removeFirst().substringBeforeLast(" ")
            .replace(",", ".").replace(" ", "").toBigDecimal()
        
        val previousFirstTrim = mutableList.removeFirst()
        
        var previousFirstInfo = mutableList.removeFirst()
        val billNumber = previousFirstInfo.substringBefore(" ")
        previousFirstInfo = previousFirstInfo.removeRange(0, billNumber.length + 1)
        
        val ebb = previousFirstInfo.takeLast(3)
        previousFirstInfo =
            previousFirstInfo.removeRange(previousFirstInfo.length - 4, previousFirstInfo.length)
        val ebp = previousFirstInfo.takeLast(3)
        previousFirstInfo =
            previousFirstInfo.removeRange(previousFirstInfo.length - 4, previousFirstInfo.length)
        
        val commaIndex = previousFirstInfo.indexOf(",")
        val totalTTCNoTimbre = previousFirstInfo.take(commaIndex + 3)
            .replace(",", ".").replace(" ", "").toBigDecimal()
        val totalTTC = previousFirstInfo.drop(commaIndex + 4)
            .replace(",", ".").replace(" ", "").toBigDecimal()
        
        // Remove year
        val trimester = previousFirstTrim.take(1)
        val year = if (trimester != "1") {
            mutableList.removeFirst()
        } else {
            previousFirstTrim.takeLast(4)
        }
        
        val gazConsumption = mutableList[0].substringAfterLast(" ").toInt()
        val gazConsumptionCost = mutableList.removeFirst().substringBeforeLast(" ")
            .replace(",", ".").replace(" ", "").toBigDecimal()
        
        val rightsAndTaxes = rightsAndTaxesPreviousTrimester(
            menageType = menageType,
            electConsumption = electConsumption
        )
        
        if (gazPCS == "0".toBigDecimal()) {
            gazPCS = extractGazPCS(
                consumptionCubicM = gazConsumption.toBigDecimal(),
                consumptionCostMinusDefault = gazConsumptionCost - GAZ_DEFAULT_CONSUMPTION_VALUE,
                menageType = menageType,
            )
        }
        
        val electricity = calculateElectricity(
            consumption = electConsumption.toBigDecimal(),
            menageType = menageType,
            defaultValue = electricityPMD.price,
        )
        val gaz = calculateGaz(
            consumptionTH = gazConsumption.toBigDecimal() * gazPCS,
            menageType = menageType,
        )
        val stateSupport =
            ((electricity.totalHT + electricity.totalTVA + gaz.totalHT + gaz.totalTVA +
                rightsAndTaxes.toBigDecimal()) - totalTTCNoTimbre).let {
                    if (it <= "0".toBigDecimal()) "0.00".toBigDecimal() else it
            }
        
        
        onDone(gazPCS)
        // Calculate the float on consumption: n*UnitPrice = XX.xxxx (Clip to only 2 digits) = XX.xx
        // Calculate the float on the rest: n*i = XX.xxxx (Roundup to 2 digits) XX.xy
        return Bill(
            reference = mainBill.reference,
            isPaid = isPaid,
            pdfByteArray = null,
            billNumber = billNumber,
            date = Constants.NO_VALUE,
            trimester = trimester,
            year = year,
            ebp = ebp,
            ebb = ebb,
            electricityMeterNumber = mainBill.electricityMeterNumber,
            electNewValue = electOldMeterValue,
            electOldValue = electOldMeterValue - electConsumption,
            electConsumption = electConsumption.toBigDecimal(),
            electConsumptionCost = electConsumptionCost,
            gazMeterNumber = mainBill.gazMeterNumber,
            gazNewValue = gazOldMeterValue,
            gazOldValue = gazOldMeterValue - gazConsumption,
            gazConsumption = gazConsumption.toBigDecimal(),
            gazConsumptionCost = gazConsumptionCost,
            stateSupport = stateSupport,
            rightsAndTaxes = rightsAndTaxes,
            totalHT = electricity.totalHT + gaz.totalHT,
            electricityTva = electricity.totalTVA,
            gazTva = gaz.totalTVA,
            totalTva = electricity.totalTVA + gaz.totalTVA,
            totalTTCNoTimbre = totalTTCNoTimbre,
            timbre = totalTTC - totalTTCNoTimbre,
            totalTTC = totalTTC
        )
    }
    
    fun rightsAndTaxesPreviousTrimester(
        menageType: MenageType,
        electConsumption: Int
    ): Int {
        val rights = when {
            menageType in listOf(MenageType.NM, MenageType.NM_OUT_CITY) -> 0
            electConsumption in RIGHTS_FIRST_TRANCHE..<RIGHTS_SECOND_TRANCHE -> RIGHTS_FIRST_TRANCHE_PRICE
            electConsumption in RIGHTS_SECOND_TRANCHE..<RIGHTS_THIRD_TRANCHE -> RIGHTS_SECOND_TRANCHE_PRICE
            electConsumption > RIGHTS_THIRD_TRANCHE -> RIGHTS_THIRD_TRANCHE_PRICE
            else -> 0
        }
        return rights + menageType.price
    }
    
}
