package org.tues.tudy.data.model

import org.tues.tudy.ui.components.StudyPhase

data class StudyUiState(
    val phase: StudyPhase = StudyPhase.IDLE,
    val remainingSeconds: Int = 0,
    val totalSeconds: Int = 0,
    val currentRound: Int = 0,
    val maxRounds: Int = 4,
    val totalMinutesStudied: Int = 0
)
