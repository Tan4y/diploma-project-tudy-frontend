package org.tues.tudy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.tues.tudy.ui.auth.CreateAccountScreen
import org.tues.tudy.ui.auth.LoginScreen
import org.tues.tudy.ui.common.SuccessErrorScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "createAccount"
    ) {
        composable("createAccount") {
            CreateAccountScreen(navController)
        }

        composable("login") {
            LoginScreen(navController)
        }

        composable("success") {
            SuccessErrorScreen(
                title = "Create Account",
                subtitle = "Account Created!",
                description = "Your account has been successfully registered.",
                buttonText = "Log in",
                arrow = false,
                success = true,
                onButtonClick = {},
                onArrowClick = {}
            )
        }

        composable("error") {
            SuccessErrorScreen(
                title = "Create Account",
                subtitle = "Create Account Unsuccessful!",
                description = "There was an error while trying create account.",
                buttonText = "Try Again",
                arrow = false,
                success = false,
                onButtonClick = {},
                onArrowClick = {}
            )
        }
    }
}
