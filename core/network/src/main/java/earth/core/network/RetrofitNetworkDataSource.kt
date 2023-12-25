package earth.core.network

import earth.core.networkmodel.ApiResponse

interface RetrofitNetworkDataSource {
  
  suspend fun getTest(): earth.core.networkmodel.ApiResponse
}