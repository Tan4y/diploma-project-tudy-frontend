package org.tues.tudy.ui.navigation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import org.tues.tudy.ui.auth.CreateAccountScreen
import org.tues.tudy.ui.auth.EmailVerificationScreen
import org.tues.tudy.ui.auth.LogInScreen
import org.tues.tudy.ui.common.SuccessErrorScreen

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
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
            LogInScreen(navController)
        }

        composable(
            route = "verifyEmail?token={token}",
            arguments = listOf(
                navArgument("token") {
                    type = NavType.StringType
                    nullable = true
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "http://10.0.2.2:5050/api/auth/verify-email?token={token}"
                },
                navDeepLink {
                    uriPattern = "https://yourdomain.com/api/auth/verify-email?token={token}"
                }
            )
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")
            EmailVerificationScreen(navController, token)
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
