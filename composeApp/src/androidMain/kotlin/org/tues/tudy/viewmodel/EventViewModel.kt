package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.repository.EventRepository

class EventViewModel: ViewModel() {
    private val repository = EventRepository(ApiServiceBuilder.apiService)
    private val _tudiesCount = MutableStateFlow(0)
    val tudiesCount = _tudiesCount.asStateFlow()

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
}
