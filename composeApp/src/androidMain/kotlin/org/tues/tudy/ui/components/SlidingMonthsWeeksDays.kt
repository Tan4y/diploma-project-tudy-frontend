package org.tues.tudy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius250
import org.tues.tudy.ui.theme.shadow1
import org.tues.tudy.utils.formatToShortDate
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Composable
fun SlidingMonthsWeeksDays(
    selectedMonth: YearMonth,
    selectedWeekStart: LocalDate? = null,
    selectedDay: LocalDate? = null,
    showWeeks: Boolean = false,
    showDays: Boolean = false,
    onMonthClick: (YearMonth) -> Unit,
    onWeekClick: (LocalDate) -> Unit,
    onDayClick: (LocalDate) -> Unit
) {
    val dayAnchor = selectedDay ?: LocalDate.now()

    val items = when {
        showDays -> listOf(
            dayAnchor.minusDays(1),
            dayAnchor,
            dayAnchor.plusDays(1),
            dayAnchor.plusDays(2)
        )

        showWeeks -> {
            val weekAnchor = (selectedWeekStart
                ?: selectedMonth.atDay(1))
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

            listOf(
                weekAnchor.minusWeeks(1),
                weekAnchor,
                weekAnchor.plusWeeks(1),
                weekAnchor.plusWeeks(2)
            )
        }

        else -> listOf(
            selectedMonth.minusMonths(1),
            selectedMonth,
            selectedMonth.plusMonths(1),
            selectedMonth.plusMonths(2)
        )
    }

    Row(
        modifier = Modifier
            .shadow1()
            .fillMaxWidth()
            .clip(RoundedCornerShape(BorderRadius250))
            .background(BaseColor0)
            .padding(horizontal = Dimens.Space150, vertical = Dimens.Space75),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        items.forEach { item ->
            val text: String
            val isSelected: Boolean
            val textColor: Color


            when {
                showDays -> {
                    val date = item as LocalDate
                    text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    isSelected = date == selectedDay
                    textColor = if (date == LocalDate.now()) BaseColor100 else BaseColor80
                }

                showWeeks -> {
                    val weekStart = item as LocalDate
                    val weekEnd = weekStart.plusDays(6)
                    text = "${weekStart.dayOfMonth} - ${weekEnd.dayOfMonth}"
                    isSelected = selectedWeekStart == weekStart
                    textColor =
                        if (LocalDate.now() in weekStart..weekEnd)
                            BaseColor100 else BaseColor80
                }

                else -> {
                    val yearMonth = item as YearMonth
                    text = yearMonth.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    isSelected = yearMonth == selectedMonth
                    textColor =
                        if (yearMonth == YearMonth.now()) BaseColor100 else BaseColor80
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        when {
                            showDays -> onDayClick(item as LocalDate)
                            showWeeks -> onWeekClick(item as LocalDate)
                            else -> onMonthClick(item as YearMonth)
                        }
                    },
            ) {
                Text(
                    text = text,
                    style = if (isSelected) AppTypography.Heading4 else AppTypography.Heading6,
                    color = textColor,
                    textAlign = TextAlign.Center
                )

                if (!showWeeks && item == selectedMonth) {
                    Spacer(modifier = Modifier.width(Dimens.Space25))

                    Text(
                        text = (item as YearMonth).year.toString(),
                        style = AppTypography.Caption1,
                        color = textColor,
                    )
                }
                if (showWeeks && item == selectedWeekStart) {
                    val weekStart = item as LocalDate
                    val monthShort = weekStart.month.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    )

                    Spacer(modifier = Modifier.width(Dimens.Space25))

                    Text(
                        text = monthShort,
                        style = AppTypography.Caption1,
                        color = textColor,
                    )
                }
                if (showDays && item == selectedDay) {
                    val day = item as LocalDate
                    Spacer(modifier = Modifier.width(Dimens.Space25))

                    Text(
                        text = day.formatToShortDate(),
                        style = AppTypography.Caption1,
                        color = textColor,
                    )
                }
            }
        }
    }
}
