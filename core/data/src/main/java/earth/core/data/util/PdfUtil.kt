package earth.core.data.util

import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import java.io.ByteArrayInputStream
import java.io.FileOutputStream


object PdfUtil {
    fun extractDataFromByteArray(byteArray: ByteArray) {
//        val file = "/storage/emulated/0/Download/testDjelfaDiff.pdf"
//
//        try {
//            FileOutputStream("file").use { fos ->
//                fos.write(byteArray)
//                println("PDF File Saved")
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        var parsedText = ""
//        try {
//            val reader: PdfReader = PdfReader(file)
//            val n = reader.numberOfPages
//            for (i in 0 until n) {
//                parsedText = parsedText + PdfTextExtractor.getTextFromPage(reader, i + 1)
//                    .trim { it <= ' ' } + "\n" //Extracting the content from the different pages
//            }
//            println(parsedText)
//            reader.close()
//        } catch (e: java.lang.Exception) {
//            println(e)
//        }
        
        try {
            val inputStream = ByteArrayInputStream(byteArray)
            val reader = PdfReader(inputStream)
            val n = reader.numberOfPages
            var parsedText = ""
            for (i in 0 until n) {
                parsedText = parsedText + PdfTextExtractor
                    .getTextFromPage(reader, i + 1).trim { it <= ' ' } + "\n"
            }
            extract(parsedText)
            reader.close()
        } catch (e: Exception) {
            println("PdfUtil.extractDataFromByteArray: $e")
        }
    }
    
