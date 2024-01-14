package earth.feature.home

sealed interface HomeEvent {
    data object OnCreateAccountClick : HomeEvent
    data object OnSignInClick : HomeEvent
    data object OnSuccessSyncUiState : HomeEvent
    data object OnFailedSyncUiState : HomeEvent
}