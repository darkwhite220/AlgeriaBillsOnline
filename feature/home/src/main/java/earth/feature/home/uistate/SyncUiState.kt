package earth.feature.home.uistate

sealed interface SyncUiState {
    
    data object InitialState : SyncUiState
    data object Loading : SyncUiState
    data class Success(val isNotEmpty: Boolean) : SyncUiState
    data class Failed(val throwable: Throwable?) : SyncUiState
    
}