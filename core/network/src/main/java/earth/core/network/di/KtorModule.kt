package earth.core.network.di

import android.util.Log
import androidx.core.text.htmlEncode
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.encodeOAuth
import io.ktor.http.encodeURLParameter
import io.ktor.http.encodeURLPath
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.encodeBase64
import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object KtorModule {
    
    @Singleton
    @Provides
    fun provideKtorClient(): HttpClient {
        return HttpClient(Android) {
            this.expectSuccess = true
            
            install(HttpCookies)
            
            BrowserUserAgent()
            
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            
            engine {
                connectTimeout = 5000
                socketTimeout = 5000
            }
            
            install(Logging) {
                logger = AndroidLogger()
                level = LogLevel.ALL
            }
            
            install(DefaultRequest) {
                header(HttpHeaders.AcceptEncoding, "gzip, deflate, br")
                header(HttpHeaders.Host, "consulter-factures.elit.dz")
                header(HttpHeaders.AcceptLanguage, "en-US,en;q=0.5")
                header(HttpHeaders.Connection, "keep-alive")
                header("DNT", "1")
            }
        }
    }
}

class AndroidLogger : Logger {
    override fun log(message: String) {
        Log.d("Ktor", message)
    }
}
