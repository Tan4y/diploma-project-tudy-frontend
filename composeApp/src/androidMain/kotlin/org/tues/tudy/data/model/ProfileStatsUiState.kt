package org.tues.tudy.data.model

data class ProfileStatsUiState(
    val totalEvents: Int = 0,
    val upcomingEvents: Int = 0,
    val totalStudyMinutes: Int = 0,
    val studyMinutesPerDay: Map<String, Int> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)
