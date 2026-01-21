package org.tues.tudy.utils

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


fun dateToDayShort(date: String): String {
    return LocalDate.parse(date)
        .dayOfWeek
        .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
}