package org.tues.tudy.ui.screens.calendar

import android.widget.Space
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
import androidx.compose.ui.text.style.TextAlign
import org.tues.tudy.data.model.Event
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius200
import org.tues.tudy.ui.theme.Dimens.BorderRadius250
import org.tues.tudy.ui.theme.shadow1
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun SlidingMonths(
    selectedMonth: YearMonth,
    selectedWeekStart: LocalDate? = null,
    events: List<Event>,
    showWeeks: Boolean = false,
    onMonthClick: (YearMonth) -> Unit,
    onWeekClick: (LocalDate) -> Unit
) {
    val months = listOf(
        selectedMonth.minusMonths(1),
        selectedMonth,
        selectedMonth.plusMonths(1),
        selectedMonth.plusMonths(2)
    )

    val items = if (!showWeeks) {
        listOf(
            selectedMonth.minusMonths(1),
            selectedMonth,
            selectedMonth.plusMonths(1),
            selectedMonth.plusMonths(2)
        )
    } else {
        // Show 4 consecutive weeks starting from the start of the selected month
        val firstDayOfMonth = selectedMonth.atDay(1)
        (0..3).map { weekIndex ->
            firstDayOfMonth.plusWeeks(weekIndex.toLong())
        }
    }

    val today = LocalDate.now()
    val currentMonth = YearMonth.from(today)

    Row(
        modifier = Modifier
            .shadow1()
            .fillMaxWidth()
            .clip(RoundedCornerShape(BorderRadius250))
            .background(BaseColor0)
            .padding(horizontal = Dimens.Space150, vertical = Dimens.Space75),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Space50),
        verticalAlignment = Alignment.Bottom
    ) {
        months.forEach { item ->
//            val monthName = yearMonth.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
//            var textColor = if (yearMonth == currentMonth) BaseColor100 else BaseColor80
//            val textSize =
//                if (selectedMonth == yearMonth) AppTypography.Heading4 else AppTypography.Heading6

            val text: String
            val isSelected: Boolean
            val textColor: androidx.compose.ui.graphics.Color

            if (!showWeeks) {
                // Month view
                val yearMonth = item as YearMonth
                val monthName = yearMonth.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                text = monthName
                isSelected = yearMonth == selectedMonth
                textColor = if (yearMonth == currentMonth) BaseColor100 else BaseColor80
            } else {
                // Week view
                val weekStart = item as LocalDate
                val weekEnd = weekStart.plusDays(6).takeIf { it.month == weekStart.month } ?: weekStart.withDayOfMonth(weekStart.lengthOfMonth())
                text = "${weekStart.dayOfMonth} - ${weekEnd.dayOfMonth} ${weekStart.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}"
                isSelected = selectedWeekStart == weekStart
                textColor = if (weekStart <= today && today <= weekEnd) BaseColor100 else BaseColor80
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        if (!showWeeks) onMonthClick(item as YearMonth)
                        else onWeekClick(item as LocalDate)
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
            }
        }
    }
}

//@Composable
//fun SlidingMonths(
//    selectedMonth: YearMonth,
//    events: List<Event>,
//    onMonthClick: (YearMonth) -> Unit
//) {
//    val months = listOf(
//        selectedMonth.minusMonths(1),
//        selectedMonth,
//        selectedMonth.plusMonths(1),
//        selectedMonth.plusMonths(2)
//    )
//
//    val today = LocalDate.now()
//    val currentMonth = YearMonth.from(today)
//
//    Row(
//        modifier = Modifier
//            .shadow1()
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(BorderRadius250))
//            .background(BaseColor0)
//            .padding(horizontal = Dimens.Space150, vertical = Dimens.Space75),
//        horizontalArrangement = Arrangement.spacedBy(Dimens.Space50),
//        verticalAlignment = Alignment.Bottom
//    ) {
//        months.forEach { yearMonth ->
//            val monthName = yearMonth.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
//            val textColor = if (yearMonth == currentMonth) BaseColor100 else BaseColor80
//            val textSize =
//                if (selectedMonth == yearMonth) AppTypography.Heading4 else AppTypography.Heading6
//
//            Row(
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.Bottom,
//                modifier = Modifier
//                    .weight(1f)
//                    .clickable(
//                        indication = null,
//                        interactionSource = remember { MutableInteractionSource() }) {
//                        onMonthClick(yearMonth)
//                    },
//            ) {
//                Text(
//                    text = monthName,
//                    style = textSize,
//                    color = textColor,
//                    textAlign = TextAlign.Center
//                )
//
//                if (yearMonth == selectedMonth) {
//                    Spacer(modifier = Modifier.width(Dimens.Space25))
//
//                    Text(
//                        text = yearMonth.year.toString(),
//                        style = AppTypography.Caption1,
//                        color = textColor,
//                    )
//                }
//            }
//        }
//    }
//}
