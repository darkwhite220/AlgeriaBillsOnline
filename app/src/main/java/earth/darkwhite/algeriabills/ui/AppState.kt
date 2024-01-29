package earth.darkwhite.algeriabills.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.darkwhite.feature.createaccount.navigation.navigateToCreateAccount
import earth.core.data.util.NetworkMonitorRepository
import earth.darkwhite.algeriabills.ui.navigation.TopLevelDestination
import earth.darkwhite.feature.estimate.navigation.estimateRoute
import earth.darkwhite.feature.estimate.navigation.navigateToEstimate
import earth.darkwhite.feature.signin.navigation.navigateToSignIn
import earth.feature.home.navigation.homeRoute
import earth.feature.home.navigation.navigateToHome
import earth.feature.settings.navigation.navigateToSettings
import earth.feature.settings.navigation.settingsRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    networkMonitor: NetworkMonitorRepository,
): AppState =
    remember(
        navController,
        coroutineScope,
        networkMonitor,
    ) {
        AppState(
            navController = navController,
            coroutineScope = coroutineScope,
            networkMonitor = networkMonitor,
        )
    }


@Stable
class AppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitorRepository,
) {
    val isOffline = networkMonitor.networkStatus
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )
    
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination
    
    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            homeRoute -> TopLevelDestination.HOME
            estimateRoute -> TopLevelDestination.ESTIMATE
            settingsRoute -> TopLevelDestination.SETTINGS
            else -> null
        }
    
    val topLevelDestination: List<TopLevelDestination> = TopLevelDestination.entries.toList()
    
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
            TopLevelDestination.ESTIMATE -> navController.navigateToEstimate(navOptions = navOptions)
            TopLevelDestination.SETTINGS -> navController.navigateToSettings(navOptions = navOptions)
        }
    }
    
    fun navigateToCreateAccount() {
        navController.navigateToCreateAccount()
    }
    
    fun navigateToSignIn() {
        navController.navigateToSignIn()
    }
    
    fun popBackStack() {
        if (navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED)
            navController.popBackStack()
    }
}