package org.tues.tudy.ui.screens.typeSubject

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
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.viewmodel.TypeSubjectViewModel
import org.tues.tudy.ui.theme.BaseColor0

@Composable
fun TypeSubjectScreen(
    navController: NavController,
    viewModel: TypeSubjectViewModel,
    userId: String,
    title: String,
    clickedIsType: Boolean
) {
    LaunchedEffect(userId) {
        viewModel.loadEventsForUser(userId)
    }

    Scaffold(
        topBar = {
            TopBar(
                primary = false,
                heading = title,
                navController = navController
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                selectedRoute = Routes.typeSubjectPageRoute(userId, title, clickedIsType),
                userId = userId
            )
        },
        containerColor = BaseColor0
    ) { innerPadding ->
        val allEvents by viewModel.events.collectAsState()
        TypeSubjectContent(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier
                .padding(innerPadding),
            userId = userId,
            title = title,
            clickedIsType = clickedIsType,
            events = allEvents
        )
    }
}