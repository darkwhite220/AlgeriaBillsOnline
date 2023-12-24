package com.darkwhite.feature.createaccount.uistate

sealed interface CreateAccountUiState {
    
    data object InitialState : CreateAccountUiState
    data object Loading : CreateAccountUiState
    data object Success : CreateAccountUiState
    data class Failed(
        val message: String? = null,
        val exception: Exception? = null
    ) : CreateAccountUiState
    
}