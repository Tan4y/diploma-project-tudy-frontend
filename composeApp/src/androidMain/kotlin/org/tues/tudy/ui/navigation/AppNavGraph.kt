package org.tues.tudy.ui.navigation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.CREATE_ACCOUNT
    ) {
        authNavGraph(navController)
        homeNavGraph(navController)
        addTudyNavGraph(navController)
        typeSubjectPageNavGraph(navController)
    }
}