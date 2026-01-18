package org.tues.tudy.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tues.tudy.data.model.ProgressSegment
import org.tues.tudy.data.model.SegmentType
import org.tues.tudy.ui.theme.BaseColor40
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.ui.theme.PrimaryColor2

@Composable
fun StudyTimelineProgress(
    segments: List<ProgressSegment>,
    currentIndex: Int,
    progressInCurrent: Float, // 0f..1f
    modifier: Modifier = Modifier,
    isStudyPhase: Boolean,
) {
    val activeColor = if (isStudyPhase) PrimaryColor1 else PrimaryColor2

    Row (
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        segments.forEachIndexed { index, segment ->
            val weight = if (segment.type == SegmentType.STUDY) 15f else 3f

            val progress = when {
                index < currentIndex -> 1f
                index == currentIndex -> progressInCurrent
                else -> 0f
            }

            val color = when {
                index <= currentIndex -> activeColor
                else -> BaseColor40
            }

            TimelineSegment(
                progress = progress,
                activeColor = color,
                backgroundColor = BaseColor40,
                modifier = Modifier
                    .weight(weight)
            )

            if (index != segments.lastIndex) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}
