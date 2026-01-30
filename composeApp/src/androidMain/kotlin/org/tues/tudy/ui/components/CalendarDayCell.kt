package org.tues.tudy.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import org.tues.tudy.data.model.CalendarDay
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor20
import org.tues.tudy.ui.theme.BaseColor60
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius250
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.ui.theme.PrimaryColor2
import org.tues.tudy.ui.theme.SecondaryColor1
import org.tues.tudy.ui.theme.shadow1
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
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

    val dotColor = when {
        isToday -> BaseColor0
        day.date < LocalDate.now() -> BaseColor60
        day.items.any { it.isStudySession } -> PrimaryColor2
        day.items.any {it.type == "personal"} -> SecondaryColor1
        day.items.isNotEmpty() -> PrimaryColor1
        else -> Color.Transparent
    }


    val verticalPadding = if (day.eventsCount > 0) Dimens.Space100 else Dimens.Space175

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .then(setShadow)
            .clip(RoundedCornerShape(BorderRadius250))
            .background(backgroundColor)
            .padding(horizontal = Dimens.Space0, vertical = verticalPadding)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) { onClick() },
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            style = AppTypography.Paragraph1,
            color = textColor
        )

        if (day.eventsCount > 0) {
            Spacer(modifier = Modifier.height(Dimens.Space25))

            Text(
                text = "•".repeat(minOf(day.eventsCount, 3)),
                style = AppTypography.Paragraph1,
                color = dotColor,
            )
        }
    }
}
