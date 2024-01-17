package earth.feature.home

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import earth.core.common.Result
import earth.core.common.asResult
import earth.core.data.UserDataRepository
import earth.core.data.util.NetworkMonitorRepository
import earth.core.domain.SetLastFetchTimeUseCase
import earth.core.domain.home.GetBillUseCase
import earth.core.domain.home.GetUsersUseCase
import earth.core.domain.home.SyncDataUseCase
import earth.feature.home.uistate.SyncUiState
import earth.feature.home.uistate.UsersUiState
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    getUsersUseCase: GetUsersUseCase,
    private val syncDataUseCase: SyncDataUseCase,
    private val getBillUseCase: GetBillUseCase,
    private val network: NetworkMonitorRepository,
    private val userDataRepository: UserDataRepository,
    private val setLastFetchTimeUseCase: SetLastFetchTimeUseCase,
) : ViewModel() {
    
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
            started = SharingStarted.WhileSubscribed(5_000),
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
        getBillUseCase.invoke("")
        
        when (event) {
            HomeEvent.OnSuccessSyncUiState -> {
                updateLastFetchTime()
                syncUiState = SyncUiState.InitialState
            }
            HomeEvent.OnFailedSyncUiState -> {
                syncUiState = SyncUiState.InitialState
            }
            is HomeEvent.OnBillPreviewClick -> {
                // TODO
            }
            HomeEvent.OnCreateAccountClick -> {}
            HomeEvent.OnSignInClick -> {}
        }
    }
    
    private fun updateLastFetchTime() = viewModelScope.launch {
        setLastFetchTimeUseCase.invoke(Date().time)
    }
    
    companion object {
        private const val TAG = "HomeViewModel"
        private const val DAY_TIME_IN_MILLIS = 86_400_000
    }
}