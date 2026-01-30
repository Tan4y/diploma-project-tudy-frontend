package org.tues.tudy.ui.screens.study

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.tues.tudy.ui.components.BasePopUp
import org.tues.tudy.ui.components.BottomBar
import org.tues.tudy.ui.components.StudyPhase
import org.tues.tudy.ui.components.TopBar
import org.tues.tudy.ui.components.TopBarMode
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.screens.moreMenu.OpenMoreMenu
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.viewmodel.StudyViewModel

@Composable
fun StudyScreen(
    navController: NavController,
    viewModel: StudyViewModel,
    userId: String
) {
    val state by viewModel.uiState.collectAsState()
    var isMenuOpen by rememberSaveable { mutableStateOf(false) }
    var showExitPopup by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopBar(
                    mode = if (state.phase == StudyPhase.IDLE) TopBarMode.MENU else TopBarMode.CLOSE,
                    heading = "Focus",
                    navController = navController,
                    onClose = { showExitPopup = true },
                    isMenuOpen = isMenuOpen,
                    onMenuClick = {
                        isMenuOpen = !isMenuOpen
                    }
                )
            },
            bottomBar = {
                if (state.phase == StudyPhase.IDLE) {
                    BottomBar(
                        navController = navController,
                        selectedRoute = Routes.studyRoute(userId),
                        userId = userId
                    )
                }
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
        OpenMoreMenu(
            navController = navController,
            userId = userId,
            isMenuOpen = isMenuOpen,
            onClick = { isMenuOpen = false }
        )
        if (showExitPopup) {
            BasePopUp(
                title = "End Session",
                description = "End your session? Current study time will not be saved.",
                buttonText = "End Session",
                onDismiss = { showExitPopup = false },
                onConfirm = {
                    showExitPopup = false
                    navController.navigate(Routes.studyRoute(userId))
                }
            )
        }
    }
}