package org.tues.tudy.ui.screens.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.tues.tudy.ui.components.DayEventsLayer
import org.tues.tudy.ui.components.HourGrid
import org.tues.tudy.ui.components.TimeColumn
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.viewmodel.CalendarViewModel

@Composable
fun DailyCalendarContent(
    viewModel: CalendarViewModel,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {
    val selectedDate = viewModel.selectedDay.collectAsState().value
    val day = viewModel.days.collectAsState().value
        .find { it.date == selectedDate }
        ?: return

    val startHour = viewModel.userStudyWindowStart.take(2).toIntOrNull() ?: 8
    val endHour = viewModel.userStudyWindowEnd.take(2).toIntOrNull() ?: 22

    val totalHours = endHour - startHour

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val paddingTopBottomBars = innerPadding.calculateTopPadding() + innerPadding.calculateBottomPadding()

    val paddingExtra = Dimens.Space150 * 2
    val availableHeight = screenHeight - paddingTopBottomBars - paddingExtra
    val hourHeightDp: Dp = availableHeight / totalHours

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.Space150)
    ) {
        // Make the ROW scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Space100)
            ) {
                // Time column
                TimeColumn(
                    startHour = startHour,
                    endHour = endHour,
                    hourHeightDp = hourHeightDp
                )

                // Events column
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    HourGrid(
                        startHour = startHour,
                        endHour = endHour,
                        hourHeightDp = hourHeightDp
                    )
                    DayEventsLayer(
                        day.items, startHour = startHour,
                        endHour = endHour
                    )
                }
            }
        }
    }
}
