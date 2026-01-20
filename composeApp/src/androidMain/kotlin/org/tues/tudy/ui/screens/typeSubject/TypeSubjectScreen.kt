package org.tues.tudy.ui.screens.typeSubject

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import org.tues.tudy.viewmodel.TypeSubjectViewModel
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.viewmodel.EventViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TypeSubjectScreen(
    navController: NavController,
    viewModel: TypeSubjectViewModel,
    eventViewModel: EventViewModel,
    userId: String,
    title: String,
    clickedIsType: Boolean
) {
    LaunchedEffect(userId) {
        viewModel.loadEventsForUser(userId)
    }

    var isMenuOpen by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopBar(
                    mode = TopBarMode.BACK,
                    heading = title,
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
                    selectedRoute = Routes.typeSubjectPageRoute(userId, title, clickedIsType),
                    userId = userId
                )
            },
            containerColor = BaseColor0
        ) { innerPadding ->
            TypeSubjectContent(
                navController = navController,
                viewModel = viewModel,
                eventViewModel = eventViewModel,
                modifier = Modifier
                    .padding(innerPadding),
                userId = userId,
                title = title,
                clickedIsType = clickedIsType,
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