package org.tues.tudy.ui.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.tues.tudy.ui.screens.profile.ProfileScreen
import org.tues.tudy.viewmodel.ProfileViewModel

fun NavGraphBuilder.profileNavGraph (navController: NavHostController) {
    composable(
        route = Routes.PROFILE,
        arguments = listOf(
            navArgument("userId") { type = NavType.StringType }
        )
    ) { backStackEntry ->

        val userId = backStackEntry.arguments?.getString("userId") ?: ""

        val profileViewModel: ProfileViewModel = viewModel()

        ProfileScreen(
            navController = navController,
            viewModel = profileViewModel,
            userId = userId
        )
    }
}