    private fun extract(parsedText: String) {
//        println(parsedText)
        val first = """
    Facture Internet
    N° RIP :
    Direction Distribution : DIR DIST DJELFA 00799999000038025661
    N° RIB :
    Agence Commerciale : DJELFA DJADIDA   CITE 05 JUILLET BT 00100654030030002427
    Référence : 172005704674134
    Nom et Prénom : MR CHABIRA BARKAHOUM
    Lieu de consommation : CITE 94 L BT 12   LOG N  70
    Facture N° : 459231108543
    Date facture: 29.11.23 Période : 4ème Trimestre 2023
    Numéro Consommation Montant energie
    Index Nouveau Index ancien
    compteur (kWh/TH)
    HT (DA)
    Clé EBP
    135
    348643 32734 32734 0 78,66
    Clé EBB
    475
    140335 11319 11319 0 85,50
    Total TTC (Sans Timbre)
    Paiement poste ou Chéque
    328,94
    Droit de timbre
    Droits & Taxes: 5,00
    150,00
    Montant HT: 314,16
    Total TTC
    Paiement en espèces
    14,78
    TVA :
    333,94
    Historique des factures :
    Consommation Montant (TTC) Clé EBP Clé EBB
    Numéro Facture Periode Montant (HT) Montant (TTC)
     (kwH/Th) Sans Timbre
    140,91 35
    3ème Trimestre
    459230807405 433,17 438,17 154 544
    2023
    118,88 22
    1 357,49 361
    2ème Trimestre
    459230508493 1 645,55 1 662,55 228 796
    2023
    503,85 203
    1 391,18 368
    1er Trimestre 2023
    459230207863 2 630,47 2 657,47 175 619
    1 333,90 446
    1 299,75 349
    4ème Trimestre
    459221107525 1 581,34 1 597,34 112 394
    2022
    474,58 193
""".trimIndent()
        val second = """
    Facture Internet
N° RIP :
Direction Distribution : DIR DIST DJELFA 00799999000038025661
N° RIB :
Agence Commerciale : DJELFA DJADIDA   CITE 05 JUILLET BT 00100654030030002427
Référence : 172005704674134
Nom et Prénom : MR CHABIRA BARKAHOUM
Lieu de consommation : CITE 94 L BT 12   LOG N  70
Facture N° : 459230207863
Date facture: 28.02.23 Période : 1er Trimestre 2023
Numéro Consommation Montant energie
Index Nouveau Index ancien
compteur (kWh/TH)
HT (DA)
Clé EBP
175
348643 32338 31970 368 1391,18
Clé EBB
619
140335 11094 10648 446 1333,90
Total TTC (Sans Timbre)
Paiement poste ou Chéque
Soutien de l'état: 2630,47
657,96
Droit de timbre
Droits & Taxes: 27,00
200,00
Montant HT: 2267,12
Total TTC
Paiement en espèces
363,35
TVA :
2657,47
Historique des factures :
Consommation Montant (TTC) Clé EBP Clé EBB
Numéro Facture Periode Montant (HT) Montant (TTC)
 (kwH/Th) Sans Timbre
1 299,75 349
4ème Trimestre
459221107525 1581,34 1597,34 112 394
2022
474,58 193
1 699,14 432
3ème Trimestre
459220807318 1 754,14 1 772,14 193 673
2022
223,56 91
11 106,51 2218
2ème Trimestre
459220507020 13 321,41 13 455,41 037 127
2022
565,31 224
8 010,54 1653
1er Trimestre 2022
459220206971 10 419,61 10 524,61 152 538
1 235,87 419
""".trimIndent()
        
        val change = parsedText.replace(" ", " ")
//        println(change)
        
        var temp: MutableList<String> = change.split("\n").toMutableList()
        
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
        temp.removeIf { it.trim() in removeList }
//        temp.forEach { println(it) }
        
        var direction = temp.removeFirst()
        direction =
            direction.removePrefix("Direction Distribution : ").substringBeforeLast(" ").trim()
                .replace(Regex("\\s+"), " ")
        println(direction)
        
        var agence = temp.removeFirst()
        agence = agence.removePrefix("Agence Commerciale : ").substringBeforeLast(" ").trim()
            .replace(Regex("\\s+"), " ")
        println(agence)
        
        temp = temp.subList(5, temp.size)
//        println(temp[0]) // must be 135 under the date
        
        val ebp = temp.removeFirst()
        println("EBP: $ebp")
        
        val electRow = temp.removeFirst().split(" ")
        val electCounterN = electRow[0]
        val electNewN = electRow[1]
        val electOldN = electRow[2]
        val electConsumption = electRow[3]
        val electPayAmount = electRow[4] // Min 78,66
        println("Electricity row: $electCounterN, $electNewN, $electOldN, $electConsumption, $electPayAmount")
        
        val ebb = temp.removeFirst()
        println("EBB: $ebb")
        
        val gazRow = temp.removeFirst().split(" ")
        val gazCounterN = gazRow[0]
        val gazNewN = gazRow[1]
        val gazOldN = gazRow[2]
        val gazConsumption = gazRow[3]
        val gazPayAmount = gazRow[4] // Min 85,50
        println("Gaz row: $gazCounterN, $gazNewN, $gazOldN, $gazConsumption, $gazPayAmount")
        
        var soutienDeta = ""
        var total = ""
        if (temp[0].contains("Soutien de l'état:")) {
            total = temp.removeFirst().removePrefix("Soutien de l'état: ")
            soutienDeta = temp.removeFirst()
            println("Soutien de l'état: $soutienDeta")
        } else {
            total = temp.removeFirst()
        }
        println("Total: $total")
        
        val timbre = temp.removeFirst().removePrefix("Droits & Taxes: ")
        println("Timbre: $timbre")
        
        val rightAndTax = temp.removeFirst()
        println("Droits & Taxes: $rightAndTax")
        
        val amountHT = temp.removeFirst().removePrefix("Montant HT: ")
        println("Montant HT: $amountHT")
        
        val tva = temp.removeFirst()
        println("TVA: $tva")
        
        val totalTTC = temp.removeFirst()
        println("Total TTC: $totalTTC")
        
        println("PREVIOUS BILLS DATA")
        
        val previousFirstTrimElectConsumption = temp[0].substringAfterLast(" ")
        println("previousFirstTrimElectConsumption $previousFirstTrimElectConsumption")
        val previousFirstTrimElectAmountTH = temp.removeFirst().substringBeforeLast(" ")
        println("previousFirstTrimElectAmountTH $previousFirstTrimElectAmountTH")
        
        val previousFirstTrim = temp.removeFirst()
        
        var previousFirstInfo = temp.removeFirst()
        val previousFirstTrimBillNumber = previousFirstInfo.substringBefore(" ")
        println("previousFirstTrimBillNumber $previousFirstTrimBillNumber")
        previousFirstInfo = previousFirstInfo.removeRange(0, previousFirstTrimBillNumber.length+1)
        
        val previousFirstTrimEBB = previousFirstInfo.takeLast(3)
        println("previousFirstTrimEBB $previousFirstTrimEBB")
        previousFirstInfo = previousFirstInfo.removeRange(previousFirstInfo.length-4,previousFirstInfo.length)
        val previousFirstTrimEBP = previousFirstInfo.takeLast(3)
        println("previousFirstTrimEBP $previousFirstTrimEBP")
        previousFirstInfo = previousFirstInfo.removeRange(previousFirstInfo.length-4, previousFirstInfo.length)
        
        val commaIndex = previousFirstInfo.indexOf(",")
        val previousFirstTrimTotal = previousFirstInfo.take(commaIndex + 3)
        println("previousFirstTrimTotal $previousFirstTrimTotal")
        val previousFirstTrimTotalTTC = previousFirstInfo.drop(commaIndex + 4)
        println("previousFirstTrimTotalTTC $previousFirstTrimTotalTTC")
        
        // Remove year
        if (!previousFirstTrim.startsWith("1")){
            temp.removeFirst()
        }
        
        val previousFirstTrimGazConsumption = temp[0].substringAfterLast(" ")
        println("previousFirstTrimGazConsumption $previousFirstTrimGazConsumption")
        val previousFirstTrimGazAmountTH = temp.removeFirst().substringBeforeLast(" ")
        println("previousFirstTrimElectAmountTH $previousFirstTrimGazAmountTH")
        
        // Second previous trimester
        
    }
}