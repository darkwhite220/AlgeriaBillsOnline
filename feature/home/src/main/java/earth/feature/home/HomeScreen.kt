package earth.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun HomeRoute(
    onSettingsClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen(
        onSettingsClick = onSettingsClick
    )
}

@Composable
private fun HomeScreen(
    onSettingsClick: () -> Unit,
) {
    Scaffold(modifier = Modifier) { paddingValues ->
        Column(
            modifier = Modifier
              .padding(paddingValues)
              .fillMaxSize()
        ) {
            Text(text = "HOME")
            OutlinedButton(onClick = onSettingsClick) {
                Text(text = "Settings")
            }
        }
    }
}