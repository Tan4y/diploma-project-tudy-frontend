package org.tues.tudy.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tues.tudy.R
import org.tues.tudy.data.repository.AuthRepository
import retrofit2.HttpException
import kotlin.invoke

class ResetPasswordViewModel : ViewModel() {
    private val repo = AuthRepository()
    private var resetToken: String = ""

    private val _state = MutableStateFlow(ResetPasswordState())
    val state: StateFlow<ResetPasswordState> = _state

    fun resetPassword(token: String, newPassword: String, confirmPassword: String) {
        if (token.isBlank()) {
            _state.value = _state.value.copy(error = R.string.invalid_or_expired_token)
            return
        }
        if (newPassword != confirmPassword) {
            _state.value = _state.value.copy(error = R.string.password_do_not_match)
            return
        }
        if (newPassword.length < 8) {
            _state.value = _state.value.copy(error = R.string.password_length)
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                repo.resetPassword(token, newPassword)
                _state.value = _state.value.copy(loading = false, success = true)
            } catch (e: HttpException) {
                val errorMsg = when(e.code()) {
                    400 -> R.string.invalid_request
                    else -> R.string.reset_password_failed
                }
                _state.value = _state.value.copy(loading = false, error = errorMsg)
            }
        }
    }
}

data class ResetPasswordState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: Int? = null
)
