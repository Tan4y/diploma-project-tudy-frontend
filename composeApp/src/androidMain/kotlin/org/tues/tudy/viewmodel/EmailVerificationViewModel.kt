package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.tues.tudy.data.remote.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import android.util.Log
import org.json.JSONObject
import org.tues.tudy.R

class EmailVerificationViewModel : ViewModel() {

    private val repo = AuthRepository()

    private val _state = MutableStateFlow(EmailVerificationState())
    val state: StateFlow<EmailVerificationState> = _state

    fun verifyEmail(token: String) {
        viewModelScope.launch {
            try {
                _state.value = EmailVerificationState(loading = true)

                repo.verifyEmail(token)

                _state.value = EmailVerificationState(
                    loading = false,
                    success = true
                )

            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("EmailVerificationVM", "HTTP Error: ${e.code()}, Body: $errorBody")

                val errorMessage = try {
                    errorBody?.let {
                        val json = JSONObject(it)
                        json.optString("message", it)
                    } ?: R.string.verification_failed
                } catch (jsonError: Exception) {
                    errorBody ?: R.string.verification_failed
                }

                _state.value = EmailVerificationState(
                    loading = false,
                    error = R.string.verification_failed
                )

            } catch (e: Exception) {
                Log.e("EmailVerificationVM", "Unexpected error: ${e.message}")
                _state.value = EmailVerificationState(
                    loading = false,
                    error = R.string.unexpected_error
                )
            }
        }
    }

    fun setError(message: Int) {
        _state.value = EmailVerificationState(error = message)
    }
}

data class EmailVerificationState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: Int? = null
)