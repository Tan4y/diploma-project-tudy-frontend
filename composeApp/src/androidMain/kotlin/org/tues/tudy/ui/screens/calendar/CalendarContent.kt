package org.tues.tudy.ui.screens.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.tues.tudy.data.model.CalendarDay
import org.tues.tudy.data.model.Event
import org.tues.tudy.ui.components.CalendarMode
import org.tues.tudy.ui.components.CalendarModes
import org.tues.tudy.ui.components.SlidingMonthsWeeksDays
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.viewmodel.CalendarViewModel
import java.time.YearMonth

@Composable
fun CalendarContent(
    navController: NavController,
    days: List<CalendarDay>,
    selectedMonth: YearMonth,
    events: List<Event>,
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel,
    userId: String
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val calendarMode by viewModel.calendarMode.collectAsState()

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryColor1)
        }
    } else {
        Column(
            modifier = modifier
                .padding(
                    start = Dimens.Space100,
                    end = Dimens.Space100,
                    top = Dimens.Space100,
                    bottom = Dimens.Space125
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            val selectedWeekStart by viewModel.selectedWeekStart.collectAsState()

            val selectedDay by viewModel.selectedDay.collectAsState()

            SlidingMonthsWeeksDays(
                selectedMonth = selectedMonth,
                selectedWeekStart = selectedWeekStart,
                selectedDay = selectedDay,
                events = events,
                showWeeks = calendarMode == CalendarMode.WEEK,
                showDays = calendarMode == CalendarMode.DAY,
                onMonthClick = { viewModel.changeMonth(it) },
                onWeekClick = { viewModel.setSelectedWeek(it) },
                onDayClick = { viewModel.setSelectedDay(it) }
            )


            Box(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (calendarMode) {
                        CalendarMode.MONTH -> {
                            MonthlyCalendarContent(
                                days = days,
                                viewModel = viewModel
                            )

                        }

                        CalendarMode.WEEK -> {
                            WeeklyCalendarContent(
                                viewModel = viewModel,
                                weekDays = viewModel.getWeekDays(selectedWeekStart)
                            )
                        }

                        CalendarMode.DAY -> {
                            DailyCalendarContent(
                                viewModel = viewModel,
                                navController = navController,
                                userId = userId
                            )
                        }
                    }
                }
            }

            CalendarModes(
                navController = navController,
                selectedMode = calendarMode,
                userId = userId,
                onModeSelected = { mode ->
                    viewModel.setCalendarMode(mode)
                }
            )
        }
    }
}