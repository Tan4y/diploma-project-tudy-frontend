package org.tues.tudy.data.model

import java.time.LocalDate

data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val eventsCount: Int,
    val items: List<CalendarItem> = emptyList()
)