package earth.darkwhite.feature.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import earth.darkwhite.feature.onboarding.OnBoardingRoute

const val onBoardingRoute = "on_boarding_route"

fun NavController.navigateToOnBoarding(navOptions: NavOptions? = null) {
    this.navigate(route = onBoardingRoute, navOptions = navOptions)
}

fun NavGraphBuilder.onBoardingScreen(
    onCreateAccountClick: () -> Unit,
    onSignInClick: () -> Unit,
) {
    composable(route = onBoardingRoute) {
        OnBoardingRoute(
            onCreateAccountClick = onCreateAccountClick,
            onSignInClick = onSignInClick,
        )
    }
}