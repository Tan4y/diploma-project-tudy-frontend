package org.tues.tudy.ui.screens.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.tues.tudy.data.model.CalendarDay
import org.tues.tudy.ui.components.CalendarDayCell
import org.tues.tudy.ui.components.CalendarMode
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.viewmodel.CalendarViewModel

@Composable
fun MonthlyCalendarContent(
    days: List<CalendarDay>,
    viewModel: CalendarViewModel,
) {
    val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    style = AppTypography.Caption1,
                    color = BaseColor100,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        Spacer(modifier = Modifier.height(Dimens.Space100))

        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.Space50),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            val weeks = days.chunked(7)
            weeks.forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Space50)
                ) {
                    week.forEach { day ->
                        CalendarDayCell(
                            day = day,
                            onClick = {
                                viewModel.setSelectedDay(day.date)
                                viewModel.setCalendarMode(CalendarMode.DAY)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
