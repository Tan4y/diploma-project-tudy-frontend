package org.tues.tudy.ui.screens.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.tues.tudy.data.model.CalendarDay
import org.tues.tudy.ui.components.CalendarMode
import org.tues.tudy.ui.components.CalendarWeekCell
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.viewmodel.CalendarViewModel

@Composable
fun WeeklyCalendarContent(
    viewModel: CalendarViewModel,
    modifier: Modifier = Modifier,
    weekDays: List<CalendarDay>,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = Dimens.Space150),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(weekDays) { day ->
            CalendarWeekCell(
                date = day.date,
                events = day.items,
                onClick = {
                    viewModel.setSelectedDay(day.date)
                    viewModel.setCalendarMode(CalendarMode.DAY)
                }
            )
        }
    }
}