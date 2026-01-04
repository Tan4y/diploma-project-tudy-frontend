package org.tues.tudy.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.tues.tudy.ui.components.BottomBar
import org.tues.tudy.ui.components.TopBar
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel,
    userId: String
) {
    val items by viewModel.items.collectAsState()

    LaunchedEffect(userId) {
        Log.d("HomeScreen", "HomeScreen userId='$userId'")
        viewModel.ensureLoaded(userId)
    }

    Scaffold(
        topBar = {
            TopBar(
                primary = true,
                heading = "Home",
                navController = navController
            )
        },
        bottomBar = {
            BottomBar(navController = navController, selectedRoute = Routes.homeRoute(userId), userId = userId)
        },
        containerColor = BaseColor0
    ) { innerPadding ->
        HomeContent(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier
                .padding(innerPadding),
            userId = userId,
            items = items
        )
    }
}
