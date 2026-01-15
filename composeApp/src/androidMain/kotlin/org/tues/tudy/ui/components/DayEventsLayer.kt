package org.tues.tudy.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.tues.tudy.data.model.CalendarItem
import kotlin.collections.forEach

@Composable
fun DayEventsLayer(events: List<CalendarItem>, startHour: Int,
                   endHour: Int) {
    Box(
        modifier = Modifier.fillMaxHeight()
    ) {
        events.forEach { item ->
            DayEventCard(item, startHour, endHour)
        }
    }
}