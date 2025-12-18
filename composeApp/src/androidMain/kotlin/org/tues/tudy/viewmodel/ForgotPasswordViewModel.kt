package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tues.tudy.R
import org.tues.tudy.data.remote.AuthRepository
import retrofit2.HttpException

class ForgotPasswordViewModel : ViewModel() {
    private val repo = AuthRepository()

    private val _state = MutableStateFlow(ForgotPasswordUsernameState())
    val state: StateFlow<ForgotPasswordUsernameState> = _state

    fun forgotPassword(username: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                repo.forgotPasswordEnterUsername(username)
                _state.value = _state.value.copy(loading = false, emailSent = true)
            } catch (e: HttpException) {
                val errorMsg = when(e.code()) {
                    404 -> R.string.username_not_found
                    else -> R.string.invalid_request
                }
                _state.value = _state.value.copy(loading = false, error = errorMsg)
            }
        }
    }
}

data class ForgotPasswordUsernameState(
    val loading: Boolean = false,
    val emailSent: Boolean = false,
    val error: Int? = null
)
