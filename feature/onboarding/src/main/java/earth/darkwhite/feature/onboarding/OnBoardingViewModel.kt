package earth.darkwhite.feature.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import earth.core.data.UserDataRepository
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    
    init {
        Log.d(TAG, "init: ")
    }
    
    fun onStartClick() = viewModelScope.launch {
        userDataRepository.setOnBoarding(false)
    }
    
    companion object {
        private const val TAG = "OnBoardingViewModel"
    }
}