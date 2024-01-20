package earth.feature.home

import earth.core.database.Bill

sealed interface HomeEvent {
    data object OnCreateAccountClick : HomeEvent
    data object OnSignInClick : HomeEvent
    data object OnSuccessSyncUiState : HomeEvent
    data object OnFailedSyncUiState : HomeEvent
    data class OnBillPreviewClick(val billNumber: String) : HomeEvent
    data object OnBillCloseClick : HomeEvent
    data class OnBillDownloadClick(val bill: Bill) : HomeEvent
}