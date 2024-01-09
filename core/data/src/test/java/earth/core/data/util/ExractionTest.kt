package earth.core.data.util

import earth.core.data.util.Constants.GAZ_DEFAULT_CONSUMPTION_VALUE
import earth.core.data.util.DataAssets.data
import earth.core.data.util.DataAssets.dataStateSupport
import earth.core.data.util.DataAssets.shortData
import earth.core.data.util.ExtractionUtil.calculateElectricity
import earth.core.data.util.ExtractionUtil.calculateGaz
import earth.core.data.util.ExtractionUtil.extractElectricityPMD
import earth.core.data.util.ExtractionUtil.extractGazPCS
import earth.core.data.util.ExtractionUtil.extractPreviousBillsData
import earth.core.data.util.ExtractionUtil.menageType
import earth.core.data.util.ExtractionUtil.rightsAndTaxesPreviousTrimester
import earth.core.database.Bill
import java.math.BigDecimal
import org.junit.Assert
import org.junit.Test

class ExtractionUtilTest {
    
    @Test
    fun test() {
        Assert.assertEquals("493.11".toBigDecimal(), "493.1102".toBigDecimal().round())
        Assert.assertEquals("1044.72".toBigDecimal(), "1044.725".toBigDecimal().round())
    }
    
    @Test
    fun extractMainBillDataCorrect() {
        val bill = ExtractionUtil.extractMainBillData(
            dataSource = data,
            pdfByteArray = "".toByteArray(),
            isPaid = false,
            onDone = { _, _, _, _, _ ->
                println("Done")
            }
        )
        println(bill)
        Assert.assertEquals(
            Bill(
                reference = "172005704674134",
                isPaid = false,
                pdfByteArray = "".toByteArray(),
                billNumber = "459231108543",
                date = "29.11.23",
                trimester = "4",
                year = "2023",
                ebp = "135",
                ebb = "475",
                electricityMeterNumber = "348643",
                electNewValue = 32734,
                electOldValue = 32734,
                electConsumption = BigDecimal("0"),
                electConsumptionCost = BigDecimal("78.66"),
                gazMeterNumber = "140335",
                gazNewValue = 11319,
                gazOldValue = 11319,
                gazConsumption = BigDecimal("0"),
                gazConsumptionCost = BigDecimal("85.50"),
                stateSupport = BigDecimal("0"),
                rightsAndTaxes = 150,
                totalHT = BigDecimal("314.16"),
                electricityTva = BigDecimal("7.08"),
                gazTva = BigDecimal("7.70"),
                totalTva = BigDecimal("14.78"),
                totalTTCNoTimbre = BigDecimal("328.94"),
                timbre = BigDecimal("5.00"),
                totalTTC = BigDecimal("333.94")
            ),
            bill
        )
    }
    
    @Test
    fun extractMainBillWithStateSupportDataCorrect() {
        val bill = ExtractionUtil.extractMainBillData(
            dataSource = dataStateSupport,
            pdfByteArray = "".toByteArray(),
            isPaid = false,
            onDone = { _, _, _, _, _ ->
                println("Done")
            }
        )
        println(bill)
        Assert.assertEquals(
            Bill(
                reference = "172005704674134",
                isPaid = false,
                pdfByteArray = "".toByteArray(),
                billNumber = "459230207863",
                date = "28.02.23",
                trimester = "1",
                year = "2023",
                ebp = "175",
                ebb = "619",
                electricityMeterNumber = "348643",
                electNewValue = 32338,
                electOldValue = 31970,
                electConsumption = BigDecimal("368"),
                electConsumptionCost = BigDecimal("1391.18"),
                gazMeterNumber = "140335",
                gazNewValue = 11094,
                gazOldValue = 10648,
                gazConsumption = BigDecimal("446"),
                gazConsumptionCost = BigDecimal("1333.90"),
                stateSupport = BigDecimal("657.96"),
                rightsAndTaxes = 200,
                totalHT = BigDecimal("2267.12"),
                electricityTva = BigDecimal("181.99"),
                gazTva = BigDecimal("181.35"),
                totalTva = BigDecimal("363.35"),
                totalTTCNoTimbre = BigDecimal("2630.47"),
                timbre = BigDecimal("27.00"),
                totalTTC = BigDecimal("2657.47")
            ),
            bill
        )
    }
    
