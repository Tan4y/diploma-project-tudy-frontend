package org.tues.tudy.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun parseDateSafe(date: String): LocalDate {
    return try {
        // Try full ISO format first
        LocalDate.parse(date, DateTimeFormatter.ISO_DATE)
    } catch (e: DateTimeParseException) {
        // If just a day number, assume current month and year
        val day = date.toIntOrNull() ?: 1
        LocalDate.of(LocalDate.now().year, LocalDate.now().month, day)
    }
}