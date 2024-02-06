package earth.core.network

import earth.core.network.utils.Constants.ACTION_BUTTON
import earth.core.network.utils.Constants.ACTION_BUTTON_VALUE
import earth.core.network.utils.Constants.BASE_URL
import earth.core.network.utils.Constants.CAPTCHA
import earth.core.network.utils.Constants.CONSULTING_BILL_URL
import earth.core.network.utils.Constants.DUPLICATE_PASSWORD
import earth.core.network.utils.Constants.EMAIL
import earth.core.network.utils.Constants.LOGIN_AUTH_URL
import earth.core.network.utils.Constants.LOGIN_CONSULT_URL
import earth.core.network.utils.Constants.LOG_OUT_URL
import earth.core.network.utils.Constants.NAME
import earth.core.network.utils.Constants.NO_VALUE
import earth.core.network.utils.Constants.PASSWORD
import earth.core.network.utils.Constants.PHONE_NUMBER
import earth.core.network.utils.Constants.REFERENCE_NUMBER
import earth.core.network.utils.Constants.SIGNUP_CAPTCHA_URL
import earth.core.network.utils.Constants.SIGNUP_FORM_URL
import earth.core.network.utils.Constants.SIGNUP_URL
import earth.core.network.utils.Constants.SIGN_IN_BUTTON_X
import earth.core.network.utils.Constants.SIGN_IN_BUTTON_Y
import earth.core.network.utils.Constants.SIGN_IN_PASSWORD
import earth.core.network.utils.Constants.SIGN_IN_USERNAME
import earth.core.network.utils.Constants.USERNAME
import earth.core.network.utils.KtorHeaders.fetchBillHeaders
import earth.core.network.utils.KtorHeaders.initialHeaders
import earth.core.network.utils.KtorHeaders.logOutHeaders
import earth.core.network.utils.KtorHeaders.signInGetHeaders
import earth.core.network.utils.KtorHeaders.signInPostHeaders
import earth.core.network.utils.KtorHeaders.signupCaptchaHeaders
import earth.core.network.utils.KtorHeaders.signupFormHeaders
import earth.core.network.utils.KtorHeaders.signupHeaders
import earth.core.network.utils.Utils.randomInt
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
import io.ktor.client.network.sockets.ConnectTimeoutException
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
        connectTimeoutExceptionWrapper {
            client.get(SIGNUP_FORM_URL) {
                signupFormHeaders()
            }
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
        connectTimeoutExceptionWrapper {
            client.get(BASE_URL) {
                initialHeaders()
            }
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
    
    private suspend fun connectTimeoutExceptionWrapper(request: suspend () -> Unit) {
        try {
            request()
        } catch (e: Exception) {
            handleConnectTimeoutException(e)
        }
    }
    
    private fun handleConnectTimeoutException(e: Throwable) {
        if (e is ConnectTimeoutException) {
            throw SignInThrowable.ServerOffline
        }
    }
    
    companion object {
        private const val TAG = "AppNetwork"
    }
}


