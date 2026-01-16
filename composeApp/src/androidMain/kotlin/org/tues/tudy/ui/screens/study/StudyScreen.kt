package org.tues.tudy.ui.screens.study

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.tues.tudy.ui.components.BottomBar
import org.tues.tudy.ui.components.TopBar
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.screens.addTudy.AddTudyContent
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.viewmodel.StudyViewModel

@Composable
fun StudyScreen (
    navController: NavController,
    viewModel: StudyViewModel,
    userId: String
) {
    Scaffold(
        topBar = {
            TopBar(
                primary = true,
                heading = "Focus",
                navController = navController
            )
        },
        bottomBar = {
            BottomBar(navController = navController, selectedRoute = Routes.studyRoute(userId), userId = userId)
        },
        containerColor = BaseColor0
    ) { innerPadding ->
        StudyContent(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier
                .padding(innerPadding),
            userId = userId
        )
    }
}