package org.tues.tudy.ui.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.repository.CalendarRepository
import org.tues.tudy.ui.screens.calendar.CalendarScreen
import org.tues.tudy.viewmodel.CalendarViewModel
import org.tues.tudy.viewmodel.CalendarViewModelFactory


fun NavGraphBuilder.calendarNavGraph(
    navController: NavHostController
) {
    composable(
        route = Routes.CALENDAR_WITH_USER
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
        val repository = CalendarRepository(ApiServiceBuilder.apiService)
        val calendarViewModel: CalendarViewModel = viewModel(
            factory = CalendarViewModelFactory(repository, userId)
        )

        CalendarScreen(
            navController = navController,
            userId = userId,
            viewModel = calendarViewModel
        )
    }
}


