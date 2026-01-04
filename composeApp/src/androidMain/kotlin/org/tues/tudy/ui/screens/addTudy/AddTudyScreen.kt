package org.tues.tudy.ui.screens.addTudy

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.tudy.ui.components.BottomBar
import org.tues.tudy.ui.components.TopBar
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.screens.home.HomeContent
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.viewmodel.AddTudyViewModel
import org.tues.tudy.viewmodel.HomeViewModel

@Composable
fun AddTudyScreen (
    navController: NavController,
    viewModel: AddTudyViewModel,
    homeViewModel: HomeViewModel,
    userId: String
) {
    Scaffold(
        topBar = {
            TopBar(
                primary = true,
                heading = "Add Tudy",
                navController = navController
            )
        },
        bottomBar = {
            BottomBar(navController = navController, selectedRoute = Routes.ADD_TUDY)
        },
        containerColor = BaseColor0
    ) { innerPadding ->
        AddTudyContent(
            navController = navController,
            viewModel = viewModel,
            homeViewModel = homeViewModel,
            modifier = Modifier
                .padding(innerPadding),
            userId = userId
        )
    }

}