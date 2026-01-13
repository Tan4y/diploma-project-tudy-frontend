package org.tues.tudy.data.model


data class StudySessionResponse(
    val sessionNumber: Int,
    val startTime: String,
    val endTime: String,
    val pages: Int
)