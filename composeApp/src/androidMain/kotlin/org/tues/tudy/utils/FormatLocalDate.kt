package org.tues.tudy.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun formatLocalDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM")
    return date.format(formatter)
}