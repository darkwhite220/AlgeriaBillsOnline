package earth.core.network

import earth.core.network.model.ApiResponse

interface RetrofitNetworkDataSource {
  
  suspend fun getTest(): ApiResponse
}