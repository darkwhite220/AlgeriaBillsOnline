package earth.core.network.di

import earth.core.network.Constants.ACCEPT_ALL
import earth.core.network.Constants.ACCEPT_IMAGE
import earth.core.network.Constants.BASE_URL
import earth.core.network.Constants.HEADER_CONSULT_URL
import earth.core.network.Constants.SIGNUP_FORM_URL
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

object KtorHeaders {
    
    // DON'T USE accept(ContentType.parse(ACCEPT_ALL))
    
    fun HttpRequestBuilder.initialHeaders() {
        headers {
            header("Accept", ACCEPT_ALL)
            header("Upgrade-Insecure-Requests", "1")
            header("Sec-Fetch-Site", "none")
            header("Sec-Fetch-Mode", "navigate")
            header("Sec-Fetch-Dest", "document")
            header("Sec-Fetch-User", "?1")
        }
    }
    
    fun HttpRequestBuilder.signupFormHeaders() {
        headers {
            header("Accept", ACCEPT_ALL)
            header(HttpHeaders.Referrer, "https://consulter-factures.elit.dz/contenu_index.jsp")
            header("Sec-Fetch-Site", "same-origin")
            header("Sec-Fetch-Mode", "navigate")
            header("Sec-Fetch-Dest", "frame")
            header("Sec-Fetch-User", "?1")
        }
    }
    
    fun HttpRequestBuilder.signupCaptchaHeaders() {
        headers {
            header("Accept", ACCEPT_IMAGE)
            header(HttpHeaders.Referrer, SIGNUP_FORM_URL)
            header(HttpHeaders.Origin, BASE_URL)
            header("Sec-Fetch-Site", "same-origin")
            header("Sec-Fetch-Mode", "no-cors")
            header("Sec-Fetch-Dest", "image")
        }
    }
    
    fun HttpRequestBuilder.signupHeaders() {
        headers {
            contentType(ContentType.Application.FormUrlEncoded)
            header("Accept", ACCEPT_ALL)
            header(HttpHeaders.Referrer, SIGNUP_FORM_URL)
            header(HttpHeaders.Origin, BASE_URL)
            header("Upgrade-Insecure-Requests", "1")
            header("Sec-Fetch-Site", "same-origin")
            header("Sec-Fetch-Mode", "navigate")
            header("Sec-Fetch-Dest", "frame")
            header("Sec-Fetch-User", "?1")
        }
    }
    
    fun HttpRequestBuilder.signInPostHeaders() {
        headers {
            contentType(ContentType.Application.FormUrlEncoded)
            header("Accept", ACCEPT_ALL)
            header(HttpHeaders.Referrer, HEADER_CONSULT_URL)
            header(HttpHeaders.Origin, BASE_URL)
            header("Upgrade-Insecure-Requests", "1")
            header("Sec-Fetch-Site", "same-origin")
            header("Sec-Fetch-Mode", "navigate")
            header("Sec-Fetch-Dest", "frame")
            header("Sec-Fetch-User", "?1")
        }
    }
    
    fun HttpRequestBuilder.signInGetHeaders() {
        headers {
            header("Accept", ACCEPT_ALL)
            header(HttpHeaders.Referrer, HEADER_CONSULT_URL)
            header("Upgrade-Insecure-Requests", "1")
            header("Sec-Fetch-Site", "same-origin")
            header("Sec-Fetch-Mode", "navigate")
            header("Sec-Fetch-Dest", "frame")
            header("Sec-Fetch-User", "?1")
        }
    }
    
    fun HttpRequestBuilder.fetchBillHeaders() {
        headers {
            header("Accept", ACCEPT_ALL)
            header("Upgrade-Insecure-Requests", "1")
            header("Sec-Fetch-Site", "none")
            header("Sec-Fetch-Mode", "navigate")
            header("Sec-Fetch-Dest", "document")
            header("Sec-Fetch-User", "?1")
        }
    }
    
    
}