package org.tues.tudy.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun getCreationDateFromId(id: String): String {
    return try {
        val timestamp = id.take(8).toLong(16)
        val instant = Instant.ofEpochSecond(timestamp)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/")
            .withZone(ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        ""
    }
}

