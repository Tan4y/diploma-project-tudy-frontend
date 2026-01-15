package org.tues.tudy.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun LocalDate.formatToShortDate(includeYear: Boolean = true): String {
    val pattern = if (includeYear) "dd/MM/yy" else "dd/MM"
    return try {
        this.format(DateTimeFormatter.ofPattern(pattern))
    } catch (e: DateTimeParseException) {
        ""
    }
}
