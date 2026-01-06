package org.tues.tudy.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.viewmodel.EventViewModel

@Composable
fun TypeCardWithCount(
    navController: NavController,
    category: String,
    icon: Painter,
    viewModel: EventViewModel = viewModel(),
    userId: String
) {
    val count by viewModel.tudiesCount.collectAsState()

    LaunchedEffect(category) {
        viewModel.loadTudiesByCategory(category)
    }

//    TypeCard(
//        navController = navController,
//        value = category,
//        icon = icon,
//        numberOfTudies = count,
//        onClick = {
//            navController.navigate(
//                Routes.typeSubjectPageRoute(userId)
//            )
//        }
//    )
}
