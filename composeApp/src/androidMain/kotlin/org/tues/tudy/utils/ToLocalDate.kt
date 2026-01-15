package org.tues.tudy.utils

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun String.toLocalDateSafe(): LocalDate {
    return try {
        OffsetDateTime.parse(this).toLocalDate()
    } catch (e: Exception) {
        LocalDate.parse(this, DateTimeFormatter.ISO_DATE)
    }
}
