package earth.darkwhite.algeriabills.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import earth.core.designsystem.components.FABCreateAccount
import earth.darkwhite.algeriabills.ui.navigation.AlgeriaBillsNavHost

@Composable
fun AlgeriaBillsApp(
    appState: AppState = rememberAppState()
) {
    val currentDestination = appState.currentTopLevelDestination
    // TODO bottomBar background like nia
    Scaffold(
        bottomBar = {
            if (currentDestination != null) {
                NavigationBar {
                    appState.topLevelDestination.forEach { item ->
                        val isSelected = appState.currentTopLevelDestination == item
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { appState.navigate(item) },
                            icon = {
                                Icon(
                                    imageVector =
                                    if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = null
                                )
                            },
                            label = { Text(text = item.iconTextId) },
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        AlgeriaBillsNavHost(
            appState = appState,
            modifier = Modifier,
            paddingValues = paddingValues,
        )
    }
}