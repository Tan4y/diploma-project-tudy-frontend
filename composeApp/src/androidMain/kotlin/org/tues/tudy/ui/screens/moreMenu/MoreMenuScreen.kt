package org.tues.tudy.ui.screens.moreMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius250
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.ui.theme.shadow1
import org.tues.tudy.viewmodel.MoreMenuViewModel

@Composable
fun MoreMenuScreen (
    navController: NavController,
    viewModel: MoreMenuViewModel,
    userId: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = true
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow1()
            .clip(
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = BorderRadius250,
                    bottomEnd = BorderRadius250,
                    bottomStart = 0.dp
                )
            )
            .background(BaseColor0)
            .border(
                width = 1.dp,
                color = PrimaryColor1,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = BorderRadius250,
                    bottomEnd = BorderRadius250,
                    bottomStart = 0.dp
                )
            )
            .padding(end = Dimens.Space100)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
    ) {

        MoreMenuContent(
            navController = navController,
            viewModel = viewModel,
            userId = userId,
            onClick = { onClick() },
            modifier = modifier,
            visible = visible
        )
    }
}