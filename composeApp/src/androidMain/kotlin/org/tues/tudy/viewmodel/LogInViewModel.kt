package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.tues.tudy.data.remote.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repo = AuthRepository()

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _state.value = LoginState(error = "All fields are required")
            return
        }

        viewModelScope.launch {
            try {
                _state.value = LoginState(loading = true)
                val response = repo.login(username, password)
                _state.value = LoginState(success = response)
            } catch (e: Exception) {
                _state.value = LoginState(error = e.message ?: "Unexpected error")
            }
        }
    }
}

data class LoginState(
    val loading: Boolean = false,
    val success: String? = null,
    val error: String? = null
)
