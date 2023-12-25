package earth.core.network

import earth.core.network.Constants.ACTION_BUTTON
import earth.core.network.Constants.ACTION_BUTTON_VALUE
import earth.core.network.Constants.BASE_URL
import earth.core.network.Constants.CAPTCHA
import earth.core.network.Constants.DUPLICATE_PASSWORD
import earth.core.network.Constants.EMAIL
import earth.core.network.Constants.NAME
import earth.core.network.Constants.NO_VALUE
import earth.core.network.Constants.PASSWORD
import earth.core.network.Constants.PHONE_NUMBER
import earth.core.network.Constants.REFERENCE_NUMBER
import earth.core.network.Constants.SIGNUP_CAPTCHA_URL
import earth.core.network.Constants.SIGNUP_FORM_URL
import earth.core.network.Constants.SIGNUP_URL
import earth.core.network.Constants.USERNAME
import earth.core.network.di.KtorHeaders.initialHeaders
import earth.core.network.di.KtorHeaders.signupCaptchaHeaders
import earth.core.network.di.KtorHeaders.signupFormHeaders
import earth.core.network.di.KtorHeaders.signupHeaders
import earth.core.networkmodel.SignupCaptcha
import earth.core.networkmodel.SignupRequestBody
import earth.core.networkmodel.SignupResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.cookies.cookies
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import javax.inject.Inject

class AppNetwork @Inject constructor(
    private val client: HttpClient
) : AppNetworkDataSource {
    
    override suspend fun fetchSignupCaptcha(): SignupCaptcha {
        // FOR COOKIES
        client.get(BASE_URL) {
            initialHeaders()
        }

//        client.printCookies()
        
        client.get(SIGNUP_FORM_URL) {
            signupHeaders()
        }
        
        var totalLength = 0
        val response = client.get(SIGNUP_CAPTCHA_URL) {
            signupCaptchaHeaders()
            onDownload { bytesSentTotal, contentLength ->
                println("Received $bytesSentTotal bytes from $contentLength")
                totalLength = bytesSentTotal.toInt()
            }
        }
        
        return SignupCaptcha(
            imageByteArray = response.body(),
            length = totalLength
        )
    }
    
    override suspend fun requestSignup(signupRequestBody: SignupRequestBody): SignupResponse {
        // FOR COOKIES
//        client.get(BASE_URL) {
//            initialHeaders()
//        }
        client.get(SIGNUP_FORM_URL) {
            signupFormHeaders()
        }
        val response = client.post(SIGNUP_URL) {
            signupHeaders()
            
            setBody(
                FormDataContent(
                    Parameters.build {
                        append(NAME, NO_VALUE)
                        append(EMAIL, signupRequestBody.email)
                        append(PHONE_NUMBER, NO_VALUE)
                        append(USERNAME, signupRequestBody.username)
                        append(REFERENCE_NUMBER, signupRequestBody.reference)
                        append(PASSWORD, signupRequestBody.newpass)
                        append(DUPLICATE_PASSWORD, signupRequestBody.cfnewpass)
                        append(CAPTCHA, signupRequestBody.captcha)
                        append(ACTION_BUTTON, ACTION_BUTTON_VALUE)
                    })
            )
        }
        
        return SignupResponse(
            headers = response.headers.entries(),
            body = response.bodyAsText()
        )
    }
    
    private suspend fun HttpClient.printCookies() {
        this.cookies(BASE_URL).forEach {
            println("Cookie: $it.")
        }
    }
}

