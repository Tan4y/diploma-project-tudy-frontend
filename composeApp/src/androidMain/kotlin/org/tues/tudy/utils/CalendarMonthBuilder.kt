package org.tues.tudy.utils

import org.tues.tudy.data.model.CalendarDay
import org.tues.tudy.data.model.Event
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

fun buildMonth(
    yearMonth: YearMonth,
    events: List<Event>
): List<CalendarDay> {

    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()

    val dayOfWeekValue = firstDayOfMonth.dayOfWeek.value
    val startOffset = dayOfWeekValue - 1
    val firstDayOfGrid = firstDayOfMonth.minusDays(startOffset.toLong())


    val totalDays = 42

    val eventsByDate = events.groupBy { it.date.toLocalDateSafe() }

    return (0 until totalDays).map { index ->
        val date = firstDayOfGrid.plusDays(index.toLong())
        CalendarDay(
            date = date,
            isCurrentMonth = date.month == yearMonth.month,
            eventsCount = eventsByDate[date]?.size ?: 0
        )
    }
}
