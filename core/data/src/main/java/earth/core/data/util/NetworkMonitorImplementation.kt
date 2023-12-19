package earth.core.data.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate


class NetworkMonitorImplementation @Inject constructor(
  @ApplicationContext context: Context
) : NetworkMonitorRepository {
  
  private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  
  override val networkStatus: Flow<Boolean> = callbackFlow {
    Log.d(TAG, "connectionStatus: ")
    
    val networkCallback = object : ConnectivityManager.NetworkCallback() {
      
      override fun onAvailable(network: Network) {
        super.onAvailable(network)
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        Log.d(TAG, "onAvailable: $networkCapabilities")
        
        if (networkCapabilities.isNetworkCapabilitiesValid()) {
          Log.d(TAG, "onAvailable: HasInternet")
          channel.trySend(true)
        } else {
          Log.d(TAG, "onAvailable: NoInternet")
          channel.trySend(false)
        }
      }
      
      override fun onLost(network: Network) {
        super.onLost(network)
        Log.d(TAG, "onLost: ")
        channel.trySend(false)
      }
    }
    
    val networkRequest = NetworkRequest.Builder()
      .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
      .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
      .build()
    connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    
    awaitClose { connectivityManager.unregisterNetworkCallback(networkCallback) }
  }.conflate()
  
  private fun NetworkCapabilities?.isNetworkCapabilitiesValid(): Boolean = when {
    this == null                                              -> false
    hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
      hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
      (hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
        hasTransport(NetworkCapabilities.TRANSPORT_VPN) ||
        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) -> true
    
    else                                                      -> false
  }
  
  companion object {
    private const val TAG = "NetworkMonitorImpl"
  }
}