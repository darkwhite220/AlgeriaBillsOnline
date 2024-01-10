package earth.darkwhite.algeriabills.ui.navigation

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.darkwhite.feature.createaccount.navigation.createAccountScreen
import earth.darkwhite.algeriabills.ui.AppState
import earth.darkwhite.feature.estimate.navigation.estimateScreen
import earth.darkwhite.feature.signin.navigation.signInScreen
import earth.feature.home.navigation.homeRoute
import earth.feature.home.navigation.homeScreen
import earth.feature.settings.navigation.settingsScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlgeriaBillsNavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {
    val navController = appState.navController
    NavHostWithFadeThough(
        navController = navController,
        startDestination = homeRoute,
        modifier = modifier
            .padding(paddingValues)
            .consumeWindowInsets(paddingValues)
    ) {
        homeScreen(
            onCreateAccountClick = {
                appState.navigateToCreateAccount()
            },
            onSignInClick = {
                appState.navigateToSignIn()
            },
        )
        estimateScreen(
            onSettingsClick = { appState.navigate(TopLevelDestination.SETTINGS) },
            onBackClick = { navController.popBackStack() },
        )
        settingsScreen(
            onBackClick = { navController.popBackStack() }
        )
        
        createAccountScreen(
            onBackClick = { navController.popBackStack() }
        )
        
        signInScreen(
            onBackClick = { navController.popBackStack() }
        )
    }
}
