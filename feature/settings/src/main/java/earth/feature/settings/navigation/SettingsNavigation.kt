package earth.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import earth.feature.settings.SettingsRoute

const val settingsRoute = "settings_route"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
  this.navigate(route = settingsRoute, navOptions = navOptions)
}

fun NavGraphBuilder.settingsScreen(
  onBackClick: () -> Unit,
) {
  composable(route = settingsRoute) {
    SettingsRoute(
      onBackClick = onBackClick
    )
  }
}