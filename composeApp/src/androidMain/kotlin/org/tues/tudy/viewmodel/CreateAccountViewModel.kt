package org.tues.tudy.viewmodel

import retrofit2.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.tues.tudy.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import org.tues.tudy.R

class CreateAccountViewModel : ViewModel() {

    private val repo = AuthRepository()

    private val _state = MutableStateFlow(CreateAccountState())
    val state: StateFlow<CreateAccountState> = _state

    private val _emailSent = MutableStateFlow(false)
    val emailSent = _emailSent.asStateFlow()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun createAccount(username: String, email: String, password: String) {

        viewModelScope.launch {
            try {
                _state.value = CreateAccountState(loading = true)

                repo.createAccount(username, email, password)

                _state.value = CreateAccountState(loading = false)
                _emailSent.value = true

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("CreateAccountVM", "HTTP Error: ${e.code()}, Body: $errorBody")

                // Parse error message
                val errorMessage = when {
                    errorBody?.contains("email", ignoreCase = true) == true ->
                        R.string.email_already_exist
                    errorBody?.contains("username", ignoreCase = true) == true ->
                        R.string.username_already_exist
                    else -> errorBody ?: R.string.registration_failed
                }

                _state.value = CreateAccountState(
                    loading = false,
                    error = R.string.email_already_exist
                )

                _emailSent.value = false
                Log.d("CreateAccountVM", "State after error: ${_state.value}, emailSent: ${_emailSent.value}")
            } catch (e: Exception) {
                _state.value = CreateAccountState(
                    loading = false,
                    error = R.string.unexpected_error
                )
                _emailSent.value = false
            }
        }
    }

    fun resetEmailSent() {
        _emailSent.value = false
    }
}

data class CreateAccountState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: Int? = null
)