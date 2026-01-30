package org.tues.tudy.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.tues.tudy.ui.components.BottomBar
import org.tues.tudy.ui.components.TopBar
import org.tues.tudy.ui.components.TopBarMode
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.screens.moreMenu.OpenMoreMenu
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen (
    navController: NavController,
    viewModel: ProfileViewModel,
    userId: String
) {
    var isMenuOpen by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopBar(
                    mode = TopBarMode.MENU,
                    heading = "Profile",
                    navController = navController,
                    isMenuOpen = isMenuOpen,
                    onMenuClick = {
                        isMenuOpen = !isMenuOpen
                    }
                )
            },
            bottomBar = {
                BottomBar(
                    navController = navController,
                    selectedRoute = Routes.profileRoute(userId),
                    userId = userId
                )
            },
            containerColor = BaseColor0
        ) { innerPadding ->
            ProfileContent(
                viewModel = viewModel,
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