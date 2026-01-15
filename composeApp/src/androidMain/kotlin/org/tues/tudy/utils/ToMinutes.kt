package org.tues.tudy.utils

fun String.toMinutes(): Int? {
    // Extract time part after 'T'
    val timePart = this.substringAfter("T", "").takeIf { it.isNotEmpty() } ?: return null

    // Split hours and minutes
    val (h, m) = timePart.split(":")
        .map { it.toIntOrNull() ?: 0 } // fallback to 0 if missing
        .let { it + listOf(0, 0) }     // ensure size 2
        .take(2)

    return h * 60 + m
}