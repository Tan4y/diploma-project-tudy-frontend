package org.tues.tudy.ui.navigation

import android.net.Uri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.tues.tudy.ui.screens.typeSubject.TypeSubjectScreen
import org.tues.tudy.viewmodel.TypeSubjectViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun NavGraphBuilder.typeSubjectPageNavGraph (navController: NavHostController){
    composable(
    route = Routes.TYPE_SUBJECT,
        arguments = listOf(
            navArgument("userId") { type = NavType.StringType },
            navArgument("title") {
                type = NavType.StringType
            },
            navArgument("clickedIsType") {
                type = NavType.BoolType
                defaultValue = true
            }
    )) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId") ?: ""
        val encodedTitle = backStackEntry.arguments?.getString("title") ?: ""
        val clickedIsType = backStackEntry.arguments?.getBoolean("clickedIsType") ?: true

        val title = URLDecoder.decode(encodedTitle, StandardCharsets.UTF_8.toString())

        val typeSubjectViewModel: TypeSubjectViewModel = viewModel()


        TypeSubjectScreen(
            navController = navController,
            viewModel = typeSubjectViewModel,
            userId = userId,
            title = title,
            clickedIsType = clickedIsType
        )
    }
}