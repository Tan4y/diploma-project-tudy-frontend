package org.tues.tudy.data.model

data class StudyStatsResponse(
    val totalRealStudyMinutes: Int,
    val studyMinutesPerDay: Map<String, Int>
)