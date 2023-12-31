package earth.feature.home.uistate

import earth.core.database.User

sealed interface UsersUiState {
    
    data object Loading : UsersUiState
    data class Successful(val data: List<User>) : UsersUiState
    
}