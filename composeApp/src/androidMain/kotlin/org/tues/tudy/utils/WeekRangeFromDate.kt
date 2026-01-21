package org.tues.tudy.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters


fun weekRangeFromDates(dates: Collection<String>?): Pair<LocalDate, LocalDate> {
    val parsedDates = dates?.mapNotNull { runCatching { LocalDate.parse(it) }.getOrNull() } ?: emptyList()
    val minDate = parsedDates.minOrNull() ?: LocalDate.now()
    val weekStart = minDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val weekEnd = weekStart.plusDays(6)
    return weekStart to weekEnd
}