package com.darkwhite.feature.createaccount.uistate

sealed interface SignupUiState {
    
    data object InitialState : SignupUiState
    data object Loading : SignupUiState
    data object Success : SignupUiState
    data class Failed(val exception: Throwable? = null) : SignupUiState
    
}