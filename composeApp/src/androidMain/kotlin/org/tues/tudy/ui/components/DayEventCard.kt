package org.tues.tudy.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import org.tues.tudy.data.model.CalendarItem
import org.tues.tudy.ui.navigation.Routes
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
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayEventCard(
    navController: NavController,
    item: CalendarItem,
    startHour: Int,
    endHour: Int,
    userId: String,
    minuteHeightDp: Dp,
    onDelete: (CalendarItem) -> Unit
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

    val today = LocalDate.now()
    val nowMinutes = LocalTime.now().hour * 60 + LocalTime.now().minute
    val isToday = today == item.date.toLocalDateSafe()

    val isPastToday = isToday && nowMinutes >= endMinutes

    val backgroundColor = when {
        isToday && nowMinutes in startMinutes until endMinutes -> PrimaryColor1
        item.date.toLocalDateSafe() < today || isPastToday -> BaseColor20
        else -> BaseColor0
    }

    val textColor = when {
        isToday && nowMinutes in startMinutes until endMinutes -> BaseColor0
        item.date.toLocalDateSafe() < today || isPastToday -> BaseColor80
        else -> BaseColor100
    }

    val spacing = if (item.date.toLocalDateSafe() < today) Dimens.Space375 else Dimens.Space575

    val showShadow = (isToday && nowMinutes < endMinutes) || item.date.toLocalDateSafe() > today

    val shadowGap = if (showShadow) Dimens.Space25 else Dimens.Space0

    val studyButtonColor = if ( isToday && nowMinutes in startMinutes until endMinutes) BaseColor0 else PrimaryColor1

    val deleteButtonColor = if ( isToday && nowMinutes in startMinutes until endMinutes) BaseColor0 else BaseColor80

    Column(
        modifier = Modifier
            .offset(y = minuteHeightDp * offsetMinutes)
            .height(
                maxOf(
                    minuteHeightDp * durationMinutes,
                    spacing
                ) - shadowGap
            )
            .fillMaxWidth()
            .padding(bottom = shadowGap)
            .then(if (showShadow) Modifier.shadow1() else Modifier)
            .background(backgroundColor, RoundedCornerShape(BorderRadius250))
            .padding(Dimens.Space100)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.Space25)
            ) {
                if (item.subject != "Unknown") {
                    Text(
                        "${item.category ?: ""} | ${item.subject ?: ""}",
                        style = AppTypography.Heading6,
                        color = textColor
                    )
                }
                Text(
                    text = "• " + item.title,
                    style = AppTypography.Heading6,
                    color = textColor
                )
            }
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${formatTime(item.startTime)} – ${formatTime(item.endTime)}",
                    style = AppTypography.Caption1,
                    color = textColor
                )
                if (item.pagesTo != 0 && item.pagesTo != null) {
                    Spacer(modifier = Modifier.height(Dimens.Space50))
                    Text(
                        text = "pages: ${item.pagesFrom} – ${item.pagesTo}",
                        style = AppTypography.Caption1,
                        color = if (isToday && nowMinutes in startMinutes until endMinutes) BaseColor0 else BaseColor80
                    )
                }
            }
        }
        if (item.description?.isNotEmpty() == true) {
            Spacer(modifier = Modifier.height(Dimens.Space75))
            Text(
                text = item.description,
                style = AppTypography.Caption1,
                color = textColor
            )
        }

        if (item.date.toLocalDateSafe() >= today) {
            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                if (item.pagesTo == 0 || item.pagesTo == null || item.type == "personal") {
                    CustomButton(
                        value = "Delete",
                        enabled = true,
                        onClick = { onDelete(item) },
                        size = ButtonSize.SMALL,
                        color = deleteButtonColor,
                    )
                }
                if (item.type == "study") {
                    Spacer(modifier = Modifier.width(Dimens.Space50))
                    CustomButton(
                        value = "Study",
                        enabled = true,
                        onClick = { navController.navigate(Routes.studyRoute(userId)) },
                        size = ButtonSize.SMALL,
                        color = studyButtonColor,
                    )
                }
            }
        }
    }
}