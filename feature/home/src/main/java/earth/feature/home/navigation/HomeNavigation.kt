package earth.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import earth.feature.home.HomeRoute

const val homeRoute = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
  this.navigate(route = homeRoute, navOptions = navOptions)
}

fun NavGraphBuilder.homeScreen(
    onCreateAccountClick: () -> Unit
) {
  composable(route = homeRoute) {
    HomeRoute(
      onCreateAccountClick = onCreateAccountClick
    )
  }
}