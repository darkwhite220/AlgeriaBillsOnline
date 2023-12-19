package earth.darkwhite.algeriabills.ui

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import earth.darkwhite.algeriabills.R
import earth.darkwhite.algeriabills.ui.navigation.ModularizationNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleApp(
    appState: AppState = rememberAppState()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        }
    ) { paddingValues ->
        ModularizationNavHost(
            appState = appState,
            modifier = Modifier,
            paddingValues = paddingValues,
        )
    }
}