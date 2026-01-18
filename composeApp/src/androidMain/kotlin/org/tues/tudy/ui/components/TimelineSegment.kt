package org.tues.tudy.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import org.tues.tudy.ui.theme.Dimens

@Composable
fun TimelineSegment(
    progress: Float,
    activeColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.height(Dimens.Space50)) {
        val radius = size.height / 2

        // background
        drawRoundRect(
            color = backgroundColor,
            cornerRadius = CornerRadius(radius, radius)
        )

        // progress
        if (progress > 0f) {
            drawRoundRect(
                color = activeColor,
                size = size.copy(width = size.width * progress),
                cornerRadius = CornerRadius(radius, radius)
            )
        }
    }
}

