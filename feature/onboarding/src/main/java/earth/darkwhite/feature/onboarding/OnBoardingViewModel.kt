package earth.darkwhite.feature.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(

) : ViewModel() {
    
    init {
        Log.d(TAG, "init: ")
    }
    
    
    companion object {
        private const val TAG = "OnBoardingViewModel"
    }
}