package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.tues.tudy.data.model.Event
import org.tues.tudy.data.model.TypeSubject
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.repository.EventRepository

class EventViewModel: ViewModel() {
    private val repository = EventRepository(ApiServiceBuilder.apiService)
    private val _tudiesCount = MutableStateFlow(0)
    val tudiesCount = _tudiesCount.asStateFlow()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events = _events.asStateFlow()

    // Load all events for the user
    fun loadEvents() {
        viewModelScope.launch {
            try {
                val response = repository.getEvents()
                _events.value = response.sortedBy { it.date }
            } catch (e: Exception) {
                _events.value = emptyList()
            }
        }
    }


    fun loadTudiesByCategory(category: String) {
        viewModelScope.launch {
            try {
                val count = repository.getTudiesCountByCategory(category)
                _tudiesCount.value = count
            } catch (e: Exception) {
                _tudiesCount.value = 0
            }
        }
    }

    fun loadTudiesBySubject(subject: String) {
        viewModelScope.launch {
            try {
                val count = repository.getTudiesCountBySubject(subject)
                _tudiesCount.value = count
            } catch (e: Exception) {
                _tudiesCount.value = 0
            }
        }
    }
    // Map subject name -> list of dates
    private val _subjectDates = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val subjectDates = _subjectDates.asStateFlow()

    fun loadDatesForSubjects(subjects: List<String>) {
        viewModelScope.launch {
            val map = mutableMapOf<String, List<String>>()
            subjects.forEach { subject ->
                try {
                    val dates = repository.getEventDatesForSubject(subject)
                    map[subject] = dates
                } catch (e: Exception) {
                    map[subject] = emptyList()
                }
            }
            _subjectDates.value = map
        }
    }

    fun deleteEvent(eventId: String, onSuccess: (() -> Unit)? = null, onError: ((String) -> Unit)? = null) {
        viewModelScope.launch {
            try {
                repository.deleteEvent(eventId)
                loadEvents()
                onSuccess?.invoke()
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "Unknown error")
            }
        }
    }


}
