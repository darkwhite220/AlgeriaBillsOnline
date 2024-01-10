package earth.darkwhite.feature.signin

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import earth.core.data.util.NetworkMonitorRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val network: NetworkMonitorRepository,
) : ViewModel() {
    
    private val isOnline = MutableStateFlow(false)
    
    init {
        Log.d(TAG, "init: ")
        viewModelScope.launch {
            network.networkStatus.collect {
                Log.d(TAG, "isOnline $it: ")
                isOnline.value = it
            }
        }
    }
    
    companion object {
        private const val TAG = "SignInViewModel"
    }
}