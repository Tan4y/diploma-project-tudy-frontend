package org.tues.tudy.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.tues.tudy.ui.components.BarChart
import org.tues.tudy.ui.components.StatCard
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.viewmodel.ProfileViewModel
import org.tues.tudy.utils.dateToDayShort
import org.tues.tudy.utils.formatLocalDate
import org.tues.tudy.utils.weekRangeFromDates

@Composable
fun ProfileContent(
    navController: NavController,
    viewModel: ProfileViewModel,
    userId: String,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadProfileStats(userId)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Dimens.Space150),
        contentPadding = PaddingValues(
            start = Dimens.Space100,
            end = Dimens.Space100,
            top = Dimens.Space100,    // extra space for the top shadow
            bottom = Dimens.Space100  // extra space for the bottom shadow
        )
    ) {

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    Dimens.Space150
                )
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    StatCard("Total Tudies", uiState.totalEvents.toString())
                }


                Box(modifier = Modifier.weight(1f)) {
                    StatCard("Upcoming Tudies", uiState.upcomingEvents.toString())
                }
            }
        }

        item {
            StatCard(
                "Total Study Time",
                "${uiState.totalStudyMinutes} min"
            )
        }

        item {
            val weekStartEnd = weekRangeFromDates(uiState.studyMinutesPerDay.keys)
            val (weekStart, weekEnd) = weekStartEnd

            val studyMinutesPerDayOfWeek = uiState.studyMinutesPerDay
                .entries
                .groupBy { dateToDayShort(it.key) }
                .mapValues { entry ->
                    entry.value.sumOf { it.value }
                }

            BarChart(
                data = studyMinutesPerDayOfWeek,
                title = "Study Time Per Day",
                weekRange = "${formatLocalDate(weekStart)} – ${formatLocalDate(weekEnd)}"
            )
        }
    }
}
