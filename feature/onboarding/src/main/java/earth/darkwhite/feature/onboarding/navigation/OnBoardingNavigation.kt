package earth.darkwhite.feature.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import earth.darkwhite.feature.onboarding.OnBoardingRoute

const val onBoardingRoute = "on_boarding_route"

fun NavGraphBuilder.onBoardingScreen() {
    composable(route = onBoardingRoute) {
        OnBoardingRoute()
    }
}