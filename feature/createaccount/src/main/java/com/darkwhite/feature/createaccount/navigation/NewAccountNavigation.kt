package com.darkwhite.feature.createaccount.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.darkwhite.feature.createaccount.CreateAccountRoute

const val createAccountRoute = "create_account_route"

fun NavController.navigateToCreateAccount(navOptions: NavOptions? = null) {
    this.navigate(route = createAccountRoute, navOptions = navOptions)
}

fun NavGraphBuilder.createAccountScreen(
    onAccountCreated: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable(route = createAccountRoute) {
        CreateAccountRoute(
            onAccountCreated = onAccountCreated,
            onBackClick = onBackClick,
        )
    }
}