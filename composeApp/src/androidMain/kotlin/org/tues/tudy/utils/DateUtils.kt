package org.tues.tudy.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun formatDateToDayMonth(dateString: String): String {
    return try {
        val parsedDate = LocalDate.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("dd.MM")
        parsedDate.format(formatter)
    } catch (e: Exception) {
        dateString
    }
}
