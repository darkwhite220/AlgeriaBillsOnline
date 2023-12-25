package com.darkwhite.feature.createaccount.uistate

import android.graphics.Bitmap

sealed interface CaptchaUiState {
    
    data object Loading : CaptchaUiState
    data class Success(val captchaBitmap: Bitmap) : CaptchaUiState
    data class Failed(val exception: Throwable? = null) : CaptchaUiState
    
}