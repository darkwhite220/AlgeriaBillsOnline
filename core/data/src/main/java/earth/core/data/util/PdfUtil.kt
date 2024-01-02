package earth.core.data.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import java.io.FileOutputStream
import java.util.*


object PdfUtil {
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun read(text: String = pdfFilePathDjelfaDiff()) {
////        val decodedBytes = Base64.getDecoder().decode(text)
//        val file = "/storage/emulated/0/Download/testDjelfaDiff.pdf"
//
//        try {
//            FileOutputStream(file).use { fos ->
//                val decoder: ByteArray = Base64.getDecoder().decode(text)
//                fos.write(decoder)
//                fos.close()
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
//        println("-----------")
//        val result = extractData(parsedText)
//        println(result)
//    }
}

private fun extractData(sourceData: String): String {
    // Extracting relevant information using regular expressions
    val directionDistribution = Regex("Direction Distribution : (.*?)\n").find(sourceData)?.groupValues?.get(1) ?: ""
    val agenceCommerciale = Regex("Agence Commerciale : (.*?)\n").find(sourceData)?.groupValues?.get(1) ?: ""
    
    // Extracting the consumption data
    val consumptionData = Regex("""\d+\s+(\d+)\s+(\d+)\s+(\d+)\s+(\d+)\s+([\d,]+\.\d+)""").findAll(sourceData)
    consumptionData.forEach {
        println("consumptionData $it")
    }
    val consumptionString = consumptionData.joinToString("\n")// { it.groupValues.drop(1).joinToString(" ") }
    
    // Extracting financial information
    val financialData = Regex("""\d+,\d+""").findAll(sourceData)
    val financialString = financialData.joinToString("\n")
    financialData.forEach {
        println("financialData ${it.value}")
    }
    
    // Extracting historical data
    val historicalData = Regex("""\d+,\d+\s+(\d+)\s+(\S+\sTrimestre)\s+(\d+)\s+(\d+)\s+(\d+)\s+(\d+)\s+(\d+,\d+)\s+(\d+,\d+)""").findAll(sourceData)
    val historicalString = historicalData.joinToString("\n")// { it.groupValues.drop(1).joinToString(" ") }
    historicalData.forEach {
        println("historicalData $it")
    }
    
    // Creating the extracted data string
    return "$directionDistribution\n$agenceCommerciale\n$consumptionString\n$financialString\n$historicalString\n"
}