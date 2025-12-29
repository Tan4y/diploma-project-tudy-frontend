package org.tues.tudy.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.shadow1(
    radius: Dp = 16.dp,
    blur: Float = 16f,
    color: Color = Color.Black.copy(alpha = 0.07f)
) = this.drawBehind {

    val paint = Paint()
    val frameworkPaint = paint.asFrameworkPaint()

    frameworkPaint.color = android.graphics.Color.argb(
        (color.alpha * 255).toInt(),
        0, 0, 0
    )

    frameworkPaint.maskFilter =
        android.graphics.BlurMaskFilter(blur, android.graphics.BlurMaskFilter.Blur.NORMAL)

    drawIntoCanvas { canvas ->
        canvas.drawRoundRect(
            0f,
            0f,
            size.width,
            size.height,
            radius.toPx(),
            radius.toPx(),
            paint
        )
    }
}