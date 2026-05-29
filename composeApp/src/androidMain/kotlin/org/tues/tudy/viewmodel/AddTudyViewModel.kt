package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tues.tudy.data.model.AddTudyUiState
import org.tues.tudy.data.model.CreateEventRequest
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.repository.EventRepository

class AddTudyViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AddTudyUiState())
    private val repository = EventRepository(ApiServiceBuilder.apiService)
    val uiState = _uiState.asStateFlow()

    fun createTudy(request: CreateEventRequest) {
        viewModelScope.launch {

            _uiState.value = AddTudyUiState(isLoading = true)

            try {
                repository.createEvent(request)

                _uiState.value = AddTudyUiState(
                    success = true
                )

            } catch (e: Exception) {

                _uiState.value = AddTudyUiState(
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = AddTudyUiState()
    }
}