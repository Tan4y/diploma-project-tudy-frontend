package org.tues.tudy.utils

import java.text.SimpleDateFormat
import java.util.*

fun BuildIsoDate(
    year: Int,
    month: Int,
    day: Int,
    hour: Int = 0,
    minute: Int = 0
): String {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        set(year, month - 1, day, hour, minute, 0)
    }
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(calendar.time)
}