    @Test
    fun previousBillsCalculationsCorrect() {
        testOne()
        testTwo()
        testThree()
        testFour()
        testFive()
    }
    
    private fun testOne() {
        val electricity = calculateElectricity(
            consumption = "35".toBigDecimal(),
            menageType = MenageType.M,
            defaultValue = ElectricityPMD.MEDIUM_MONO_PHASE.price,
        )
        println("Electricity $electricity")
        val gaz = calculateGaz(
            consumptionTH = "22".toBigDecimal() * "9.02".toBigDecimal(),
            menageType = MenageType.M,
        )
        println("gaz $gaz")
        
        val taxAndRights = rightsAndTaxesPreviousTrimester(
            menageType = MenageType.M,
            electConsumption = 35
        )
        val stateSupport = "433.17".toBigDecimal() -
            (electricity.totalHT + electricity.totalTVA + gaz.totalHT + gaz.totalTVA +
                taxAndRights.toBigDecimal())
        
        println("StateSupport $stateSupport")
    }
    
    private fun testTwo() {
        val electricity = calculateElectricity(
            consumption = "361".toBigDecimal(),
            menageType = MenageType.M,
            defaultValue = ElectricityPMD.MEDIUM_MONO_PHASE.price,
        )
        println("Electricity $electricity")
        val gaz = calculateGaz(
            consumptionTH = "203".toBigDecimal() * "9.02".toBigDecimal(),
            menageType = MenageType.M,
        )
        println("gaz $gaz")
        
        val taxAndRights = rightsAndTaxesPreviousTrimester(
            menageType = MenageType.M,
            electConsumption = 361
        )
        val stateSupport = "1645.55".toBigDecimal() -
            (electricity.totalHT + electricity.totalTVA + gaz.totalHT + gaz.totalTVA +
                taxAndRights.toBigDecimal())
        println("StateSupport $stateSupport")
    }
    
    private fun testThree() {
        val electricity = calculateElectricity(
            consumption = "368".toBigDecimal(),
            menageType = MenageType.M,
            defaultValue = ElectricityPMD.MEDIUM_MONO_PHASE.price,
        )
        println("Electricity $electricity")
        val gaz = calculateGaz(
            consumptionTH = "446".toBigDecimal() * "9.02".toBigDecimal(),
            menageType = MenageType.M,
        )
        println("gaz $gaz")
        
        val taxAndRights = rightsAndTaxesPreviousTrimester(
            menageType = MenageType.M,
            electConsumption = 368
        )
        val stateSupport = "2630.47".toBigDecimal() -
            (electricity.totalHT + electricity.totalTVA + gaz.totalHT + gaz.totalTVA +
                taxAndRights.toBigDecimal())
        
        println("StateSupport $stateSupport")
    }
    
    private fun testFour() {
        val electricity = calculateElectricity(
            consumption = "2218".toBigDecimal(),
            menageType = MenageType.M,
            defaultValue = ElectricityPMD.MEDIUM_MONO_PHASE.price,
        )
        println("Electricity $electricity")
        val gaz = calculateGaz(
            consumptionTH = "224".toBigDecimal() * "9.02".toBigDecimal(),
            menageType = MenageType.M,
        )
        println("gaz $gaz")
        
        val taxAndRights = rightsAndTaxesPreviousTrimester(
            menageType = MenageType.M,
            electConsumption = 2218
        )
        val stateSupport = "13321.41".toBigDecimal() -
            (electricity.totalHT + electricity.totalTVA + gaz.totalHT + gaz.totalTVA +
                taxAndRights.toBigDecimal())
        
        println("StateSupport $stateSupport")
    }
    
