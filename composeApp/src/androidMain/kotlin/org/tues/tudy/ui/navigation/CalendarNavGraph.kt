package org.tues.tudy.ui.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.repository.CalendarRepository
import org.tues.tudy.ui.screens.calendar.MonthlyCalendarScreen
import org.tues.tudy.viewmodel.CalendarViewModel
import org.tues.tudy.viewmodel.CalendarViewModelFactory


fun NavGraphBuilder.calendarNavGraph(
    navController: NavHostController
)  {
    composable(
        route = Routes.CALENDAR_WITH_USER
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId") ?: return@composable

        // Create repository instance (or inject if using DI)
        val repository = CalendarRepository(ApiServiceBuilder.apiService)

        // Provide the factory to viewModel()
        val calendarViewModel: CalendarViewModel = viewModel(
            factory = CalendarViewModelFactory(repository, userId)
        )

        MonthlyCalendarScreen(
            navController = navController,
            userId = userId,
            viewModel = calendarViewModel
        )
    }

    composable(
        route = Routes.CALENDAR_WEEK
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
        val repository = CalendarRepository(ApiServiceBuilder.apiService)
        val calendarViewModel: CalendarViewModel = viewModel(
            factory = CalendarViewModelFactory(repository, userId)
        )

        WeeklyCalendarScreen(
            navController = navController,
            userId = userId,
            viewModel = calendarViewModel
        )
    }
}


