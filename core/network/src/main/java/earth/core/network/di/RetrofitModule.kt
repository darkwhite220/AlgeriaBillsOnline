package earth.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import earth.core.network.BuildConfig
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
  
  @Provides
  @Singleton
  fun provideJson(): Json = Json {
    ignoreUnknownKeys = true
  }
  
  @Provides
  @Singleton
  fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
    .addInterceptor(
      HttpLoggingInterceptor()
        .apply {
          if (BuildConfig.DEBUG) {
            setLevel(HttpLoggingInterceptor.Level.BODY)
          }
        },
    )
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .build()
  
}