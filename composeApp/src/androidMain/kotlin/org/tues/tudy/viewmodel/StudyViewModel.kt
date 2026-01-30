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
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.repository.StudyRepository
import org.tues.tudy.ui.components.StudyPhase

class StudyViewModel : ViewModel() {

    private val studyRepository =
        StudyRepository(ApiServiceBuilder.apiService)

    companion object {
        private const val STUDY_MINUTES = 15
        private const val REST_MINUTES = 3
        private const val MAX_ROUNDS = 4
    }

    private val _uiState = MutableStateFlow(StudyUiState(maxRounds = MAX_ROUNDS))
    val uiState: StateFlow<StudyUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    private var studyStartTime: Long = 0L
    private var accumulatedStudySeconds = 0


    fun startSession() {
        println("=== START SESSION CALLED ===")
        accumulatedStudySeconds = 0

        _uiState.value = StudyUiState(
            maxRounds = MAX_ROUNDS,
            currentRound = 1
        )
        startStudy()
    }

    fun finishStudyEarly() {
        println("finishStudyEarly() called")
        finishStudySegment()

        startRest()
    }

    fun nextPhase() {
        when (_uiState.value.phase) {
            StudyPhase.STUDYING -> {
                println("nextPhase() called from STUDYING phase")
                finishStudySegment()
                startRest()
            }
            StudyPhase.RESTING -> {
                println("nextPhase() called from RESTING phase")
                if (_uiState.value.currentRound >= _uiState.value.maxRounds) {
                    println("All rounds completed")
                    // Save ONCE at the end
                    viewModelScope.launch {
                        try {
                            studyRepository.saveStudyTime(accumulatedStudySeconds)
                            println("Final study time saved: $accumulatedStudySeconds seconds")
                        } catch (e: Exception) {
                            println("Failed to save: ${e.message}")
                        }
                    }
                    _uiState.value = _uiState.value.copy(phase = StudyPhase.FINISHED)
                } else {
                    println("Moving to round ${_uiState.value.currentRound + 1}")
                    _uiState.value = _uiState.value.copy(
                        currentRound = _uiState.value.currentRound + 1
                    )
                    startStudy()
                }
            }
            else -> {
                println("nextPhase() called in unexpected phase: ${_uiState.value.phase}")
            }
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
            _uiState.value = _uiState.value.copy(phase = StudyPhase.FINISHED)
        } else {
            _uiState.value = _uiState.value.copy(
                currentRound = _uiState.value.currentRound + 1
            )
            startStudy()
        }
    }

    private fun startStudy() {
        println("startStudy() called")
        studyStartTime = System.currentTimeMillis()

        _uiState.value = _uiState.value.copy(
            phase = StudyPhase.STUDYING,
            totalSeconds = STUDY_MINUTES * 60,
            remainingSeconds = STUDY_MINUTES * 60
        )
        println("Study phase set, starting timer...")
        startTimer()
    }

    private fun finishStudySegment() {
        val now = System.currentTimeMillis()
        val studiedSeconds = ((now - studyStartTime) / 1000).toInt().coerceAtLeast(1)
        accumulatedStudySeconds += studiedSeconds

        println("finishStudySegment() called")
        println("studyStartTime = $studyStartTime")
        println("now = $now")
        println("studiedSeconds = $studiedSeconds")
        println("accumulatedStudySeconds = $accumulatedStudySeconds")
    }

    private fun startRest() {
        println("startRest() called")
        cancelTimer()
        _uiState.value = _uiState.value.copy(
            phase = StudyPhase.RESTING,
            totalSeconds = REST_MINUTES * 60,
            remainingSeconds = REST_MINUTES * 60
        )
        println("Rest phase set, starting timer...")
        startTimer()
    }

    private fun startTimer() {
        println("startTimer() called")
        cancelTimer()
        timerJob = viewModelScope.launch {
            println("Timer coroutine started, remainingSeconds = ${_uiState.value.remainingSeconds}")
            while (_uiState.value.remainingSeconds > 0) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    remainingSeconds = _uiState.value.remainingSeconds - 1
                )
            }
            println("Timer countdown finished, calling onTimerFinished()")
            onTimerFinished()
        }
    }

    private fun onTimerFinished() {
        println("onTimerFinished() called, phase = ${_uiState.value.phase}, currentRound = ${_uiState.value.currentRound}")
        when (_uiState.value.phase) {
            StudyPhase.STUDYING -> {
                println("Study timer finished, transitioning to rest")
                finishStudySegment()
                startRest()
            }

            StudyPhase.RESTING -> {
                println("Rest timer finished, checking if more rounds...")
                if (_uiState.value.currentRound >= MAX_ROUNDS) {
                    println("All ${MAX_ROUNDS} rounds completed!")
                    cancelTimer()

                    // Save ONCE at the end
                    viewModelScope.launch {
                        try {
                            studyRepository.saveStudyTime(accumulatedStudySeconds)
                            println("Final study time saved: $accumulatedStudySeconds seconds")
                        } catch (e: Exception) {
                            println("Failed to save final study time: ${e.message}")
                            e.printStackTrace()
                        }
                    }

                    _uiState.value = _uiState.value.copy(
                        phase = StudyPhase.FINISHED
                    )
                } else {
                    println("Starting round ${_uiState.value.currentRound + 1}")
                    _uiState.value = _uiState.value.copy(
                        currentRound = _uiState.value.currentRound + 1
                    )
                    startStudy()
                }
            }

            else -> {
                println("onTimerFinished() in unexpected phase: ${_uiState.value.phase}")
            }
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