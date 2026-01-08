package org.tues.tudy.utils

import android.os.Build
import androidx.annotation.RequiresApi
import org.tues.tudy.data.model.Event
import java.time.OffsetDateTime
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.O)
fun Event.isUpcoming(): Boolean {
    return try {
        val eventDateTime = OffsetDateTime.parse(startTime)
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        eventDateTime.isAfter(now)
    } catch (e: Exception) {
        false
    }
}


