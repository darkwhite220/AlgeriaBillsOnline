package earth.feature.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import earth.core.data.util.NetworkMonitorRepository
import earth.core.domain.home.GetBillUseCase
import earth.core.domain.home.GetUsersUseCase
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
    private val getBillUseCase: GetBillUseCase,
    savedStateHandle: SavedStateHandle,
    private val network: NetworkMonitorRepository,
) : ViewModel() {
    
    private val isOnline = MutableStateFlow(false)
    
    private val lastFetchTime = savedStateHandle.getStateFlow(LAST_FETCH_TIME, 0)
    
    private val canFetchNewData: Boolean
        get() = (Date().time - lastFetchTime.value) > DAY_TIME_IN_MILLIS
    
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
        viewModelScope.launch {
            usersUiState.collect {
                Log.d(TAG, "isOnline $it: ")
                
            }
        }
    }
    
    fun onEvent() {
        getBillUseCase.invoke("")
    }
    
    companion object {
        private const val TAG = "HomeViewModel"
        private const val LAST_FETCH_TIME = "last_fetch_time"
        private const val DAY_TIME_IN_MILLIS = 86_400_000
    }
}