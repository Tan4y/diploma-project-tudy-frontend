package org.tues.tudy.ui.screens.study

import org.tues.tudy.R
import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.tues.tudy.ui.components.CustomButton
import org.tues.tudy.ui.components.FlashText
import org.tues.tudy.ui.components.StudyPhase
import org.tues.tudy.ui.components.StudyTimelineProgress
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor40
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius0
import org.tues.tudy.ui.theme.Dimens.BorderRadius200
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.ui.theme.PrimaryColor2
import org.tues.tudy.viewmodel.StudyViewModel
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import kotlinx.coroutines.delay
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import kotlin.math.sin
import kotlin.math.PI
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.res.ResourcesCompat
import org.tues.tudy.ui.theme.PrimaryFont

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

private fun formatTimeSeconds(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}

@Composable
fun StudyContent(
    navController: NavController,
    viewModel: StudyViewModel,
    userId: String,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    val currentSegmentIndex =
        (state.currentRound - 1) * 2 +
                if (state.phase == StudyPhase.STUDYING) 0 else 1

    val toneGen = remember {
        ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80)
    }

    LaunchedEffect(currentSegmentIndex) {
        if (currentSegmentIndex > 0) {
            val tone = if (state.phase == StudyPhase.STUDYING)
                ToneGenerator.TONE_PROP_ACK
            else
                ToneGenerator.TONE_PROP_PROMPT

            toneGen.startTone(tone, 180)
        }
    }

    LaunchedEffect(state.phase) {
        if (state.phase == StudyPhase.FINISHED) {
            toneGen.startTone(ToneGenerator.TONE_PROP_ACK, 120)
            delay(140)
            toneGen.startTone(ToneGenerator.TONE_PROP_ACK, 180)
        }
    }

    val modeColor =
        if (state.phase == StudyPhase.RESTING) PrimaryColor2
        else PrimaryColor1

    when (state.phase) {
        StudyPhase.IDLE -> {
            Column(
                modifier = modifier.padding(
                    horizontal = Dimens.Space100,
                    vertical = Dimens.Space125
                ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier.wrapContentWidth()
                ) {
                    SubcomposeLayout { constraints ->
                        val innerColumnPlaceable = subcompose("inner") {
                            Column(
                                modifier = Modifier
                                    .border(
                                        width = 2.dp,
                                        color = PrimaryColor1,
                                        shape = RoundedCornerShape(
                                            bottomStart = BorderRadius200,
                                            bottomEnd = BorderRadius200,
                                            topStart = BorderRadius0,
                                            topEnd = BorderRadius0
                                        )
                                    )
                                    .padding(
                                        start = Dimens.Space125,
                                        end = Dimens.Space125,
                                        bottom = Dimens.Space125,
                                        top = Dimens.Space175,
                                    ),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.spacedBy(Dimens.Space150)
                            ) {
                                FlashText(text = "Study for 15 minutes")
                                FlashText(text = "Rest for 3 minutes")
                                FlashText(text = "Repeat 4 times")
                                FlashText(text = "Do not browse your phone")
                            }
                        }[0].measure(constraints)

                        val boxPlaceable = subcompose("box") {
                            Box(
                                modifier = Modifier
                                    .width(innerColumnPlaceable.width.toDp())
                                    .background(
                                        color = PrimaryColor1,
                                        shape = RoundedCornerShape(
                                            topStart = BorderRadius200,
                                            topEnd = BorderRadius200,
                                            bottomStart = BorderRadius0,
                                            bottomEnd = BorderRadius0
                                        )
                                    )
                                    .padding(Dimens.Space125),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Focus Mode",
                                    style = AppTypography.Heading4,
                                    color = BaseColor0
                                )
                            }
                        }[0].measure(constraints)

                        layout(
                            maxOf(boxPlaceable.width, innerColumnPlaceable.width),
                            boxPlaceable.height + innerColumnPlaceable.height
                        ) {
                            boxPlaceable.place(0, 0)
                            innerColumnPlaceable.place(0, boxPlaceable.height)
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(contentAlignment = Alignment.BottomCenter) {
                    CustomButton(
                        value = "Start",
                        enabled = true,
                        onClick = { viewModel.startSession() }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.giraffe_head_with_sunglasses),
                        contentDescription = "Giraffe head",
                        modifier = Modifier
                            .size(104.dp)
                            .align(Alignment.TopEnd)
                            .graphicsLayer { translationY = -28.dp.toPx() }
                            .zIndex(1f)
                    )
                }
            }
        }

        StudyPhase.STUDYING,
        StudyPhase.RESTING -> {
            val progress =
                1f - (state.remainingSeconds.toFloat() / state.totalSeconds)

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(22.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.Space250)
                ) {

                    Box (contentAlignment = Alignment.Center) {
                    WaveLiquidTimer(
                        totalSeconds = state.totalSeconds,
                        remainingSeconds = state.remainingSeconds,
                        phase = state.phase,
                        //modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                        if( state.phase == StudyPhase.STUDYING) {
                        Image(
                            painter = painterResource(id = R.drawable.giraffe_head_with_sunglasses),
                            contentDescription = "Giraffe head",
                            modifier = Modifier
                                .size(104.dp)
                                .align(Alignment.TopCenter)
                                .graphicsLayer { translationY = -81.dp.toPx() }
                                .zIndex(1f)
                        )} else {
                            Image(
                                painter = painterResource(id = R.drawable.giraffe_resting),
                                contentDescription = "Giraffe head",
                                modifier = Modifier
                                    .size(104.dp)
                                    .align(Alignment.TopCenter)
                                    .graphicsLayer { translationY = -68.dp.toPx(); translationX = 24.dp.toPx() }
                                    .zIndex(1f))
                        }
                    }


                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimens.Space50),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (state.phase == StudyPhase.STUDYING)
                                "Study"
                            else
                                "Rest",
                            style = AppTypography.Heading6,
                            color = BaseColor100,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "Take it easy. Each step comes with a helpful beep sound.",
                            style = AppTypography.Caption1,
                            color = BaseColor100
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Column {
                    val segments = remember { viewModel.buildSegments(state.maxRounds) }

                    val progress =
                        1f - (state.remainingSeconds.toFloat() / state.totalSeconds)

                    StudyTimelineProgress(
                        segments = segments,
                        currentIndex = currentSegmentIndex,
                        progressInCurrent = progress,
                        isStudyPhase = state.phase == StudyPhase.STUDYING,
                        modifier = Modifier.fillMaxWidth()
                    )



                    Spacer(modifier = Modifier.height(Dimens.Space125))

                    CustomButton(
                        value = if (state.phase == StudyPhase.STUDYING) "I am done" else if (state.currentRound == currentSegmentIndex / 2) "Finish" else "Continue studying",
                        enabled = true,
                        onClick = { viewModel.nextPhase() },
                        color = modeColor
                    )
                }
            }
        }

        StudyPhase.FINISHED -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(Dimens.Space100),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Session completed!",
                    style = AppTypography.Heading3,
                    color = PrimaryColor1
                )

                Spacer(modifier = Modifier.weight(1f))

                CustomButton(
                    value = "Go Back",
                    enabled = true,
                    onClick = { navController.navigate(Routes.studyRoute(userId)) },
                )
            }
        }
    }
}
