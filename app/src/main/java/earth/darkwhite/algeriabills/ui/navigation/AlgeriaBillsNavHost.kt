package earth.darkwhite.algeriabills.ui.navigation

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.darkwhite.feature.createaccount.navigation.createAccountScreen
import earth.darkwhite.algeriabills.ui.AppState
import earth.darkwhite.feature.estimate.navigation.estimateScreen
import earth.darkwhite.feature.onboarding.navigation.onBoardingRoute
import earth.darkwhite.feature.onboarding.navigation.onBoardingScreen
import earth.darkwhite.feature.signin.navigation.signInScreen
import earth.feature.home.navigation.homeRoute
import earth.feature.home.navigation.homeRouteStartingDestination
import earth.feature.home.navigation.homeScreen
import earth.feature.settings.navigation.settingsScreen


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlgeriaBillsNavHost(
    appState: AppState,
    shouldShowOnBoarding: Boolean,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {
    NavHostWithFadeThough(
        navController = appState.navController,
        startDestination = if (shouldShowOnBoarding) onBoardingRoute else homeRouteStartingDestination,
        modifier = modifier
            .padding(paddingValues)
            .consumeWindowInsets(paddingValues)
    ) {
        onBoardingScreen()
        
        homeNestedGraph(appState)
        
        estimateScreen(
            onBackClick = { appState.popBackStack() },
        )
        
        settingsScreen(
            onBackClick = { appState.popBackStack() }
        )
    }
}

private fun NavGraphBuilder.homeNestedGraph(appState: AppState) {
    navigation(startDestination = homeRoute, route = homeRouteStartingDestination) {
        homeScreen(
            onCreateAccountClick = {
                appState.navigateToCreateAccount()
            },
            onSignInClick = {
                appState.navigateToSignIn()
            },
        )
        
        createAccountScreen(
            onBackClick = { appState.popBackStack() }
        )
        
        signInScreen(
            onBackClick = { appState.popBackStack() }
        )
    }
}
