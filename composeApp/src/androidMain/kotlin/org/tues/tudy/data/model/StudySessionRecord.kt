package org.tues.tudy.data.model

import java.time.LocalDate

data class StudySessionRecord(
    val userId: String,
    val date: LocalDate,        // 2026-01-16
    val startTime: Long,        // epoch millis
    val endTime: Long,          // epoch millis
    val durationSeconds: Int,   // only STUDY time
    val roundsCompleted: Int
)
