package earth.darkwhite.feature.estimate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import earth.core.designsystem.components.largeDp
import earth.core.designsystem.components.topappbar.CenteredTopAppBar
import earth.feature.estimate.R

@Composable
internal fun EstimateRoute(
    onSettingsClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: EstimateViewModel = hiltViewModel()
) {
    EstimateScreen(
        onSettingsClick = onSettingsClick,
        onBackClick = onBackClick,
    )
}

@Preview(showBackground = true)
@Composable
private fun EstimateScreen(
    onSettingsClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CenteredTopAppBar(
            titleId = R.string.estimate,
            onBackClick = onBackClick,
        )
        
        Box(modifier = Modifier.padding(largeDp)) {
            Text(text = "Estimate")
        }
    }
}