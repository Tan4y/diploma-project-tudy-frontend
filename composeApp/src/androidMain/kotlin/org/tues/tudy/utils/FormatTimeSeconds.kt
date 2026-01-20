package org.tues.tudy.utils

fun formatTimeSeconds(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}