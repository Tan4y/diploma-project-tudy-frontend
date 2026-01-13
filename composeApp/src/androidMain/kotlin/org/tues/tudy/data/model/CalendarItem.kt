package org.tues.tudy.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

data class CalendarItem(
    val id: String,
    val title: String,
    val description: String?,
    val date: String,
    val startTime: String?,
    val endTime: String?,
    val type: String,
    val isStudySession: Boolean,
    val category: String? = null,
    val subject: String? = null,
    val pagesFrom: Int? = null,
    val pagesTo: Int? = null
) {
    private val now: LocalDateTime
        @RequiresApi(Build.VERSION_CODES.O)
        get() = LocalDateTime.now(ZoneId.systemDefault())

    val startDateTime: LocalDateTime
        get() = OffsetDateTime.parse(startTime).toLocalDateTime()

    val endDateTime: LocalDateTime?
        get() = endTime?.let { LocalDateTime.parse(it) }

    val isPast: Boolean
        get() = endDateTime?.isBefore(now) == true

    val isUpcoming: Boolean
        get() = startDateTime?.isAfter(now) == true

    val isOngoing: Boolean
        get() = startDateTime != null &&
                endDateTime != null &&
                now.isAfter(startDateTime) &&
                now.isBefore(endDateTime)
}
