package earth.darkwhite.feature.signin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import earth.darkwhite.feature.signin.SignInRoute

const val signInRoute = "sign_in_route"

fun NavController.navigateToSignIn(navOptions: NavOptions? = null) {
    this.navigate(route = signInRoute, navOptions = navOptions)
}

fun NavGraphBuilder.signInScreen(
    onBackClick: () -> Unit,
) {
    composable(route = signInRoute) {
        SignInRoute(
            onBackClick = onBackClick
        )
    }
}