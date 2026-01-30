package org.tues.tudy.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tues.tudy.data.model.ProfileStatsUiState
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.repository.EventRepository
import org.tues.tudy.data.repository.StudyRepository
import org.tues.tudy.utils.isUpcoming
import java.time.LocalDate

class ProfileViewModel : ViewModel() {

    private val eventRepository = EventRepository(ApiServiceBuilder.apiService)
    private val studyRepository = StudyRepository(ApiServiceBuilder.apiService)

    private val _uiState = MutableStateFlow(ProfileStatsUiState())
    val uiState: StateFlow<ProfileStatsUiState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadProfileStats(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // EVENTS
                val events = eventRepository.getEventsForUser(userId)
                val totalEvents = events.size
                val upcomingEvents = events.count { it.isUpcoming() }

                // STUDY STATS
                val studyStats = studyRepository.getStudyStats()

                // Convert string keys to LocalDate
                val studyMinutesPerDayLocal: Map<LocalDate, Int> =
                    studyStats.studyMinutesPerDay.mapKeys { (dateString, _) ->
                        LocalDate.parse(dateString) // parses "YYYY-MM-DD"
                    }

                _uiState.value = _uiState.value.copy(
                    totalEvents = totalEvents,
                    upcomingEvents = upcomingEvents,
                    totalStudyMinutes = studyStats.totalRealStudyMinutes,
                    studyMinutesPerDay = studyMinutesPerDayLocal, // <-- now LocalDate
                    error = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to load profile stats"
                )
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}
