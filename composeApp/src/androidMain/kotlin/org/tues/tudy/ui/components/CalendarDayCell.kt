package org.tues.tudy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.tues.tudy.data.model.CalendarDay
import org.tues.tudy.ui.theme.*
import java.time.LocalDate

@Composable
fun CalendarDayCell(
    modifier: Modifier = Modifier,
    day: CalendarDay,
    onClick: () -> Unit
) {
    val isToday = day.date == LocalDate.now()

    val backgroundColor = when {
        isToday -> PrimaryColor1
        day.isCurrentMonth -> BaseColor0
        else -> BaseColor20
    }

    val setShadow = if (day.isCurrentMonth) Modifier.shadow1() else Modifier

    val textColor = when {
        isToday -> BaseColor0
        day.date < LocalDate.now() -> BaseColor100
        day.isCurrentMonth && day.eventsCount > 0 -> PrimaryColor1
        day.isCurrentMonth -> BaseColor100
        else -> BaseColor80
    }

    val verticalPadding = if (day.eventsCount > 0) Dimens.Space100 else Dimens.Space175

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .then(setShadow)
            .clip(RoundedCornerShape(Dimens.BorderRadius250))
            .background(backgroundColor)
            .padding(horizontal = Dimens.Space0, vertical = verticalPadding)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() },
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            style = AppTypography.Paragraph1,
            color = textColor
        )

        if (day.eventsCount > 0) {
            Spacer(modifier = Modifier.height(Dimens.Space75))

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.Space25),
                verticalAlignment = Alignment.CenterVertically
            ) {
                day.items.take(3).forEach { item ->

                    val dotColor = when {
                        item.isStudySession -> PrimaryColor2
                        item.type == "personal" -> SecondaryColor1
                        else -> PrimaryColor1
                    }

                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(dotColor)
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.Space25))
        }
    }
}