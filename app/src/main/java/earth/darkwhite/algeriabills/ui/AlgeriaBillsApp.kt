package earth.darkwhite.algeriabills.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import earth.core.data.util.NetworkMonitorRepository
import earth.core.designsystem.components.smallDp
import earth.darkwhite.algeriabills.R
import earth.darkwhite.algeriabills.ui.navigation.AlgeriaBillsNavHost
import earth.darkwhite.algeriabills.ui.navigation.IconRepresentation

@Composable
fun AlgeriaBillsApp(
    networkMonitor: NetworkMonitorRepository,
    appState: AppState = rememberAppState(
        networkMonitor = networkMonitor
    ),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val currentDestination = appState.currentTopLevelDestination
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    
    val notConnectedMessage = stringResource(R.string.not_connected)
    LaunchedEffect(isOffline) {
        if (isOffline) {
//            snackbarHostState.showSnackbar(
//                message = notConnectedMessage,
//                duration = SnackbarDuration.Indefinite,
//            )
        }
    }
    
    // TODO add padding to layout when snackbar is on
    // TODO bottomBar background like nia
    
    Scaffold(
        bottomBar = {
//            Column {
//                if (isOffline) {
//                    NoConnectionUi()
//                }
            if (currentDestination != null) {
                NavigationBar {
                    appState.topLevelDestination.forEach { item ->
                        val isSelected = appState.currentTopLevelDestination == item
                        val targetIcon = if (isSelected) item.selectedIcon else item.unselectedIcon
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
//            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .padding(paddingValues)
//                .consumeWindowInsets(paddingValues)
//        ) {
        AlgeriaBillsNavHost(
            appState = appState,
            modifier = Modifier,//.weight(1f),
            paddingValues = paddingValues,//PaddingValues(0.dp),
        )
//            if (isOffline) {
//                Spacer(modifier = Modifier.padding(54.dp))
//            }
//        }
    }
}

@Composable
private fun NoConnectionUi() {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Text(
            text = "No connection",
            color = MaterialTheme.colorScheme.onErrorContainer,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(smallDp)
        )
    }
}