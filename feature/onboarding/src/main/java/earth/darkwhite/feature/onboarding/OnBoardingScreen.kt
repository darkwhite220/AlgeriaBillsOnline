package earth.darkwhite.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
internal fun OnBoardingRoute(
    viewModel: OnBoardingViewModel = hiltViewModel()
) {
    viewModel.onStartClick()
    OnBoardingScreen(
    
    )
}

@Composable
private fun OnBoardingScreen(
) {

}
