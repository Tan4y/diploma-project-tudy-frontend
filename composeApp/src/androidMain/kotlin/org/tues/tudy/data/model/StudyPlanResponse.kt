package org.tues.tudy.data.model

data class StudyPlanResponse(
    val _id: String,
    val userId: String,
    val eventId: String,
    val subject: String,
    val category: String?,
    val sessions: List<StudySessionResponse>,
    val eventDate: String
)

