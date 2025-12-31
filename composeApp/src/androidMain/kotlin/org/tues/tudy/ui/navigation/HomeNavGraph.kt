package org.tues.tudy.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.tues.tudy.ui.screens.home.HomeScreen

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {

        composable(Routes.HOME) {
            HomeScreen(navController)
        }
    }
