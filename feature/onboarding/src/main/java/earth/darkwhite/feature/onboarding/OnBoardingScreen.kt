package earth.darkwhite.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
internal fun OnBoardingRoute(
    onCreateAccountClick: () -> Unit,
    onSignInClick: () -> Unit,
    viewModel: OnBoardingViewModel = hiltViewModel()
) {
    
    OnBoardingScreen(
    
    )
    
}

@Composable
private fun OnBoardingScreen(
) {

}
