package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tues.tudy.data.model.ProgressSegment
import org.tues.tudy.data.model.SegmentType
import org.tues.tudy.data.model.StudyUiState
import org.tues.tudy.ui.components.StudyPhase

class StudyViewModel : ViewModel() {

    companion object {
        private const val STUDY_MINUTES = 15
        private const val REST_MINUTES = 3
        private const val MAX_ROUNDS = 4
    }

    private val _uiState = MutableStateFlow(StudyUiState(maxRounds = MAX_ROUNDS))
    val uiState: StateFlow<StudyUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun startSession() {
        _uiState.value = StudyUiState(
            phase = StudyPhase.STUDYING,
            totalSeconds = STUDY_MINUTES * 60,
            remainingSeconds = STUDY_MINUTES * 60,
            currentRound = 1,
            maxRounds = MAX_ROUNDS
        )
        startTimer()
    }

    fun finishStudyEarly() {
        // Study done → start rest, keep currentRound the same
        startRest()
    }

    fun nextPhase() {
        when (_uiState.value.phase) {
            StudyPhase.STUDYING -> startRest() // done studying
            StudyPhase.RESTING -> {
                if (_uiState.value.currentRound >= _uiState.value.maxRounds) {
                    _uiState.value = _uiState.value.copy(phase = StudyPhase.FINISHED)
                } else {
                    _uiState.value = _uiState.value.copy(
                        currentRound = _uiState.value.currentRound + 1
                    )
                    startStudy()
                }
            }
            else -> {}
        }
    }

    fun buildSegments(maxRounds: Int): List<ProgressSegment> {
        val segments = mutableListOf<ProgressSegment>()
        repeat(maxRounds) { round ->
            segments += ProgressSegment(
                index = segments.size,
                type = SegmentType.STUDY,
                round = round + 1
            )
            segments += ProgressSegment(
                index = segments.size,
                type = SegmentType.REST,
                round = round + 1
            )
        }
        return segments
    }

    fun continueAfterRest() {
        if (_uiState.value.currentRound >= _uiState.value.maxRounds) {
            // All rounds done → finish
            _uiState.value = _uiState.value.copy(phase = StudyPhase.FINISHED)
        } else {
            // Increment round and start next study
            _uiState.value = _uiState.value.copy(
                currentRound = _uiState.value.currentRound + 1
            )
            startStudy()
        }
    }



    private fun startStudy() {
        _uiState.value = _uiState.value.copy(
            phase = StudyPhase.STUDYING,
            totalSeconds = STUDY_MINUTES * 60,
            remainingSeconds = STUDY_MINUTES * 60
        )
        startTimer()
    }

    private fun startRest() {
        cancelTimer()
        _uiState.value = _uiState.value.copy(
            phase = StudyPhase.RESTING,
            totalSeconds = REST_MINUTES * 60,
            remainingSeconds = REST_MINUTES * 60
        )
        startTimer()
    }

    private fun startTimer() {
        cancelTimer()
        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingSeconds > 0) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    remainingSeconds = _uiState.value.remainingSeconds - 1
                )
            }
            onTimerFinished()
        }
    }

    private fun onTimerFinished() {
        when (_uiState.value.phase) {
            StudyPhase.STUDYING -> startRest()

            StudyPhase.RESTING -> {
                if (_uiState.value.currentRound >= MAX_ROUNDS) {
                    cancelTimer()
                    _uiState.value = _uiState.value.copy(
                        phase = StudyPhase.FINISHED
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        currentRound = _uiState.value.currentRound + 1
                    )
                    startStudy()
                }
            }

            else -> Unit
        }
    }

    private fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun onCleared() {
        cancelTimer()
        super.onCleared()
    }
}
