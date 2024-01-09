package earth.core.data.util

import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import earth.core.throwablemodel.ConvertingPdfThrowable
import java.io.ByteArrayInputStream


object PdfUtil {
    
    fun extractDataFromByteArray(byteArray: ByteArray): List<String> {
        val dataAsListOfString: MutableList<String>
        
        try {
            val inputStream = ByteArrayInputStream(byteArray)
            val reader = PdfReader(inputStream)
            val parsedText = PdfTextExtractor.getTextFromPage(reader, 1).trim()
            
            val cleanedText: MutableList<String> = parsedText.cleanExtractedText()
            println("cleanedText: $cleanedText")
            initialCheck(cleanedText)
            
            dataAsListOfString = cleanedText.prepStringForMainExtraction()
            println("dataAsListOfString: $dataAsListOfString")
            
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
        // Remove last 6 lines
//IMPORTANT
//* Cette facture peut être utilisée pour le paiement.
//* Cette facture ne peut être utilisée comme pièce justificative pour les dossiers administratifs.
//* La société de distribution décline toute responsabilité quant à une utilisation frauduleuse de ce document.
//GAZ ELEC GAZ ELEC GAZ ELEC GAZ ELEC
//GAZ ELEC
        return dataList.subList(0, dataList.size - 6)
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
                throw ConvertingPdfThrowable.UnhandledPdfFormat(cleanedText)
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
                throw ConvertingPdfThrowable.BadPdfFormat(cleanedText)
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
                throw ConvertingPdfThrowable.BadPdfFormatWithStateSupport(cleanedText)
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

