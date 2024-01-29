package earth.darkwhite.feature.estimate.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import earth.darkwhite.feature.estimate.EstimateRoute

const val estimateRoute = "estimate_route"

fun NavController.navigateToEstimate(navOptions: NavOptions? = null) {
    this.navigate(route = estimateRoute, navOptions = navOptions)
}

fun NavGraphBuilder.estimateScreen(
    onBackClick: () -> Unit,
) {
    composable(route = estimateRoute) {
        EstimateRoute(
            onBackClick = onBackClick,
        )
    }
}