    private fun testFive() {
        val electricity = calculateElectricity(
            consumption = "861".toBigDecimal(),
            menageType = MenageType.M,
            defaultValue = ElectricityPMD.MEDIUM_MONO_PHASE.price,
        )
        println("Electricity $electricity")
        
        val gazPCS = extractGazPCS(
            consumptionCubicM = "62".toBigDecimal(),
            consumptionCostMinusDefault = "186.76".toBigDecimal() - GAZ_DEFAULT_CONSUMPTION_VALUE,
            menageType = MenageType.M
        )
        println("GazPCS $gazPCS")
        val gaz = calculateGaz(
            consumptionTH = "62".toBigDecimal() * gazPCS,
            menageType = MenageType.M,
        )
        println("gaz $gaz")
        
        val taxAndRights = rightsAndTaxesPreviousTrimester(
            menageType = MenageType.M,
            electConsumption = 861
        )
        val stateSupport = "4849.78".toBigDecimal() -
            (electricity.totalHT + electricity.totalTVA + gaz.totalHT + gaz.totalTVA +
                taxAndRights.toBigDecimal())
        
        println("StateSupport $stateSupport")
    }
    
    @Test
    fun menageTypeCorrect() {
        Assert.assertEquals(
            MenageType.M, menageType(
                electConsumption = 368,
                previousRightsAndTaxes = 200
            )
        )
        Assert.assertEquals(
            MenageType.M_OUT_CITY, menageType(
                electConsumption = 368,
                previousRightsAndTaxes = 125
            )
        )
        Assert.assertEquals(
            MenageType.NM, menageType(
                electConsumption = 128,
                previousRightsAndTaxes = 600
            )
        )
        Assert.assertEquals(
            MenageType.NM, menageType(
                electConsumption = 400,
                previousRightsAndTaxes = 700
            )
        )
        Assert.assertEquals(
            MenageType.NM_OUT_CITY, menageType(
                electConsumption = 400,
                previousRightsAndTaxes = 400
            )
        )
    }
    
    @Test
    fun extractElectricityPMDCorrect() {
        Assert.assertEquals(
            ElectricityPMD.MEDIUM_MONO_PHASE,
            extractElectricityPMD(
                consumption = "368".toBigDecimal(),
                consumptionCost = "1391.18".toBigDecimal(),
                menageType = MenageType.M
            )
        )
        Assert.assertEquals(
            ElectricityPMD.MEDIUM_MONO_PHASE,
            extractElectricityPMD(
                consumption = "128".toBigDecimal(),
                consumptionCost = "613.56".toBigDecimal(),
                menageType = MenageType.M
            )
        )
        Assert.assertEquals(
            ElectricityPMD.MEDIUM_MONO_PHASE,
            extractElectricityPMD(
                consumption = "349".toBigDecimal(),
                consumptionCost = "1299.75".toBigDecimal(),
                menageType = MenageType.M
            )
        )
        Assert.assertEquals(
            ElectricityPMD.MEDIUM_MONO_PHASE,
            extractElectricityPMD(
                consumption = "546".toBigDecimal(),
                consumptionCost = "2247.71".toBigDecimal(),
                menageType = MenageType.M
            )
        )
        Assert.assertEquals(
            ElectricityPMD.MEDIUM_MONO_PHASE,
            extractElectricityPMD(
                consumption = "122".toBigDecimal(),
                consumptionCost = "588.49".toBigDecimal(),
                menageType = MenageType.NM
            )
        )
    }
    
