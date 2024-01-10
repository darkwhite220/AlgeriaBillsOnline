package earth.feature.home

interface HomeEvent {
    data object OnCreateAccountClick: HomeEvent
    data object OnSignInClick: HomeEvent
    data object OnSuccessSyncUiState: HomeEvent
}