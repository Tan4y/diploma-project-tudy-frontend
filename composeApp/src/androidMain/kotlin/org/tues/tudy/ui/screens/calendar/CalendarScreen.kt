package org.tues.tudy.ui.screens.calendar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.tues.tudy.ui.components.BottomBar
import org.tues.tudy.ui.components.TopBar
import org.tues.tudy.ui.components.TopBarMode
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.viewmodel.CalendarViewModel

@Composable
fun CalendarScreen(
    navController: NavController,
    userId: String,
    viewModel: CalendarViewModel
) {
    val days by viewModel.days.collectAsState()
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val month by viewModel.currentMonth.collectAsState()

    LaunchedEffect(month) {
        viewModel.loadMonth(month)
    }

    Scaffold(
        topBar = {
            TopBar(
                mode = TopBarMode.MENU,
                heading = "Calendar",
                navController = navController
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                selectedRoute = Routes.calendarRoute(userId),
                userId = userId
            )
        },
        containerColor = BaseColor0
    ) { innerPadding ->
        CalendarContent(
            navController = navController,
            days = days,
            selectedMonth = selectedMonth,
            events = emptyList(),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            viewModel = viewModel,
            userId = userId
        )
    }
}

