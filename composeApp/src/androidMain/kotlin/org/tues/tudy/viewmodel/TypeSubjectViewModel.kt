package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tues.tudy.data.model.Event
import org.tues.tudy.data.remote.ApiServiceBuilder

class TypeSubjectViewModel: ViewModel() {
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    fun loadEventsForUser(userId: String) {
        viewModelScope.launch {
            try {
                val response = ApiServiceBuilder.apiService.getEvents()
                if (response.isSuccessful) {
                    // Filter by the user if needed
                    _events.value = response.body()?.filter { it.user == userId } ?: emptyList()
                } else {
                    _events.value = emptyList()
                }
            } catch (e: Exception) {
                _events.value = emptyList()
            }
        }
    }

    // Returns events grouped by subject for a given type
    fun getSubjectsWithEventsForType(type: String): Map<String, List<Event>> {
        return _events.value
            .filter { it.type == "study" && it.category == type }
            .groupBy { it.subject ?: "Unknown Subject" }
    }

    // Returns events grouped by type for a given subject
    fun getTypesWithEventsForSubject(subject: String): Map<String, List<Event>> {
        return _events.value
            .filter { it.type == "study" && it.subject == subject }
            .groupBy { it.category ?: "Unknown Type" }
    }
}