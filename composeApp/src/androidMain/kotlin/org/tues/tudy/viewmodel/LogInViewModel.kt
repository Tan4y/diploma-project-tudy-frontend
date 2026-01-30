package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.tues.tudy.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import android.util.Log
import org.tues.tudy.R
import org.tues.tudy.data.storage.TokenStorage

class LoginViewModel : ViewModel() {

    private val repo = AuthRepository()

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun clearAuthErrors() {
        _state.value = _state.value.copy(
            usernameError = null,
            passwordError = null
        )
    }

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _state.value = LoginState(
                usernameError = if (username.isBlank()) R.string.username_is_required else null,
                passwordError = if (password.isBlank()) R.string.password_is_required else null
            )
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
                        usernameError = R.string.invalid_username_or_password,
                        passwordError = R.string.invalid_username_or_password
                    )
                    return@launch
                }

                TokenStorage.saveAccessToken(response.accessToken)
                response.refreshToken?.let { TokenStorage.saveRefreshToken(it) }

                _state.value = LoginState(
                    loading = false,
                    success = true,
                    userId = response.user.id
                )

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("LoginVM", "HTTP Error: ${e.code()}, Body: $errorBody")

                val newState = when {
                    errorBody?.contains("username", ignoreCase = true) == true ||
                            errorBody?.contains("password", ignoreCase = true) == true ||
                            e.code() == 401 -> {
                        // Show under both fields
                        LoginState(
                            loading = false,
                            usernameError = R.string.invalid_username_or_password,
                            passwordError = R.string.invalid_username_or_password
                        )
                    }

                    errorBody?.contains("not verified", ignoreCase = true) == true -> {
                        LoginState(
                            loading = false,
                            passwordError = R.string.verify_your_email_first
                        )
                    }

                    else -> {
                        LoginState(
                            loading = false,
                            passwordError = R.string.log_in_failed
                        )
                    }
                }

                _state.value = newState
            } catch (e: Exception) {
                Log.e("LoginVM", "Unexpected error: ${e.message}")
                _state.value = _state.value.copy(
                    loading = false,
                    usernameError = null,
                    passwordError = null
                )
            }
        }
    }
}

data class LoginState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val userId: String? = null
)
