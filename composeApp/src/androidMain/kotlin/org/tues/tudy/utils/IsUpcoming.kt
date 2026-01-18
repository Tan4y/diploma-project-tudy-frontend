package org.tues.tudy.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.OffsetDateTime

@RequiresApi(Build.VERSION_CODES.O)
fun String.isUpcoming(): Boolean {
    return try {
        val eventDate = OffsetDateTime.parse(this).toLocalDate()
        val today = LocalDate.now()
        !eventDate.isBefore(today)
    } catch (e: Exception) {
        false
    }
}
