package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.tues.tudy.data.remote.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateAccountViewModel : ViewModel() {

    private val repo = AuthRepository()

    private val _state = MutableStateFlow(CreateAccountState())
    val state: StateFlow<CreateAccountState> = _state

    fun createAccount(username: String, email: String, password: String) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _state.value = CreateAccountState(error = "All fields are required")
            return
        }

        viewModelScope.launch {
            try {
                _state.value = CreateAccountState(loading = true)
                val response = repo.createAccount(username, email, password)
                _state.value = CreateAccountState(success = response)
            } catch (e: Exception) {
                _state.value = CreateAccountState(error = e.message ?: "Unexpected error")
            }
        }
    }
}

data class CreateAccountState(
    val loading: Boolean = false,
    val success: String? = null,
    val error: String? = null
)
