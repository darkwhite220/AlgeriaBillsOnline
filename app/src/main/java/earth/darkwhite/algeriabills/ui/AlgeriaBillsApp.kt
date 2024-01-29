package earth.darkwhite.algeriabills.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import earth.core.data.NetworkMonitorRepository
import earth.core.designsystem.components.smallDp
import earth.darkwhite.algeriabills.ui.navigation.AlgeriaBillsNavHost
import earth.darkwhite.algeriabills.ui.navigation.IconRepresentation

@Composable
fun AlgeriaBillsApp(
    networkMonitor: NetworkMonitorRepository,
    appState: AppState = rememberAppState(
        networkMonitor = networkMonitor
    ),
) {
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    
    Scaffold(
        bottomBar = {
            BottomBar(
                isOffline = isOffline,
                appState = appState
            )
        },
    ) { paddingValues ->
        AlgeriaBillsNavHost(
            appState = appState,
            modifier = Modifier,
            paddingValues = paddingValues,
        )
    }
}

@Composable
private fun BottomBar(
    isOffline: Boolean,
    appState: AppState
) {
    val currentDestination = appState.currentTopLevelDestination
    Column {
        if (isOffline) {
            NoConnectionUi(navigationBarPaddingEnabled = { currentDestination == null })
        }
        if (currentDestination != null) {
            NavigationBar {
                appState.topLevelDestination.forEach { item ->
                    val isSelected = appState.currentTopLevelDestination == item
                    val targetIcon =
                        if (isSelected) item.selectedIcon else item.unselectedIcon
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { appState.navigate(item) },
                        icon = {
                            when (targetIcon) {
                                is IconRepresentation.Vector -> {
                                    Icon(
                                        imageVector = targetIcon.imageVector,
                                        contentDescription = null
                                    )
                                }
                                is IconRepresentation.Drawable -> {
                                    Icon(
                                        painter = painterResource(targetIcon.drawableId),
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        label = { Text(text = stringResource(item.titleTextId)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun NoConnectionUi(
    navigationBarPaddingEnabled: () -> Boolean
) {
    val modifier = if (navigationBarPaddingEnabled())
        Modifier.padding(NavigationBarDefaults.windowInsets.asPaddingValues())
    else Modifier
    Surface(
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Text(
            text = "No connection",
            color = MaterialTheme.colorScheme.onErrorContainer,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier)
                .padding(smallDp)
        )
    }
}