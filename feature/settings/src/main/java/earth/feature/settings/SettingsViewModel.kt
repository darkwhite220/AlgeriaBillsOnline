package earth.feature.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import earth.core.data.UserDataRepository
import earth.core.designsystem.Constants.VIEW_MODEL_SUBSCRIPTION_TIME
import earth.core.preferencesmodel.UserData
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    
    val uiState: StateFlow<SettingsUiState> = userDataRepository.userData
        .map { SettingsUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(VIEW_MODEL_SUBSCRIPTION_TIME),
            initialValue = SettingsUiState.Loading
        )
    
    init {
        Log.d(TAG, "init: ")
    }
    
    fun setUserData(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnDarKThemeChange -> {
                viewModelScope.launch {
                    userDataRepository.setDarkThemeConfig(event.darkThemeConfig)
                }
            }
            
            is SettingsEvent.OnThemeBrandChange -> {
                viewModelScope.launch {
                    userDataRepository.setThemeBrand(event.themeBrand)
                }
            }
            
            is SettingsEvent.OnNotificationChange -> {
                viewModelScope.launch {
                    userDataRepository.setNotificationStatus(event.isEnabled)
                }
            }
            
            is SettingsEvent.OnLanguageChange -> {
                viewModelScope.launch {
                    userDataRepository.setLanguage(event.language)
                }
            }
            
            SettingsEvent.OnFirstLaunch -> {
                viewModelScope.launch {
                    userDataRepository.setFirstLaunch(false)
                    userDataRepository.setNotificationStatus(true)
                }
            }
        }
    }
    
    companion object {
        private const val TAG = "SettingsViewModel"
    }
}

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val userData: UserData) : SettingsUiState
}