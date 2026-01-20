package org.tues.tudy.ui.screens.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import org.tues.tudy.ui.components.BottomBar
import org.tues.tudy.ui.components.TopBar
import org.tues.tudy.ui.components.TopBarMode
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.screens.moreMenu.MoreMenuScreen
import org.tues.tudy.ui.screens.moreMenu.OpenMoreMenu
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.viewmodel.EventViewModel
import org.tues.tudy.viewmodel.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel,
    eventViewModel: EventViewModel,
    userId: String
) {
    LaunchedEffect(userId) {
        viewModel.ensureLoaded(userId)
        eventViewModel.loadEvents(userId)
    }

    var isMenuOpen by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopBar(
                    mode = TopBarMode.MENU,
                    heading = "Home",
                    navController = navController,
                    userId = userId,
                    isMenuOpen = isMenuOpen,
                    onMenuClick = {
                        isMenuOpen = !isMenuOpen
                    }
                )
            },
            bottomBar = {
                BottomBar(
                    navController = navController,
                    selectedRoute = Routes.homeRoute(userId),
                    userId = userId
                )
            },
            containerColor = BaseColor0
        ) { innerPadding ->
            HomeContent(
                navController = navController,
                viewModel = viewModel,
                eventViewModel = eventViewModel,
                modifier = Modifier
                    .padding(innerPadding),
                userId = userId,
            )
        }
        OpenMoreMenu(
            navController = navController,
            userId = userId,
            isMenuOpen = isMenuOpen,
            onClick = { isMenuOpen = false }
        )
    }
}