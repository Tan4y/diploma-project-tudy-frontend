package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.tues.tudy.data.remote.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import android.util.Log

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

                repo.login(username, password)

                _state.value = LoginState(
                    loading = false,
                    success = true
                )

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("LoginVM", "HTTP Error: ${e.code()}, Body: $errorBody")

                val errorMessage = when {
                    errorBody?.contains("credentials", ignoreCase = true) == true ->
                        "Invalid username or password"
                    errorBody?.contains("not verified", ignoreCase = true) == true ->
                        "Please verify your email first"
                    else -> errorBody ?: "Login failed"
                }

                _state.value = LoginState(
                    loading = false,
                    error = errorMessage
                )

            } catch (e: Exception) {
                Log.e("LoginVM", "Unexpected error: ${e.message}")
                _state.value = LoginState(
                    loading = false,
                    error = e.message ?: "Unexpected error"
                )
            }
        }
    }
}

data class LoginState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)