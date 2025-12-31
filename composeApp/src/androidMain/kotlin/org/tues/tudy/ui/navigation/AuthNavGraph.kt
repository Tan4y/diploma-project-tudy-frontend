package org.tues.tudy.ui.navigation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import org.tues.tudy.ui.screens.auth.CreateAccountScreen
import org.tues.tudy.ui.screens.auth.EmailVerificationScreen
import org.tues.tudy.ui.screens.auth.ForgotPasswordUsernameScreen
import org.tues.tudy.ui.screens.auth.LogInScreen
import org.tues.tudy.ui.screens.auth.ResetPasswordScreen
import org.tues.tudy.ui.common.SuccessErrorScreen
import org.tues.tudy.viewmodel.ResetPasswordViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
fun NavGraphBuilder.authNavGraph(navController: NavHostController) {

        composable(Routes.CREATE_ACCOUNT) {
            CreateAccountScreen(navController)
        }

        composable(Routes.LOGIN) {
            LogInScreen(navController)
        }

        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordUsernameScreen(navController)
        }

        composable(
            route = Routes.RESET_PASSWORD,
        ) {
            val viewModel: ResetPasswordViewModel = viewModel()
            ResetPasswordScreen(navController = navController, viewModel = viewModel)
        }



        // Email verification route
        composable(
            route = "${Routes.EMAIL_VERIFICATION}?token={token}",
            arguments = listOf(
                navArgument("token") {
                    type = NavType.StringType
                    nullable = true
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "http://10.0.2.2:5050/api/auth/verify-email?token={token}"
                }
            )
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")
            EmailVerificationScreen(navController, token)
        }

        composable(
            route = "${Routes.SUCCESS_ERROR}/{title}/{subtitle}/{description}/{buttonText}/{buttonDest}/{arrow}/{arrowDest}/{success}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("subtitle") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType },
                navArgument("buttonText") { type = NavType.StringType },
                navArgument("buttonDest") { type = NavType.StringType },
                navArgument("arrow") { type = NavType.BoolType },
                navArgument("arrowDest") { type = NavType.StringType },
                navArgument("success") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            fun decode(value: String) = URLDecoder.decode(value, StandardCharsets.UTF_8.toString())

            val title = backStackEntry.arguments?.getString("title") ?.let { decode(it) } ?: ""
            val subtitle = backStackEntry.arguments?.getString("subtitle") ?.let { decode(it) } ?: ""
            val description = backStackEntry.arguments?.getString("description") ?.let { decode(it) } ?: ""
            val buttonText = backStackEntry.arguments?.getString("buttonText") ?.let { decode(it) } ?: ""
            val buttonDest = backStackEntry.arguments?.getString("buttonDest") ?.let { decode(it) } ?: ""
            val arrow = backStackEntry.arguments?.getBoolean("arrow") ?: false
            val arrowDest = backStackEntry.arguments?.getString("arrowDest")?.let { decode(it) } ?: ""
            val success = backStackEntry.arguments?.getBoolean("success") ?: false

            SuccessErrorScreen(
                navController = navController,
                title = title,
                subtitle = subtitle,
                description = description,
                buttonText = buttonText,
                arrow = arrow,
                success = success,
                buttonDestination = buttonDest,
                arrowDestination = arrowDest,
            )
        }
    }
