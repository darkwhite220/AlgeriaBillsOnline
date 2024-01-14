package earth.core.network

import earth.core.network.Constants.ACTION_BUTTON
import earth.core.network.Constants.ACTION_BUTTON_VALUE
import earth.core.network.Constants.BASE_URL
import earth.core.network.Constants.CAPTCHA
import earth.core.network.Constants.CONSULTING_BILL_URL
import earth.core.network.Constants.DUPLICATE_PASSWORD
import earth.core.network.Constants.EMAIL
import earth.core.network.Constants.LOGIN_AUTH_URL
import earth.core.network.Constants.LOGIN_CONSULT_URL
import earth.core.network.Constants.LOG_OUT_URL
import earth.core.network.Constants.NAME
import earth.core.network.Constants.NO_VALUE
import earth.core.network.Constants.PASSWORD
import earth.core.network.Constants.PHONE_NUMBER
import earth.core.network.Constants.REFERENCE_NUMBER
import earth.core.network.Constants.SIGNUP_CAPTCHA_URL
import earth.core.network.Constants.SIGNUP_FORM_URL
import earth.core.network.Constants.SIGNUP_URL
import earth.core.network.Constants.SIGN_IN_BUTTON_X
import earth.core.network.Constants.SIGN_IN_BUTTON_Y
import earth.core.network.Constants.SIGN_IN_PASSWORD
import earth.core.network.Constants.SIGN_IN_USERNAME
import earth.core.network.Constants.USERNAME
import earth.core.network.Utils.randomInt
import earth.core.network.di.KtorHeaders.fetchBillHeaders
import earth.core.network.di.KtorHeaders.initialHeaders
import earth.core.network.di.KtorHeaders.logOutHeaders
import earth.core.network.di.KtorHeaders.signInGetHeaders
import earth.core.network.di.KtorHeaders.signInPostHeaders
import earth.core.network.di.KtorHeaders.signupCaptchaHeaders
import earth.core.network.di.KtorHeaders.signupFormHeaders
import earth.core.network.di.KtorHeaders.signupHeaders
import earth.core.networkmodel.BillResponse
import earth.core.networkmodel.SignInResponse
import earth.core.networkmodel.SignupCaptcha
import earth.core.networkmodel.SignupRequestBody
import earth.core.networkmodel.SignupResponse
import earth.core.throwablemodel.SignInThrowable
import earth.core.throwablemodel.SignInThrowableConstants
import earth.core.throwablemodel.SignInThrowableConstants.TEMPORARILY_LOCKED_ACCOUNT_KEY
import earth.core.throwablemodel.SignInThrowableConstants.TEMPORARILY_LOCKED_ACCOUNT_VALUE
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.cookies.cookies
import io.ktor.client.plugins.expectSuccess
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
        client.get(SIGNUP_FORM_URL) {
            signupFormHeaders()
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
        val response = client.post(SIGNUP_URL) {
            signupHeaders()
            
            expectSuccess = false
            
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
        
        println("SignupResponse ${response.status.value} ${response.status}")
        return SignupResponse(
            responseCode = response.status.value,
            headers = response.headers.entries(),
            body = response.bodyAsText()
        )
    }
    
    override suspend fun signIn(username: String, password: String): SignInResponse {
        client.get(BASE_URL) {
            initialHeaders()
        }
        
        val logIn = client.post(LOGIN_AUTH_URL) {
            signInPostHeaders()
            
            expectSuccess = false
            
            setBody(
                FormDataContent(
                    Parameters.build {
                        append(SIGN_IN_USERNAME, username)
                        append(SIGN_IN_PASSWORD, password)
                        append(SIGN_IN_BUTTON_X, randomInt())
                        append(SIGN_IN_BUTTON_Y, randomInt())
                    })
            )
        }
        
        val logInBody = logIn.bodyAsText()
        
        if (logInBody.contains(SignInThrowableConstants.WRONG_USERNAME) ||
            logInBody.contains(SignInThrowableConstants.WRONG_USERNAME_TWO)
        ) {
            throw SignInThrowable.BadUsername
        } else if (logInBody.contains(SignInThrowableConstants.WRONG_PASSWORD)) {
            throw SignInThrowable.BadPassword
        } else if (logIn.headers.contains(
                TEMPORARILY_LOCKED_ACCOUNT_KEY,
                TEMPORARILY_LOCKED_ACCOUNT_VALUE
            )
        ) {
            throw SignInThrowable.TemporarilyLockedAccount
        }
        
        val response = client.get(LOGIN_CONSULT_URL) {
            signInGetHeaders()
        }
        
        return SignInResponse(
            signInBody = logInBody,
            homePageBody = response.bodyAsText(),
        )
    }
    
    override suspend fun fetchBill(urlEndpoint: String): BillResponse {
        var totalLength = 0
        val response = client.get(CONSULTING_BILL_URL + urlEndpoint) {
            fetchBillHeaders()
            onDownload { bytesSentTotal, contentLength ->
                println("Received $bytesSentTotal bytes from $contentLength")
                totalLength = bytesSentTotal.toInt()
            }
        }
        
        return BillResponse(
            length = totalLength,
            pdfByteArray = response.body(),
        )
    }
    
    override suspend fun logOut() {
        client.get(LOG_OUT_URL) {
            logOutHeaders()
            expectSuccess = false
        }
    }
    
    private suspend fun HttpClient.printCookies() {
        this.cookies(BASE_URL).forEach {
            println("Cookie: $it.")
        }
    }
    
    companion object {
        private const val TAG = "AppNetwork"
    }
}


