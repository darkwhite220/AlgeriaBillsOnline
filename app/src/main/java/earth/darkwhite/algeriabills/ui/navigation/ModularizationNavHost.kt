package earth.darkwhite.algeriabills.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import earth.darkwhite.algeriabills.ui.AppState
import earth.feature.home.navigation.homeRoute
import earth.feature.home.navigation.homeScreen
import earth.feature.settings.navigation.settingsScreen

@Composable
fun ModularizationNavHost(
  appState: AppState,
  modifier: Modifier = Modifier,
  paddingValues: PaddingValues
) {
  val navController = appState.navController
  NavHost(
    navController = navController,
    startDestination = homeRoute,
    modifier = modifier.padding(paddingValues)
  ) {
    homeScreen(
      onSettingsClick = { appState.navigate(TopLevelDestination.SETTINGS) }
    )
    settingsScreen(
      onBackClick = { navController.popBackStack() }
    )
  }
}
