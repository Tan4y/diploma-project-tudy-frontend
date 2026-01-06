package org.tues.tudy.ui.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.tues.tudy.ui.screens.addTudy.AddTudyScreen
import org.tues.tudy.viewmodel.AddTudyViewModel
import org.tues.tudy.viewmodel.HomeViewModel

fun NavGraphBuilder.addTudyNavGraph(navController: NavHostController) {
    composable(
        route = Routes.ADD_TUDY_WITH_USER,
        arguments = listOf(
            navArgument("userId") { type = NavType.StringType }
        )) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId") ?: ""
        val addTudyViewModel: AddTudyViewModel = viewModel()
        val homeViewModel: HomeViewModel = viewModel()


        AddTudyScreen(
            navController = navController,
            viewModel = addTudyViewModel,
            homeViewModel = homeViewModel,
            userId = userId
        )
    }
}