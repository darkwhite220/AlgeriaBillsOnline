package earth.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import earth.core.data.util.NetworkMonitorRepository
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val network: NetworkMonitorRepository
) : ViewModel() {
  
  init {
    Log.d(TAG, "init: ")
    viewModelScope.launch {
      network.networkStatus.collect {
        Log.d(TAG, "is connected $it: ")
      }
    }
  }
  
  companion object {
    private const val TAG = "HomeViewModel"
  }
}