    @Test
    fun calculateElectricityCorrect() {
        Assert.assertEquals(
            CalculationHolder(
                firstTotalHT = "78.66".toBigDecimal(),
                firstTotalTTC = "85.74".toBigDecimal(),
                firstTVA = "7.08".toBigDecimal(),
                totalHT = "78.66".toBigDecimal(),
                totalTVA = "7.08".toBigDecimal()
            ),
            calculateElectricity(
                consumption = "0".toBigDecimal(),
                menageType = MenageType.M,
                defaultValue = "78.66".toBigDecimal(),
            )
        )
        Assert.assertEquals(
            CalculationHolder(
                firstTotalHT = "823.36".toBigDecimal(),
                firstTotalTTC = "897.46".toBigDecimal(),
                firstTVA = "74.10".toBigDecimal(),
                secondTotalHT = "567.82".toBigDecimal(),
                secondTotalTTC = "675.71".toBigDecimal(),
                secondTVA = "107.89".toBigDecimal(),
                totalHT = "1391.18".toBigDecimal(),
                totalTVA = "181.99".toBigDecimal()
            ),
            calculateElectricity(
                consumption = "368".toBigDecimal(),
                menageType = MenageType.M,
                defaultValue = "78.66".toBigDecimal(),
            )
        )
        Assert.assertEquals(
            CalculationHolder(
                firstTotalHT = "823.36".toBigDecimal(),
                firstTotalTTC = "897.46".toBigDecimal(),
                firstTVA = "74.10".toBigDecimal(),
                secondTotalHT = "2940.13".toBigDecimal(),
                secondTotalTTC = "3498.75".toBigDecimal(),
                secondTVA = "558.62".toBigDecimal(),
                totalHT = "3763.49".toBigDecimal(),
                totalTVA = "632.72".toBigDecimal()
            ),
            calculateElectricity(
                consumption = "861".toBigDecimal(),
                menageType = MenageType.M,
                defaultValue = "78.66".toBigDecimal(),
            )
        )
        Assert.assertEquals(
            CalculationHolder(
                firstTotalHT = "823.36".toBigDecimal(),
                firstTotalTTC = "897.46".toBigDecimal(),
                firstTVA = "74.10".toBigDecimal(),
                secondTotalHT = "1424.35".toBigDecimal(),
                secondTotalTTC = "1694.98".toBigDecimal(),
                secondTVA = "270.63".toBigDecimal(),
                totalHT = "2247.71".toBigDecimal(),
                totalTVA = "344.73".toBigDecimal()
            ),
            calculateElectricity(
                consumption = "546".toBigDecimal(),
                menageType = MenageType.M,
                defaultValue = "78.66".toBigDecimal(),
            )
        )
        
        // None Menage
        Assert.assertEquals(
            CalculationHolder(
                firstTotalHT = "571.77".toBigDecimal(),
                firstTotalTTC = "623.23".toBigDecimal(),
                firstTVA = "51.46".toBigDecimal(),
                totalHT = "571.77".toBigDecimal(),
                totalTVA = "51.46".toBigDecimal()
            ),
            calculateElectricity(
                consumption = "118".toBigDecimal(),
                menageType = MenageType.NM,
                defaultValue = "78.66".toBigDecimal(),
            )
        )
        Assert.assertEquals(
            CalculationHolder(
                firstTotalHT = "613.56".toBigDecimal(),
                firstTotalTTC = "668.78".toBigDecimal(),
                firstTVA = "55.22".toBigDecimal(),
                totalHT = "613.56".toBigDecimal(),
                totalTVA = "55.22".toBigDecimal()
            ),
            calculateElectricity(
                consumption = "128".toBigDecimal(),
                menageType = MenageType.NM,
                defaultValue = "78.66".toBigDecimal(),
            )
        )
    }
    
    @Test
    fun extractGazPCSCorrect() {
        Assert.assertEquals(
            "8.80".toBigDecimal(),
            extractGazPCS(
                consumptionCubicM = "69".toBigDecimal(),
                consumptionCostMinusDefault = "197.04".toBigDecimal(),
                menageType = MenageType.NM,
            )
        )
        Assert.assertEquals(
            "9.02".toBigDecimal(),
            extractGazPCS(
                consumptionCubicM = "544".toBigDecimal(),
                consumptionCostMinusDefault = "1604.19".toBigDecimal(),
                menageType = MenageType.M,
            )
        )
        Assert.assertEquals(
            "9.71".toBigDecimal(),
            extractGazPCS(
                consumptionCubicM = "62".toBigDecimal(),
                consumptionCostMinusDefault = "186.76".toBigDecimal() - GAZ_DEFAULT_CONSUMPTION_VALUE,
                menageType = MenageType.M,
            )
        )
        Assert.assertEquals(
            "8.80".toBigDecimal(),
            extractGazPCS(
                consumptionCubicM = "122".toBigDecimal(),
                consumptionCostMinusDefault = "266.08".toBigDecimal() - GAZ_DEFAULT_CONSUMPTION_VALUE,
                menageType = MenageType.M,
            )
        )
    }
    
