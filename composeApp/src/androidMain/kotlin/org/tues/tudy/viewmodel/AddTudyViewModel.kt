package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tues.tudy.data.model.AddTudyUiState
import org.tues.tudy.data.model.CreateEventRequest
import org.tues.tudy.data.remote.ApiServiceBuilder

class AddTudyViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AddTudyUiState())
    val uiState = _uiState.asStateFlow()

    fun createTudy(request: CreateEventRequest, userId: String) {
        viewModelScope.launch {
            _uiState.value = AddTudyUiState(isLoading = true)

            try {
                val response = ApiServiceBuilder.apiService.createEvent(request)

                if (response.isSuccessful) {
                    _uiState.value = AddTudyUiState(success = true)
                } else {
                    _uiState.value =
                        AddTudyUiState(error = "Server returned ${response.code()}")
                }

            } catch (e: Exception) {
                _uiState.value =
                    AddTudyUiState(error = e.message ?: "Unknown error")
            }
        }
    }

    fun resetState() {
        _uiState.value = AddTudyUiState()
    }
}