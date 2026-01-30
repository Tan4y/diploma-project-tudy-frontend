package org.tues.tudy.ui.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.tues.tudy.ui.screens.study.StudyScreen
import org.tues.tudy.viewmodel.StudyViewModel

fun NavGraphBuilder.studyNavGraph (navController: NavHostController) {
    composable(
        route = Routes.STUDY,
        arguments = listOf(
            navArgument("userId") { type = NavType.StringType }
        )) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId") ?: ""
        val studyViewModel: StudyViewModel = viewModel()


        StudyScreen(
            navController = navController,
            viewModel = studyViewModel,
            userId = userId
        )
    }
}