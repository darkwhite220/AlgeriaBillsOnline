package earth.feature.home

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import earth.core.common.Result
import earth.core.common.asResult
import earth.core.data.UserDataRepository
import earth.core.data.util.NetworkMonitorRepository
import earth.core.database.Bill
import earth.core.database.BillDownload
import earth.core.database.asBillDownload
import earth.core.designsystem.Constants.VIEW_MODEL_SUBSCRIPTION_TIME
import earth.core.domain.SetLastFetchTimeUseCase
import earth.core.domain.home.DownloadBillUseCase
import earth.core.domain.home.GetBillUseCase
import earth.core.domain.home.GetUsersUseCase
import earth.core.domain.home.SyncDataUseCase
import earth.feature.home.uistate.SyncUiState
import earth.feature.home.uistate.UsersUiState
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// TODO download ui state

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    getUsersUseCase: GetUsersUseCase,
    private val syncDataUseCase: SyncDataUseCase,
    private val getBillUseCase: GetBillUseCase,
    private val downloadBillUseCase: DownloadBillUseCase,
    private val network: NetworkMonitorRepository,
    private val savedStateHandle: SavedStateHandle,
    private val userDataRepository: UserDataRepository,
    private val setLastFetchTimeUseCase: SetLastFetchTimeUseCase,
) : ViewModel() {
    
    var onDownload by mutableStateOf(EMPTY_STRING)
    
    val selectedBill: StateFlow<Bill?> =
        savedStateHandle.getStateFlow(SELECTED_BILL_NUMBER, EMPTY_STRING)
            .flatMapLatest { billNumber ->
                if (billNumber.isEmpty()) {
                    flowOf(null)
                } else {
                    getBillUseCase.invoke(billNumber)
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(VIEW_MODEL_SUBSCRIPTION_TIME),
                initialValue = null,
            )
    
    private val isOnline = MutableStateFlow(false)
    
    private val lastFetchTime = MutableStateFlow(0L)
    private val canFetch: Boolean
        get() = (Date().time - lastFetchTime.value) > DAY_TIME_IN_MILLIS
    
    var syncUiState: SyncUiState by mutableStateOf(SyncUiState.InitialState)
        private set
    
    val usersUiState: StateFlow<UsersUiState> = getUsersUseCase.invoke()
        .map { users ->
            UsersUiState.Successful(users)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(VIEW_MODEL_SUBSCRIPTION_TIME),
            initialValue = UsersUiState.Loading
        )
    
    init {
        Log.d(TAG, "init: ")
        viewModelScope.let { scope ->
            scope.launch {
                network.networkStatus.collect {
                    Log.d(TAG, "isOnline $it ")
                    isOnline.value = it
                }
            }
            scope.launch {
                userDataRepository.lastFetchTime.collect {
                    Log.d(TAG, "lastFetchTime $it ")
                    lastFetchTime.value = it
                }
            }
        }
    }
    
    fun initSyncData() {
        Log.d(TAG, "initSyncData: canFetch $canFetch")
        viewModelScope.launch {
            if (isOnline.value && canFetch) {
                syncDataUseCase.invoke()
                    .asResult()
                    .collect { result ->
                        syncUiState = when (result) {
                            is Result.Loading -> SyncUiState.Loading
                            is Result.Success -> SyncUiState.Success(result.data)
                            is Result.Error -> SyncUiState.Failed(result.exception)
                        }
                    }
            } else if (!isOnline.value) {
                SyncUiState.Failed(NetworkErrorException("No connection"))
            } else {
                SyncUiState.InitialState
            }
        }
    }
    
    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.OnSuccessSyncUiState -> {
                updateLastFetchTime()
                syncUiState = SyncUiState.InitialState
            }
            HomeEvent.OnFailedSyncUiState -> {
                syncUiState = SyncUiState.InitialState
            }
            is HomeEvent.OnBillPreviewClick -> {
                updateSelectedBill(event.billNumber)
            }
            HomeEvent.OnBillCloseClick -> {
                updateSelectedBill(EMPTY_STRING)
            }
            is HomeEvent.OnBillDownloadClick -> {
                downloadBill(event.bill.asBillDownload())
            }
            HomeEvent.OnCreateAccountClick -> {
                // No op
            }
            HomeEvent.OnSignInClick -> {
                // No op
            }
        }
    }
    
    private fun downloadBill(billDownload: BillDownload) {
        viewModelScope.launch {
            onDownload = downloadBillUseCase.invoke(billDownload)
            updateSelectedBill(EMPTY_STRING)
        }
    }
    
    private fun updateSelectedBill(newValue: String) {
        savedStateHandle[SELECTED_BILL_NUMBER] = newValue
    }
    
    private fun updateLastFetchTime() = viewModelScope.launch {
        setLastFetchTimeUseCase.invoke(Date().time)
    }
    
    fun resetDownloadData() {
        onDownload = EMPTY_STRING
    }
    
    companion object {
        private const val TAG = "HomeViewModel"
        private const val SELECTED_BILL_NUMBER = "selected_bill"
        private const val EMPTY_STRING = ""
        private const val DAY_TIME_IN_MILLIS = 86_400_000
    }
}