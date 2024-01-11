package earth.darkwhite.feature.signin.uistate

sealed interface SignInUiState {
    
    data object InitialState : SignInUiState
    data object Loading : SignInUiState
    data class Success(val data: Boolean) : SignInUiState
    data class Failed(val throwable: Throwable?) : SignInUiState
    
}
