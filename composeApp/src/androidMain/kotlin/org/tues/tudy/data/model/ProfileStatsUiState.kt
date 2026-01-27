package org.tues.tudy.data.model

import java.time.LocalDate

data class ProfileStatsUiState(
    val totalEvents: Int = 0,
    val upcomingEvents: Int = 0,
    val totalStudyMinutes: Int = 0,
    val studyMinutesPerDay: Map<LocalDate, Int> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)
