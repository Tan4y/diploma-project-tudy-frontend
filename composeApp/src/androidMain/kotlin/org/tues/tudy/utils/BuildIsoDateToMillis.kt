package org.tues.tudy.utils

import java.util.Calendar

fun BuildIsoDateToMillis(dateIso: String?, timeIso: String?): Long {
    if (dateIso == null) return 0L
    val parts = dateIso.split("-").map { it.toInt() }
    val cal = Calendar.getInstance()
    cal.set(parts[0], parts[1] - 1, parts[2], 0, 0)

    if (timeIso != null) {
        val timeParts = timeIso.split(":").map { it.toInt() }
        cal.set(Calendar.HOUR_OF_DAY, timeParts[0])
        cal.set(Calendar.MINUTE, timeParts[1])
    }
    return cal.timeInMillis
}
