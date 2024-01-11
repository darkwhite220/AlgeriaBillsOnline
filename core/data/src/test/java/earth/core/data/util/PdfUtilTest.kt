package earth.core.data.util

import earth.core.data.util.InitialAssets.parsedText
import earth.core.data.util.InitialAssets.parsedTextStateSupport
import earth.core.data.util.PdfUtil.cleanExtractedText
import earth.core.data.util.PdfUtil.initialCheck
import earth.core.data.util.PdfUtil.prepStringForMainExtraction
import org.junit.jupiter.api.Test

class PdfUtilTest {
    
    @Test
    fun initialCheckCorrect() {
        val cleanedText: MutableList<String> = parsedText.cleanExtractedText()
        initialCheck(cleanedText)
        println(
            cleanedText
                .prepStringForMainExtraction()
                .joinToString(", ", prefix = "[", postfix = "]", transform = { "\"$it\"" })
        )
    }
    
    @Test
    fun initialCheckStateSupportCorrect() {
        val cleanedText: MutableList<String> = parsedTextStateSupport.cleanExtractedText()
        initialCheck(cleanedText)
        println(
            cleanedText
                .prepStringForMainExtraction()
                .joinToString(", ", prefix = "[", postfix = "]", transform = { "\"$it\"" })
        )
    }
    
    
}

object InitialAssets {
    
    val parsedText = """
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
    val parsedTextStateSupport = """
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
    
}