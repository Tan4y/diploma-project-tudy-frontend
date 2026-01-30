package org.tues.tudy.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import org.tues.tudy.data.model.CalendarItem
import org.tues.tudy.ui.theme.Dimens
import kotlin.collections.forEach

@Composable
fun DayEventsLayer(
    events: List<CalendarItem>,
    startHour: Int,
    endHour: Int,
    navController: NavController,
    userId: String,
    minuteHeightDp: Dp,
    onDeleteEvent: (CalendarItem) -> Unit
) {
    Box(modifier = Modifier.fillMaxHeight().padding(vertical = Dimens.Space50)) {
        events.forEach { item ->
            DayEventCard(
                item = item,
                startHour = startHour,
                endHour = endHour,
                navController = navController,
                userId = userId,
                minuteHeightDp = minuteHeightDp,
                onDelete = onDeleteEvent
            )
        }
    }
}
