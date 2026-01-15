package org.tues.tudy.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HourGrid(
    startHour: Int,
    endHour: Int,
    hourHeightDp: Dp
) {
    Column {
        repeat(endHour - startHour + 1) {
            Spacer(
                modifier = Modifier
                    .height(hourHeightDp)
            )
        }
    }
}