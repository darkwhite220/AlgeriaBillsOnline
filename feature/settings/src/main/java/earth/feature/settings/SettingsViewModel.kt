package earth.feature.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import earth.core.data.UserDataRepository
import earth.core.model.UserData
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
      started = SharingStarted.WhileSubscribed(5_000),
      initialValue = SettingsUiState.Loading
    )
  
  init {
    Log.d(TAG, "init: ")
  }
  
  fun setUserData(event: SettingsEvent) {
    when (event) {
      is SettingsEvent.OnDarKThemeChange  -> {
        viewModelScope.launch{
          userDataRepository.setDarkThemeConfig(event.darkThemeConfig)
        }
      }
      
      is SettingsEvent.OnThemeBrandChange -> {
        viewModelScope.launch{
          userDataRepository.setThemeBrand(event.themeBrand)
        }
      }
    }
  }
  
  companion object {
    private const val TAG = "SettingsViewModel"
  }
}

sealed interface SettingsUiState {
  object Loading : SettingsUiState
  data class Success(val userData: UserData) : SettingsUiState
}