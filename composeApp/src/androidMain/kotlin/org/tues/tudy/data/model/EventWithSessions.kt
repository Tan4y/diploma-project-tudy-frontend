package org.tues.tudy.data.model

data class EventWithSessions(
    val event: CalendarItem,
    val sessions: List<CalendarItem>
)
