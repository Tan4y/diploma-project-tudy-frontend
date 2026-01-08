package org.tues.tudy.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
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
import org.tues.tudy.utils.isUpcoming

class EventViewModel : ViewModel() {
    private val repository = EventRepository(ApiServiceBuilder.apiService)
    private val _tudiesCount = MutableStateFlow(0)
    val tudiesCount = _tudiesCount.asStateFlow()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events = _events.asStateFlow()

    private val _subjectDates = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val subjectDates = _subjectDates.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    @RequiresApi(Build.VERSION_CODES.O)
    fun loadEvents(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getEventsForUser(userId)
                _events.value = response
                    .filter { it.type == "study" && it.isUpcoming() }
                    .sortedBy { it.date }

            } catch (e: Exception) {
                _events.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun loadTudiesByCategory(userId: String, category: String) {
        viewModelScope.launch {
            try {
                val count = repository.getTudiesCountByCategory(userId, category)
                _tudiesCount.value = count
            } catch (e: Exception) {
                _tudiesCount.value = 0
            }
        }
    }

    fun loadTudiesBySubject(userId: String, subject: String) {
        viewModelScope.launch {
            try {
                val count = repository.getTudiesCountBySubject(userId, subject)
                _tudiesCount.value = count
            } catch (e: Exception) {
                _tudiesCount.value = 0
            }
        }
    }

    fun loadDatesForSubjects(userId: String, subjects: List<String>) {
        viewModelScope.launch {
            val map = mutableMapOf<String, List<String>>()
            subjects.forEach { subject ->
                try {
                    val dates = repository.getEventDatesForSubject(userId, subject)
                    map[subject] = dates
                } catch (e: Exception) {
                    map[subject] = emptyList()
                }
            }
            _subjectDates.value = map
        }
    }

    fun deleteEvent(
        userId: String,
        eventId: String,
        onSuccess: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                repository.deleteEvent(eventId)
                loadEvents(userId)
                onSuccess?.invoke()
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "Unknown error")
            }
        }
    }


}
