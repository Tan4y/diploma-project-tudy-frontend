package org.tues.tudy.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.tudy.viewmodel.EventViewModel

@Composable
fun SubjectCardWithCount (
    navController: NavController,
    subject: String,
    icon: Painter,
    viewModel: EventViewModel = viewModel()
    ) {
    val count by viewModel.tudiesCount.collectAsState()

    LaunchedEffect(subject) {
        viewModel.loadTudiesByCategory(subject)
    }

    SubjectCard(
        navController = navController,
        value = subject,
        icon = icon,
        numberOfTudies = count
    )
}