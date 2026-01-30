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
    val state: StateFlow<CreateAccountState> = _state.asStateFlow()

    private val _emailSent = MutableStateFlow(false)
    val emailSent = _emailSent.asStateFlow()

    fun clearUsernameError() {
        _state.value = _state.value.copy(usernameError = null)
    }

    fun clearEmailError() {
        _state.value = _state.value.copy(emailError = null)
    }

    fun clearPasswordError() {
        _state.value = _state.value.copy(passwordError = null)
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun createAccount(username: String, email: String, password: String) {

        viewModelScope.launch {
            _state.value = CreateAccountState(loading = true)

            try {
                repo.createAccount(username, email, password)

                _state.value = CreateAccountState(success = true)
                _emailSent.value = true

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("CreateAccountVM", "HTTP ${e.code()}: $errorBody")

                _state.value = when {
                    errorBody?.contains("email", ignoreCase = true) == true ->
                        CreateAccountState(emailError = R.string.email_already_exist)

                    errorBody?.contains("username", ignoreCase = true) == true ->
                        CreateAccountState(usernameError = R.string.username_already_exist)

                    else ->
                        CreateAccountState(passwordError = R.string.registration_failed)
                }

                _emailSent.value = false
            } catch (_: Exception) {
                _state.value = CreateAccountState(
                    passwordError = R.string.unexpected_error
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
    val usernameError: Int? = null,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val success: Boolean = false
)

