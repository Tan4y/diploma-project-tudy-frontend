package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.tues.tudy.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import android.util.Log
import org.json.JSONObject
import org.tues.tudy.R
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.storage.TokenStorage

class LoginViewModel : ViewModel() {

    private val repo = AuthRepository()

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _state.value = LoginState(error = R.string.all_fields_are_required)
            return
        }

        viewModelScope.launch {
            try {
                _state.value = LoginState(loading = true)

                val response = repo.login(username, password)
                Log.d("LoginVM", "Login response: $response")

                if (response.accessToken.isNullOrEmpty() || response.user?.id.isNullOrEmpty()) {
                    _state.value = LoginState(
                        loading = false,
                        error = R.string.log_in_failed
                    )
                    return@launch
                }

                TokenStorage.saveAccessToken(response.accessToken)

                response.refreshToken?.let {
                    TokenStorage.saveRefreshToken(it)
                }

                _state.value = LoginState(
                    loading = false,
                    success = true,
                    userId = response.user.id
                )

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("LoginVM", "HTTP Error: ${e.code()}, Body: $errorBody")

                // Parse JSON error message
                val errorMessage = try {
                    errorBody?.let {
                        val json = JSONObject(it)
                        json.optString("message", it)
                    } ?: R.string.log_in_failed
                } catch (jsonError: Exception) {
                    errorBody ?: R.string.log_in_failed
                }

                // Customize error messages
                val finalMessage = when {
                    errorBody?.contains("credentials", ignoreCase = true) == true ->
                        R.string.invalid_username_or_password
                    errorBody?.contains("not verified", ignoreCase = true) == true ->
                        R.string.verify_your_email_first
                    else -> errorMessage
                }

                _state.value = LoginState(
                    loading = false,
                    error = finalMessage as? Int ?: R.string.log_in_failed
                )

            } catch (e: Exception) {
                Log.e("LoginVM", "Unexpected error: ${e.message}")
                _state.value = LoginState(
                    loading = false,
                    error = R.string.unexpected_error
                )
            }
        }
    }
}

data class LoginState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: Int? = null,
    val userId: String? = null
)