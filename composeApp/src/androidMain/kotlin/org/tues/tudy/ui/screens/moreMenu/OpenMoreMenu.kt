package org.tues.tudy.ui.screens.moreMenu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.tudy.ui.theme.BaseColor100

@Composable
fun OpenMoreMenu (
    navController: NavController,
    userId: String,
    isMenuOpen: Boolean,
    onClick: () -> Unit
) {
    if (isMenuOpen) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BaseColor100.copy(alpha = 0.6f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onClick()
                }
                .zIndex(5f)
        )
    }

    AnimatedVisibility(
        visible = isMenuOpen,
        modifier = Modifier.fillMaxSize().zIndex(10f)
    ) {
        MoreMenuScreen(
            navController = navController,
            viewModel = viewModel(),
            userId = userId,
            onClick = { onClick() },
            modifier = Modifier.padding(end = 16.dp),
            visible = isMenuOpen
        )
    }
}