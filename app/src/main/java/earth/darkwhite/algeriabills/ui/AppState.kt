package earth.darkwhite.algeriabills.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import earth.darkwhite.algeriabills.ui.navigation.TopLevelDestination
import earth.feature.home.navigation.navigateToHome
import earth.feature.settings.navigation.navigateToSettings

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
): AppState =
    remember(
        navController
    ) {
        AppState(
            navController = navController,
        )
    }


@Stable
class AppState(
    val navController: NavHostController,
) {
    fun navigate(destination: TopLevelDestination) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        
        when (destination) {
            TopLevelDestination.HOME -> navController.navigateToHome(navOptions = navOptions)
            TopLevelDestination.SETTINGS -> navController.navigateToSettings(navOptions = navOptions)
        }
    }
    
}