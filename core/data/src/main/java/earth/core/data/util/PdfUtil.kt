package earth.core.data.util

import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import java.io.ByteArrayInputStream


object PdfUtil {
    
    fun extractDataFromByteArray(byteArray: ByteArray): List<String> {
        val dataAsListOfString: MutableList<String>
        
        try {
            val inputStream = ByteArrayInputStream(byteArray)
            val reader = PdfReader(inputStream)
            val parsedText = PdfTextExtractor.getTextFromPage(reader, 1).trim()
            
            val cleanedText: MutableList<String> = parsedText.cleanExtractedText()
            initialCheck(cleanedText)
            
            dataAsListOfString = cleanedText.prepStringForMainExtraction()
            
            /*billsList.add(extractMainBillData(dataAsListOfString, byteArray))
            dataAsListOfString =
                dataAsListOfString.subList(dataAsListOfString.size - 19, dataAsListOfString.size)
            println(dataAsListOfString[0])
            
            val menageType = menageType(
                electConsumption = billsList[0].electConsumption.toInt(),
                previousRightsAndTaxes = billsList[0].rightsAndTaxes,
            )
            Log.d(TAG, "extractDataFromByteArray: $menageType")
            
            val electricityPMD = extractElectricityPMD(
                consumption = billsList[0].electConsumption,
                consumptionCost = billsList[0].electConsumptionCost,
                menageType = menageType,
            )
            Log.d(TAG, "extractDataFromByteArray: $electricityPMD")
            
            val gazPCS = extractGazPCS(
                consumptionCubicM = billsList[0].gazConsumption,
                consumptionCostMinusDefault = billsList[0].gazConsumptionCost - GAZ_DEFAULT_CONSUMPTION_VALUE,
                menageType = menageType,
            )
            
            val calculateGaz = calculateGaz(
                consumptionTH = (billsList[0].gazConsumption * gazPCS).round(),
                menageType = menageType,
            )
            
            (0 until 4).forEach { index ->
                Log.d(TAG, "extractDataFromByteArray: $index extractPreviousBillsData")
                billsList.add(
                    extractPreviousBillsData(
                        dataList = dataAsListOfString,
                        pdfByteArray = byteArray,
                        reference = billsList[0].reference,
                        electricityMeterNumber = billsList[0].electricityMeterNumber,
                        electOldValue = billsList[0].electOldValue,
                        gazMeterNumber = billsList[0].gazMeterNumber,
                        gazOldValue = billsList[0].gazOldValue,
                        mainBillStateSupport = billsList[0].stateSupport,
                        rightsAndTaxes = billsList[0].rightsAndTaxes,
                        menageType = menageType,
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
            }*/
            
            reader.close()
        } catch (e: Exception) {
            println("PdfUtil.extractDataFromByteArray: $e")
            throw e
        }
        
        return dataAsListOfString
    }
    
    fun String.cleanExtractedText(): MutableList<String> {
        val change = this.replace(" ", " ")
        val dataList: MutableList<String> = change.split("\n").toMutableList()
        for (index in dataList.indices) {
            dataList[index] = dataList[index].replace(Regex("\\s+"), " ").trim()
        }
        return dataList
    }
    
    fun initialCheck(cleanedText: List<String>) {
        when (cleanedText.size) {
            55 -> {
                println("initialCheck: 55 LINES")
                checkMatchingLines(cleanedText)
            }
            56 -> {
                println("initialCheck: 56 LINES")
                checkMatchingLinesStateSupport(cleanedText)
            }
            else -> {
                throw java.lang.Exception("Wrong formatted text")
            }
        }
    }
    
    private fun checkMatchingLines(
        cleanedText: List<String>
    ) {
        val initialCheckRegexStrings = listOf(
            "Total TTC \\(Sans Timbre\\)",
            "Paiement poste ou Chéque",
            "(\\d+,\\d{2})",
            "Droit de timbre",
            "Droits & Taxes: (\\d+,\\d{2})",
            "(\\d+,\\d{2})",
            "Montant HT: (\\d+,\\d{2})",
            "Total TTC",
            "Paiement en espèces",
            "(\\d+,\\d{2})",
            "TVA :",
            "(\\d+,\\d{2})"
        )
        
        initialCheckRegexStrings.forEachIndexed { index, regexString ->
            if (!Regex(regexString).matches(cleanedText.getOrNull(20 + index) ?: ""))
                throw java.lang.Exception("None matching format (55)")
        }
    }
    
    private fun checkMatchingLinesStateSupport(
        cleanedText: List<String>
    ) {
        val initialCheckRegexStrings = listOf(
            "Total TTC \\(Sans Timbre\\)",
            "Paiement poste ou Chéque",
            "Soutien de l'état: (\\d+,\\d{2})",
            "(\\d+,\\d{2})",
            "Droit de timbre",
            "Droits & Taxes: (\\d+,\\d{2})",
            "(\\d+,\\d{2})",
            "Montant HT: (\\d+,\\d{2})",
            "Total TTC",
            "Paiement en espèces",
            "(\\d+,\\d{2})",
            "TVA :",
            "(\\d+,\\d{2})"
        )
        
        initialCheckRegexStrings.forEachIndexed { index, regexString ->
            if (!Regex(regexString).matches(cleanedText.getOrNull(20 + index) ?: ""))
                throw java.lang.Exception("None matching format (56)")
        }
    }
    
    fun MutableList<String>.prepStringForMainExtraction(): MutableList<String> {
        val removeList = listOf(
            "Facture Internet",
            "N° RIP :",
            "N° RIB :",
            "Numéro Consommation Montant energie",
            "Index Nouveau Index ancien",
            "compteur (kWh/TH)",
            "HT (DA)",
            "Clé EBP",
            "Clé EBB",
            "Total TTC (Sans Timbre)",
            "Paiement poste ou Chéque",
            "Droit de timbre",
            "Total TTC",
            "Paiement en espèces",
            "TVA :",
            "Historique des factures :",
            "Consommation Montant (TTC) Clé EBP Clé EBB",
            "Numéro Facture Periode Montant (HT) Montant (TTC)",
            "(kwH/Th) Sans Timbre"
        )
        this.removeIf { it in removeList }
        return this
    }
    
}

