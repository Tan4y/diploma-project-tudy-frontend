package org.tues.tudy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tues.tudy.data.model.CalendarItem
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor20
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius250
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.ui.theme.shadow1
import org.tues.tudy.utils.formatTime
import org.tues.tudy.utils.toLocalDateSafe
import org.tues.tudy.utils.toMinutes

@Composable
fun DayEventCard(
    item: CalendarItem,
    startHour: Int,
    endHour: Int
) {
    val startMinutes = item.startTime?.toMinutes() ?: return
    val endMinutes = item.endTime?.toMinutes() ?: return

    // Ignore events completely before or after the visible hours
    if (endMinutes <= startHour * 60 || startMinutes >= endHour * 60) return

    // Clamp start/end to visible hours
    val clampedStart = startMinutes.coerceAtLeast(startHour * 60)
    val clampedEnd = endMinutes.coerceAtMost(endHour * 60)

    val offsetMinutes = clampedStart - startHour * 60
    val durationMinutes = (clampedEnd - clampedStart).coerceAtLeast(15)

    val today = java.time.LocalDate.now()
    val nowMinutes = java.time.LocalTime.now().hour * 60 + java.time.LocalTime.now().minute
    val isToday = today == item.date.toLocalDateSafe()

    val backgroundColor = when {
        isToday && nowMinutes in startMinutes until endMinutes -> PrimaryColor1
        item.date.toLocalDateSafe() < today -> BaseColor20
        else -> BaseColor0
    }

    val textColor = when {
        isToday && nowMinutes in startMinutes until endMinutes -> BaseColor0
        item.date.toLocalDateSafe() < today -> BaseColor80
        else -> BaseColor100
    }

    val showShadow = (isToday && nowMinutes < endMinutes) || item.date.toLocalDateSafe() > today

    Column(
        modifier = Modifier
            .offset(y = offsetMinutes.dp)
            .height(durationMinutes.dp)
            .heightIn(min = Dimens.Space250)
            .fillMaxWidth()
            .then(if (showShadow) Modifier.shadow1() else Modifier)
            .background(backgroundColor, RoundedCornerShape(BorderRadius250))
            .padding(Dimens.Space100)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.Space25)
            ) {
                Text(
                    "${item.category ?: ""} | ${item.subject ?: ""}",
                    style = AppTypography.Heading6,
                    color = textColor
                )
                Text(
                    text = "• " + item.title,
                    style = AppTypography.Heading6,
                    color = textColor
                )
            }
            Text(
                text = "${formatTime(item.startTime)} – ${formatTime(item.endTime)}",
                style = AppTypography.Caption1,
                color = textColor
            )
        }
        if (item.description?.isNotEmpty() == true) {
            Spacer(modifier = Modifier.height(Dimens.Space75))
            Text(
                text = item.description,
                style = AppTypography.Paragraph1,
                color = textColor
            )
        }
    }
}