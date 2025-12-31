package org.tues.tudy.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.tudy.viewmodel.HomeViewModel
import androidx.compose.material3.Scaffold
import androidx.compose.ui.unit.dp
import org.tues.tudy.ui.components.BottomBar
import org.tues.tudy.ui.components.TopBar
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.theme.BaseColor0

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {

    Scaffold(
        topBar = {
            TopBar(
                primary = true,
                heading = "Home",
                navController = navController
            )
        },
        bottomBar = {
            BottomBar(navController = navController, selectedRoute = Routes.HOME)
        },
        containerColor = BaseColor0
    ) { innerPadding ->
        HomeContent(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}
