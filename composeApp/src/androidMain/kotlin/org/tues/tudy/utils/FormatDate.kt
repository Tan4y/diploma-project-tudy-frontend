package org.tues.tudy.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(
    dateString: String,
    includeShortYear: Boolean = false
): String {
    return try {
        val parsedDate = OffsetDateTime.parse(dateString)

        val formatter = if (includeShortYear)
            DateTimeFormatter.ofPattern("dd/MM/yy")
        else
            DateTimeFormatter.ofPattern("dd/MM")

        parsedDate.format(formatter)
    } catch (e: Exception) {
        dateString
    }
}

