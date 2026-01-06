package org.tues.tudy.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateToDayMonth(dateString: String): String {
    return try {
        val parsedDate = OffsetDateTime.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("dd.MM")
        parsedDate.format(formatter)
    } catch (e: Exception) {
        dateString
    }
}
