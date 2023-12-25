package earth.core.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import earth.core.networkmodel.ApiResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

@Singleton
class RetrofitNetwork @Inject constructor(
  json: Json,
  okHttpClient: Call.Factory
) : RetrofitNetworkDataSource {
  
  private val retrofitNetworkApi = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .callFactory(okHttpClient)
    .addConverterFactory(
      json.asConverterFactory("application/json".toMediaType())
    )
    .build()
    .create(RetrofitNetworkApi::class.java)
  
  override suspend fun getTest(): earth.core.networkmodel.ApiResponse =
    retrofitNetworkApi.getTest()
  
  companion object {
    private const val BASE_URL = ""
  }
}

private interface RetrofitNetworkApi {
  
  @GET("")
  suspend fun getTest(): earth.core.networkmodel.ApiResponse
  
}
