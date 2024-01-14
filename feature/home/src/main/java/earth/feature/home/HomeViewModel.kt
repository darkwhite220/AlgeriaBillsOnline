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
import earth.core.data.util.NetworkMonitorRepository
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    getUsersUseCase: GetUsersUseCase,
    private val syncDataUseCase: SyncDataUseCase,
    private val getBillUseCase: GetBillUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val network: NetworkMonitorRepository,
) : ViewModel() {
    
    private val isOnline = MutableStateFlow(false)
    // TODO store the value with user
    private val lastFetchTime = savedStateHandle.getStateFlow(
        key = LAST_FETCH_TIME,
        initialValue = 0
    )
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
        viewModelScope.launch {
            network.networkStatus.collect {
                Log.d(TAG, "isOnline $it: ")
                isOnline.value = it
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
                savedStateHandle[LAST_FETCH_TIME] = Date().time
                syncUiState = SyncUiState.InitialState
            }
            HomeEvent.OnFailedSyncUiState -> {
                syncUiState = SyncUiState.InitialState
            }
            HomeEvent.OnCreateAccountClick -> TODO()
            HomeEvent.OnSignInClick -> TODO()
        }
    }
    
    companion object {
        private const val TAG = "HomeViewModel"
        private const val LAST_FETCH_TIME = "last_fetch_time"
        private const val DAY_TIME_IN_MILLIS = 86_400_000
    }
}