package earth.feature.settings

import android.os.Build
import android.os.Build.VERSION
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import earth.core.designsystem.components.horizontalSpacedBy
import earth.core.model.DarkThemeConfig
import earth.core.model.ThemeBrand
import earth.core.model.UserData

@Composable
internal fun SettingsRoute(
  modifier: Modifier = Modifier,
  onBackClick: () -> Unit,
  viewModel: SettingsViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  SettingsScreen(
    modifier = modifier,
    uiState = uiState,
    onBackClick = onBackClick,
    onSettingsEvent = viewModel::setUserData,
  )
}

@Composable
private fun SettingsScreen(
  modifier: Modifier = Modifier,
  uiState: SettingsUiState,
  onBackClick: () -> Unit,
  onSettingsEvent: (SettingsEvent) -> Unit,
) {
  when (uiState) {
    SettingsUiState.Loading -> {
      CircularProgressIndicator()
    }
    
    is SettingsUiState.Success -> {
      SettingsContent(modifier, uiState.userData, onBackClick, onSettingsEvent)
    }
  }
}

@Composable
private fun SettingsContent(
  modifier: Modifier,
  userData: UserData,
  onBackClick: () -> Unit,
  onSettingsEvent: (SettingsEvent) -> Unit
) {
  Scaffold(modifier = modifier) { paddingValues ->
    Column(
      modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()
    ) {
      Text(text = "SETTINGS")
      OutlinedButton(onClick = onBackClick) {
        Text(text = "Back")
      }
      
      Text(text = "Dark Theme")
      Row(horizontalArrangement = horizontalSpacedBy()) {
        Text(text = "FOLLOW SYSTEM")
        Checkbox(
          checked = userData.darkThemeConfig == DarkThemeConfig.FOLLOW_SYSTEM,
          onCheckedChange = { onSettingsEvent(SettingsEvent.OnDarKThemeChange(DarkThemeConfig.FOLLOW_SYSTEM)) })
      }
      Row(horizontalArrangement = horizontalSpacedBy()) {
        Text(text = "DARK")
        Checkbox(
          checked = userData.darkThemeConfig == DarkThemeConfig.DARK,
          onCheckedChange = { onSettingsEvent(SettingsEvent.OnDarKThemeChange(DarkThemeConfig.DARK)) }
        )
      }
      Row(horizontalArrangement = horizontalSpacedBy()) {
        Text(text = "LIGHT")
        Checkbox(
          checked = userData.darkThemeConfig == DarkThemeConfig.LIGHT,
          onCheckedChange = { onSettingsEvent(SettingsEvent.OnDarKThemeChange(DarkThemeConfig.LIGHT)) }
        )
      }
  
      if (VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Text(text = "Theme Brand")
        Row(horizontalArrangement = horizontalSpacedBy()) {
          Text(text = "Default")
          Checkbox(
            checked = userData.themeBrand == ThemeBrand.DEFAULT,
            onCheckedChange = { onSettingsEvent(SettingsEvent.OnThemeBrandChange(ThemeBrand.DEFAULT)) })
        }
        Row(horizontalArrangement = horizontalSpacedBy()) {
          Text(text = "Android")
          Checkbox(
            checked = userData.themeBrand == ThemeBrand.ANDROID,
            onCheckedChange = { onSettingsEvent(SettingsEvent.OnThemeBrandChange(ThemeBrand.ANDROID)) }
          )
        }
      }
      
    }
  }
}