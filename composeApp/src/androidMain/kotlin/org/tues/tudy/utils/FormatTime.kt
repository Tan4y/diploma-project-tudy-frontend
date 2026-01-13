package org.tues.tudy.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(dateString: String): String {
    return try {
        val parsedDate = OffsetDateTime.parse(dateString)
        parsedDate.format(DateTimeFormatter.ofPattern("HH:mm"))
    } catch (e: Exception) {
        ""
    }
}
