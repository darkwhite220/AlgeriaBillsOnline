package earth.core.data.util

import android.util.Log
import earth.core.network.Constants
import earth.core.networkmodel.SignInData
import earth.core.throwablemodel.ConvertingPdfThrowable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

private const val TAG = "SignInUtil"

object SignInUtil {
    
    // TODO ADD TESTS
    fun extractSignInPageData(response: String): SignInData {
        val result: SignInData
        try {
            result = extractData(response)
        } catch (e: Exception) {
            println("SignInUtil.extractSignInPageData $e")
            throw ConvertingPdfThrowable.UnhandledSignInResponse(response)
        }
        return result
    }
    
    private fun extractData(response: String): SignInData {
        val document: Document = Jsoup.parse(response, "UTF-8")
        
        // Extract data based on labels
        val fullName =
            document.select("td.champs_form:contains(Nom et Prénom) + td span.res_form")
                .text().trim()
        val address =
            document.select("td.champs_form:contains(Lieu de consommation) + td span.res_form")
                .text().trim()
        
        Log.d(TAG, "extractData: Name: $fullName")
        Log.d(TAG, "extractData: Address: $address")
        
        // Select elements based on the structure and classes. Adjust if necessary.
//        val update = document.select("p.orange").first()?.text()
        val billNumber =
            document.select("tr.tr_pair > td").first()?.text()?.trim() ?: Constants.DEFAULT_VALUE
        val date = document.select("tr.tr_pair > td").eq(1).text().trim()
        val trimestreData = document.select("tr.tr_pair > td").eq(2).text().trim()
        val trimestre = trimestreData.first().toString()
        val trimestreYear = trimestreData.takeLast(4)
        
        // Print extracted data
//        Log.d(TAG, update) // Should print: update date
        Log.d(TAG, billNumber) // Should print: 412231105103
        Log.d(TAG, date) // Should print: 2023-11-15
        Log.d(TAG, trimestreData) // Should print: 4ème Trimestre 2023
        Log.d(TAG, trimestre) // Should print: 4ème Trimestre 2023
        Log.d(TAG, trimestreYear) // Should print: 4ème Trimestre 2023
        
        // Assuming the part you are interested in is within an anchor <a> tag with onclick attribute containing "num_fac"
        val hrefValue = document.select("a[onclick*=num_fac]").first()?.attr("onclick") ?: ""
        Log.d(TAG, hrefValue)
        // Use regular expressions to extract the URL part you're interested in
        val regexPattern = """'([^']*)'""".toRegex()
        val matchResult = regexPattern.find(hrefValue)
        
        // Extract the actual URL part from the match result
        var mttTtc: String? = null
        val urlPart = matchResult?.groups?.get(1)?.value
        
        // Should print: fact.jsp?num_fac=412231105103&mtt_ttc=4849.780&filial=SDC
        Log.d(TAG, urlPart.toString())
        urlPart?.let {
            // Extracting the query parameters
            val queryParams = urlPart.substringAfter("?")
            val params = queryParams.split("&").associate {
                val (key, value) = it.split("=")
                key to value
            }
            
            // Getting the specific parameter values
            val numFac = params["num_fac"]
            mttTtc = params["mtt_ttc"]?.dropLast(1)
            val filial = params["filial"]
            Log.d(
                TAG,
                "extractData: num_fac: $numFac"
            ) // Should print: num_fac: 412231105103
            Log.d(TAG, "extractData: mtt_ttc: $mttTtc") // Should print: mtt_ttc: 4849.780
            Log.d(TAG, "extractData: filial: $filial") // Should print: filial: SDC}
        }
        
        return SignInData(
            fullName = fullName,
            address = address,
            billNumber = billNumber,
            date = date,
            trimester = trimestre,
            year = trimestreYear,
            amount = mttTtc,
            billUrl = urlPart,
        )
    }
}