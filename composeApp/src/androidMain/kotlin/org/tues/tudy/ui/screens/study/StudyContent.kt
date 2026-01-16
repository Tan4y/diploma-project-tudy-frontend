package org.tues.tudy.ui.screens.study

import android.R
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
import androidx.compose.ui.Alignment
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

private fun formatTime(seconds: Int): String {
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
    val context = LocalContext.current
    //val soundPlayer = remember { SoundPlayer(context) }

    val state by viewModel.uiState.collectAsState()

    val currentSegmentIndex =
        (state.currentRound - 1) * 2 +
                if (state.phase == StudyPhase.STUDYING) 0 else 1

    val toneGen = remember {
        ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80)
    }

    LaunchedEffect(currentSegmentIndex) {
        if (currentSegmentIndex >= 0) {
            toneGen.startTone(
                ToneGenerator.TONE_PROP_BEEP,
                150
            )
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

                CustomButton(
                    value = "Start",
                    enabled = true,
                    onClick = { viewModel.startSession() }
                )
            }
        }

        StudyPhase.STUDYING,
        StudyPhase.RESTING -> {
            val progress =
                1f - (state.remainingSeconds.toFloat() / state.totalSeconds)

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (state.phase == StudyPhase.STUDYING)
                            "Study"
                        else
                            "Rest",
                        style = AppTypography.Heading4,
                        color = BaseColor100,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = formatTime(state.remainingSeconds),
                        style = MaterialTheme.typography.displaySmall,
                        color = modeColor
                    )

                    Text(
                        text = "Round ${state.currentRound} / ${state.maxRounds}"
                    )
                }

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


                    Spacer(modifier = Modifier.height(16.dp))

                    CustomButton(
                        value = if (state.phase == StudyPhase.STUDYING) "I am done" else "Continue studying",
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
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Session completed 🎉",
                    style = MaterialTheme.typography.headlineMedium
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
