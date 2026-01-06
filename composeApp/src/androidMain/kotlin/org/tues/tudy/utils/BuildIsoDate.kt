package org.tues.tudy.utils

import java.text.SimpleDateFormat
import java.util.*

fun BuildIsoDate(year: Int, month: Int, day: Int): String {
    val calendar = Calendar.getInstance().apply {
        set(year, month - 1, day, 0, 0, 0)
    }
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return sdf.format(calendar.time)
}
