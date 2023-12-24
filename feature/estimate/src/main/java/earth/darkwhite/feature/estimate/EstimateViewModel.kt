package earth.darkwhite.feature.estimate

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EstimateViewModel @Inject constructor(
//  private val network: NetworkMonitorRepository
) : ViewModel() {
    
    init {
        Log.d(TAG, "init: ")
//    viewModelScope.launch {
//      network.networkStatus.collect {
//        Log.d(TAG, "is connected $it: ")
//      }
//    }
    }
    
    companion object {
        private const val TAG = "EstimateViewModel"
    }
}