    @Test
    fun calculateGazCorrect() {
        Assert.assertEquals(
            CalculationHolder(
                firstTotalHT = "720.92".toBigDecimal(),
                firstTotalTTC = "785.80".toBigDecimal(),
                firstTVA = "64.88".toBigDecimal(),
                secondTotalHT = "968.77".toBigDecimal(),
                secondTotalTTC = "1152.84".toBigDecimal(),
                secondTVA = "184.07".toBigDecimal(),
                totalHT = "1689.69".toBigDecimal(),
                totalTVA = "248.95".toBigDecimal()
            ),
            calculateGaz(
                consumptionTH = "4906.88".toBigDecimal(),
                menageType = MenageType.M
            )
        )
        Assert.assertEquals(
            CalculationHolder(
                firstTotalHT = "186.76".toBigDecimal(),
                firstTotalTTC = "203.57".toBigDecimal(),
                firstTVA = "16.81".toBigDecimal(),
                totalHT = "186.76".toBigDecimal(),
                totalTVA = "16.81".toBigDecimal()
            ),
            calculateGaz(
                consumptionTH = "602.02".toBigDecimal(),
                menageType = MenageType.M
            )
        )
        Assert.assertEquals(
            CalculationHolder(
                firstTotalHT = "282.54".toBigDecimal(),
                firstTotalTTC = "307.97".toBigDecimal(),
                firstTVA = "25.43".toBigDecimal(),
                totalHT = "282.54".toBigDecimal(),
                totalTVA = "25.43".toBigDecimal()
            ),
            calculateGaz(
                consumptionTH = "607.20".toBigDecimal(),
                menageType = MenageType.NM
            )
        )
    }
    
    @Test
    fun extractPreviousBillsDataCorrect() {
        val bills = extractPreviousBillsData(
            dataSource = shortData,
            mainBill = Bill(
                reference = "172005704674134",
                isPaid = false,
                pdfByteArray = "".toByteArray(),
                billNumber = "459231108543",
                date = "29.11.23",
                trimester = "4",
                year = "2023",
                ebp = "135",
                ebb = "475",
                electricityMeterNumber = "348643",
                electNewValue = 32734,
                electOldValue = 32734,
                electConsumption = BigDecimal("0"),
                electConsumptionCost = BigDecimal("78.66"),
                gazMeterNumber = "140335",
                gazNewValue = 11319,
                gazOldValue = 11319,
                gazConsumption = BigDecimal("0"),
                gazConsumptionCost = BigDecimal("85.50"),
                stateSupport = BigDecimal("0"),
                rightsAndTaxes = 150,
                totalHT = BigDecimal("314.16"),
                electricityTva = BigDecimal("7.08"),
                gazTva = BigDecimal("7.70"),
                totalTva = BigDecimal("14.78"),
                totalTTCNoTimbre = BigDecimal("328.94"),
                timbre = BigDecimal("5.00"),
                totalTTC = BigDecimal("333.94")
            ),
            menageType = MenageType.M,
            electricityPMD = ElectricityPMD.MEDIUM_MONO_PHASE,
            gazPCS = "9.02".toBigDecimal(),
            isPaid = true,
        )
        println(bills)
    }
    
