package earth.darkwhite.feature.signin

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SignInRoute(
    onBackClick: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    SignInScreen()
}

@Composable
fun SignInScreen() {


}
