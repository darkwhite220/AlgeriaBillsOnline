package earth.core.network

import android.util.Log
import earth.core.network.Constants.DEFAULT_VALUE
import earth.core.networkmodel.SignInResponse
import kotlin.random.Random
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object Utils {
    private const val TAG = "Network Utils"
    
    fun randomInt() = Random.nextInt(1, 30).toString()
    
    fun extractSignInPageData(response: String): SignInResponse {
        val document: Document = Jsoup.parse(response, "UTF-8")
        
        // Extract data based on labels
        val fullName =
            document.select("td.champs_form:contains(Nom et Prénom) + td span.res_form")
                .text().trim()
        val address =
            document.select("td.champs_form:contains(Lieu de consommation) + td span.res_form")
                .text().trim()
        
        Log.d(TAG, "extractSignInPageData: Name: $fullName")
        Log.d(TAG, "extractSignInPageData: Address: $address")
        
        // Select elements based on the structure and classes. Adjust if necessary.
//        val update = document.select("p.orange").first()?.text()
        val billNumber = document.select("tr.tr_pair > td").first()?.text()?.trim() ?: DEFAULT_VALUE
        val date = document.select("tr.tr_pair > td").eq(1).text().trim()
        val trimestreData = document.select("tr.tr_pair > td").eq(2).text().trim()
        val trimestre = trimestreData.first().toString()
        val trimestreYear = trimestreData.takeLast(4)
        
        // Print extracted data
//        println(update) // Should print: update date
        println(billNumber) // Should print: 412231105103
        println(date) // Should print: 2023-11-15
        println(trimestreData) // Should print: 4ème Trimestre 2023
        println(trimestre) // Should print: 4ème Trimestre 2023
        println(trimestreYear) // Should print: 4ème Trimestre 2023
        
        // Assuming the part you are interested in is within an anchor <a> tag with onclick attribute containing "num_fac"
        val hrefValue = document.select("a[onclick*=num_fac]").first()?.attr("onclick") ?: ""
        println(hrefValue)
        // Use regular expressions to extract the URL part you're interested in
        val regexPattern = """'([^']*)'""".toRegex()
        val matchResult = regexPattern.find(hrefValue)
        
        // Extract the actual URL part from the match result
        var mttTtc: String? = null
        val urlPart = matchResult?.groups?.get(1)?.value
        
        println(urlPart) // Should print: fact.jsp?num_fac=412231105103&mtt_ttc=4849.780&filial=SDC
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
                "extractSignInPageData: num_fac: $numFac"
            ) // Should print: num_fac: 412231105103
            Log.d(TAG, "extractSignInPageData: mtt_ttc: $mttTtc") // Should print: mtt_ttc: 4849.780
            Log.d(TAG, "extractSignInPageData: filial: $filial") // Should print: filial: SDC}
        }
        
        return SignInResponse(
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