    @Test
    fun rightsAndTaxesPreviousTrimesterCorrect() {
        Assert.assertEquals(
            200,
            rightsAndTaxesPreviousTrimester(
                menageType = MenageType.M,
                electConsumption = 368
            )
        )
        Assert.assertEquals(
            250,
            rightsAndTaxesPreviousTrimester(
                menageType = MenageType.M,
                electConsumption = 681
            )
        )
        Assert.assertEquals(
            600,
            rightsAndTaxesPreviousTrimester(
                menageType = MenageType.NM,
                electConsumption = 118
            )
        )
    }
}

private object DataAssets {
    val data = listOf(
        "Direction Distribution : DIR DIST DJELFA 00799999000038025661",
        "Agence Commerciale : DJELFA DJADIDA CITE 05 JUILLET BT 00100654030030002427",
        "Référence : 172005704674134",
        "Nom et Prénom : MR CHABIRA BARKAHOUM",
        "Lieu de consommation : CITE 94 L BT 12 LOG N 70",
        "Facture N° : 459231108543",
        "Date facture: 29.11.23 Période : 4ème Trimestre 2023",
        "135",
        "348643 32734 32734 0 78,66",
        "475",
        "140335 11319 11319 0 85,50",
        "328,94",
        "Droits & Taxes: 5,00",
        "150,00",
        "Montant HT: 314,16",
        "14,78",
        "333,94",
        "140,91 35",
        "3ème Trimestre",
        "459230807405 433,17 438,17 154 544",
        "2023",
        "118,88 22",
        "1 357,49 361",
        "2ème Trimestre",
        "459230508493 1 645,55 1 662,55 228 796",
        "2023",
        "503,85 203",
        "1 391,18 368",
        "1er Trimestre 2023",
        "459230207863 2 630,47 2 657,47 175 619",
        "1 333,90 446",
        "1 299,75 349",
        "4ème Trimestre",
        "459221107525 1 581,34 1 597,34 112 394",
        "2022",
        "474,58 193"
    )
    val dataStateSupport = listOf(
        "Direction Distribution : DIR DIST DJELFA 00799999000038025661",
        "Agence Commerciale : DJELFA DJADIDA CITE 05 JUILLET BT 00100654030030002427",
        "Référence : 172005704674134",
        "Nom et Prénom : MR CHABIRA BARKAHOUM",
        "Lieu de consommation : CITE 94 L BT 12 LOG N 70",
        "Facture N° : 459230207863",
        "Date facture: 28.02.23 Période : 1er Trimestre 2023",
        "175",
        "348643 32338 31970 368 1391,18",
        "619",
        "140335 11094 10648 446 1333,90",
        "Soutien de l'état: 2630,47",
        "657,96",
        "Droits & Taxes: 27,00",
        "200,00",
        "Montant HT: 2267,12",
        "363,35",
        "2657,47",
        "1 299,75 349",
        "4ème Trimestre",
        "459221107525 1581,34 1597,34 112 394",
        "2022",
        "474,58 193",
        "1 699,14 432",
        "3ème Trimestre",
        "459220807318 1 754,14 1 772,14 193 673",
        "2022",
        "223,56 91",
        "11 106,51 2218",
        "2ème Trimestre",
        "459220507020 13 321,41 13 455,41 037 127",
        "2022",
        "565,31 224",
        "8 010,54 1653",
        "1er Trimestre 2022",
        "459220206971 10 419,61 10 524,61 152 538",
        "1 235,87 419"
    )
    val shortData = listOf(
        "140,91 35",
        "3ème Trimestre",
        "459230807405 433,17 438,17 154 544",
        "2023",
        "118,88 22",
        "1 357,49 361",
        "2ème Trimestre",
        "459230508493 1 645,55 1 662,55 228 796",
        "2023",
        "503,85 203",
        "1 391,18 368",
        "1er Trimestre 2023",
        "459230207863 2 630,47 2 657,47 175 619",
        "1 333,90 446",
        "1 299,75 349",
        "4ème Trimestre",
        "459221107525 1 581,34 1 597,34 112 394",
        "2022",
        "474,58 193"
    )
}