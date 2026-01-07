package org.tues.tudy.ui.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.tues.tudy.ui.screens.home.HomeScreen
import org.tues.tudy.viewmodel.EventViewModel
import org.tues.tudy.viewmodel.HomeViewModel

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {

    composable(
        route = Routes.HOME_WITH_USER,
        arguments = listOf(
            navArgument("userId") { type = NavType.StringType }
        )
    ) { backStackEntry ->

        val userId = backStackEntry.arguments?.getString("userId") ?: ""

        val homeViewModel: HomeViewModel =
            androidx.lifecycle.viewmodel.compose.viewModel(backStackEntry)
        val eventViewModel: EventViewModel = viewModel()

        HomeScreen(
            navController = navController,
            viewModel = homeViewModel,
            eventViewModel = eventViewModel,
            userId = userId
        )
    }
}
