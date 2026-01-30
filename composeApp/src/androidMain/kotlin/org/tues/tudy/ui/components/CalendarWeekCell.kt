package org.tues.tudy.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import org.tues.tudy.data.model.CalendarItem
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor20
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius250
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.ui.theme.shadow1
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarWeekCell(
    date: LocalDate,
    events: List<CalendarItem>,
    onClick: () -> Unit
) {
    val today = LocalDate.now()

    val backgroundColor = when {
        date < today -> BaseColor20
        date > today -> BaseColor0
        else -> PrimaryColor1
    }

    val textColor = when {
        date < today -> BaseColor80
        date > today -> BaseColor100
        else -> BaseColor0
    }

    val dotColor = when {
        date < today -> BaseColor80
        date > today -> PrimaryColor1
        else -> BaseColor0
    }
    val eventsSize = events.size
    val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() }
            .background(BaseColor0)
            .padding(vertical = Dimens.Space75),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Space100)
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.Space75)
                .width(Dimens.Space275),
            verticalArrangement = Arrangement.spacedBy(Dimens.Space25),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = AppTypography.Paragraph1,
                color = BaseColor80,
            )
            Text(
                text = dayName,
                style = AppTypography.Paragraph1,
                color = BaseColor80,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = Dimens.Space450)
                .then(if (date >= today) Modifier.shadow1() else Modifier)
                .background(backgroundColor, RoundedCornerShape(BorderRadius250))
                .padding(Dimens.Space100),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Space25)
        ) {
            if (eventsSize == 0 && date == today) {
                Text(
                    text = "Free to rest!",
                    style = AppTypography.Paragraph1,
                    color = BaseColor0
                )
            } else if (eventsSize > 0) {
                val half = (eventsSize + 1) / 2
                val firstColumn = events.take(half)
                val secondColumn = events.drop(half)

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Space50),
                ) {
                    val maxEventsToShow = 5
                    val eventsToDisplay = events.take(maxEventsToShow)

                    eventsToDisplay.forEach { event ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(Dimens.Space25)
                        ) {
                            Text(
                                text = "•",
                                style = AppTypography.Paragraph1,
                                color = dotColor
                            )
                            Text(
                                text = event.title,
                                style = AppTypography.Paragraph1,
                                color = textColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (event.isStudySession) {
                                Text(
                                    text = "- session",
                                    style = AppTypography.Paragraph1,
                                    color = textColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    if (events.size > maxEventsToShow) {
                        Text(
                            text = "Tap to see more...",
                            style = AppTypography.Caption1,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}
