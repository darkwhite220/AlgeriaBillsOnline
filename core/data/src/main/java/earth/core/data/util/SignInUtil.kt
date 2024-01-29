package earth.core.data.util

import earth.core.networkmodel.SignInData
import earth.core.networkmodel.SignInResponse
import earth.core.throwablemodel.ConvertingPdfThrowable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


object SignInUtil {
    
    fun extractSignInPageData(response: SignInResponse): SignInData {
        val result: SignInData
        
        try {
            result = extractData(response.homePageBody)
        } catch (e: Exception) {
            println("SignInUtil.extractSignInPageData $e")
            throw ConvertingPdfThrowable.UnhandledSignInResponse(response.concatThrowableMessage())
        }
        return result
    }
    
    private fun extractData(response: String): SignInData {
        val document: Document = Jsoup.parse(response, "UTF-8")
        
        // Extract data based on labels
        val reference =
            document.select("td.champs_form:contains(Référence:) + td span.res_form")
                .text().trim()
        val fullName =
            document.select("td.champs_form:contains(Nom et Prénom) + td span.res_form")
                .text().trim()
        val address =
            document.select("td.champs_form:contains(Lieu de consommation) + td span.res_form")
                .text().trim()
        
        println("extractData: reference: $reference")
        println("extractData: Name: $fullName")
        println("extractData: Address: $address")

//        val update = document.select("p.orange").first()?.text()
        val billNumber = document.select("tr.tr_pair > td").first()?.text()?.trim()
        val date = document.select("tr.tr_pair > td").eq(1).text().trim()
        val trimesterData = document.select("tr.tr_pair > td").eq(2).text().trim()
        val trimester = trimesterData.first().toString()
        val trimesterYear = trimesterData.takeLast(4)
        
        // Print extracted data
//        println( update) // Should print: update date
        println(billNumber) // Should print: 412231105103
        println(date) // Should print: 2023-11-15
        println(trimesterData) // Should print: 4ème Trimestre 2023
        println(trimester) // Should print: 4ème Trimestre 2023
        println(trimesterYear) // Should print: 4ème Trimestre 2023
        
        // <a> tag with onclick attribute containing "num_fac"
        val hrefValue = document.select("a[onclick*=num_fac]").first()?.attr("onclick")
        println(hrefValue)
        
        // First check
        if (reference.isEmpty() || fullName.isEmpty() || address.isEmpty() ||
            billNumber.isNullOrEmpty() || date.isEmpty() || trimester.isEmpty() ||
            trimesterYear.isEmpty() || hrefValue.isNullOrEmpty()
        ) {
            throw Exception("Error: (Check 1) Failed to extract data from home page")
        }
        
        // Use regular expressions to extract the URL part you're interested in
        val regexPattern = """'([^']*)'""".toRegex()
        val matchResult = regexPattern.find(hrefValue)
        
        // Extract the actual URL part from the match result
        var mttTtc: String? = null
        val urlPart = matchResult?.groups?.get(1)?.value
        
        // Should print: fact.jsp?num_fac=412231105103&mtt_ttc=4849.780&filial=SDC
        println(urlPart.toString())
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
            println("extractData: num_fac: $numFac") // Should print: num_fac: 412231105103
            println("extractData: mtt_ttc: $mttTtc") // Should print: mtt_ttc: 4849.780
            println("extractData: filial: $filial") // Should print: filial: SDC}
        }
        
        // Second check
        if (urlPart.isNullOrEmpty() || mttTtc.isNullOrEmpty()) {
            throw Exception("Error: (Check 2) Failed to extract data from home page")
        }
        
        return SignInData(
            reference = reference,
            fullName = fullName,
            address = address,
            billNumber = billNumber,
            date = date,
            trimester = trimester,
            year = trimesterYear,
            amount = mttTtc,
            billUrl = urlPart,
        )
    }
    
}