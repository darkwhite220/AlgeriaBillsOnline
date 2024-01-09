package earth.feature.home

import android.accounts.NetworkErrorException
import android.util.Log
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    getUsersUseCase: GetUsersUseCase,
    private val syncDataUseCase: SyncDataUseCase,
    private val getBillUseCase: GetBillUseCase,
    savedStateHandle: SavedStateHandle,
    private val network: NetworkMonitorRepository,
) : ViewModel() {
    
    private val isOnline = MutableStateFlow(false)
    
    private val lastFetchTime = savedStateHandle.getStateFlow(LAST_FETCH_TIME, 0)
    private val canFetch: MutableStateFlow<Boolean> = MutableStateFlow(false)
    
    val usersUiState: StateFlow<UsersUiState> = getUsersUseCase.invoke()
        .map { users ->
            UsersUiState.Successful(users)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UsersUiState.Loading
        )
    
    val syncUiState: StateFlow<SyncUiState> = combine(isOnline, canFetch) { isOnline, canFetch ->
        Pair(isOnline, canFetch)
    }.flatMapLatest { (isOnline, canFetch) ->
        if (isOnline && canFetch) {
            syncDataUseCase.invoke()
                .asResult()
                .map { result ->
                    when (result) {
                        is Result.Loading -> SyncUiState.Loading
                        is Result.Success -> {
                            SyncUiState.Success(result.data)
                        }
                        is Result.Error -> SyncUiState.Failed(result.exception)
                    }
                }
        } else if (!isOnline) {
            flowOf(SyncUiState.Failed(NetworkErrorException("No connection")))
        } else {
            flowOf(SyncUiState.InitialState)
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SyncUiState.InitialState
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
//        canFetch.value = (Date().time - lastFetchTime.value) > DAY_TIME_IN_MILLIS
        canFetch.value = true
        Log.d(TAG, "initSyncData: canFetch ${canFetch.value}")
    }
    
    fun onEvent() {
        getBillUseCase.invoke("")
        // TODO UPDATE LAST FETCH TIME WHEN CORRECT SUCCESS FETCH
//        savedStateHandle[LAST_FETCH_TIME] = Date().time
    }
    
    companion object {
        private const val TAG = "HomeViewModel"
        private const val LAST_FETCH_TIME = "last_fetch_time"
        private const val DAY_TIME_IN_MILLIS = 86_400_000
    }
}