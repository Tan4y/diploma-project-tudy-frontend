package org.tues.tudy.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import org.tues.tudy.R
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.ui.theme.PrimaryColor2
import org.tues.tudy.utils.formatTimeSeconds
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun WaveLiquidTimer(
    totalSeconds: Int,
    remainingSeconds: Int,
    phase: StudyPhase,
    modifier: Modifier = Modifier
) {
    val progress = 1f - remainingSeconds.toFloat() / totalSeconds
    val activeColor = if (phase == StudyPhase.STUDYING) PrimaryColor1 else PrimaryColor2
    val timerText = formatTimeSeconds(remainingSeconds)
    val context = LocalContext.current

    // Animate horizontal wave movement
    val infiniteTransition = rememberInfiniteTransition()
    val waveShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing)
        )
    )

    val density = LocalDensity.current.density // get density here

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(280.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val diameter = size.minDimension
            val radius = diameter / 2

            // Draw circle outline
            drawCircle(
                color = activeColor,
                radius = radius,
                style = Stroke(width = 4f)
            )

            // Wave path
            val waveHeight = 10f
            val waveLength = diameter / 1.5f
            val liquidLevel = diameter * (1 - progress)

            val path = Path().apply {
                moveTo(0f, diameter)
                lineTo(0f, liquidLevel)
                for (x in 0..diameter.toInt()) {
                    val y =
                        liquidLevel + sin((x / waveLength + waveShift) * 2 * PI).toFloat() * waveHeight
                    lineTo(x.toFloat(), y)
                }
                lineTo(diameter, diameter)
                close()
            }

            // Clip the circle and draw the wave
            clipPath(Path().apply {
                addOval(
                    androidx.compose.ui.geometry.Rect(
                        0f,
                        0f,
                        diameter,
                        diameter
                    )
                )
            }) {
                drawPath(path, color = activeColor)
            }

            val typefaceValue = ResourcesCompat.getFont(context, R.font.fredoka_bold)

            // Draw text in two layers: behind and in front of wave
            val paintBehind = android.graphics.Paint().apply {
                color = BaseColor0.toArgb()
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = 40 * density
                typeface = typefaceValue
            }
            val paintFront = android.graphics.Paint().apply {
                color = activeColor.toArgb()
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = 40 * density
                typeface = typefaceValue
            }

            // Center the text vertically
            val yPos = diameter / 2 - (paintBehind.descent() + paintBehind.ascent()) / 2
            val padding = 2f

            // Behind the liquid
            clipRect(0f, liquidLevel - padding, diameter, diameter) {
                drawContext.canvas.nativeCanvas.drawText(timerText, diameter / 2, yPos, paintBehind)
            }

            // In front of the liquid
            clipRect(0f, 0f, diameter, liquidLevel + padding) {
                drawContext.canvas.nativeCanvas.drawText(timerText, diameter / 2, yPos, paintFront)
            }
        }
    }
}