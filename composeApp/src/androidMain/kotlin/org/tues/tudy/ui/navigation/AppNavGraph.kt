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

        composable(
            route = "successError/{title}/{subtitle}/{description}/{buttonText}/{arrow}/{success}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("subtitle") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("buttonText") { type = NavType.StringType },
                navArgument("arrow") { type = NavType.BoolType },
                navArgument("success") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val subtitle = backStackEntry.arguments?.getString("subtitle") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            val buttonText = backStackEntry.arguments?.getString("buttonText") ?: ""
            val arrow = backStackEntry.arguments?.getBoolean("arrow") ?: false
            val success = backStackEntry.arguments?.getBoolean("success") ?: false

            SuccessErrorScreen(
                title = title,
                subtitle = subtitle,
                description = description,
                buttonText = buttonText,
                arrow = arrow,
                success = success,
                onButtonClick = {},
                onArrowClick = {}
            )
        }
    }
}
