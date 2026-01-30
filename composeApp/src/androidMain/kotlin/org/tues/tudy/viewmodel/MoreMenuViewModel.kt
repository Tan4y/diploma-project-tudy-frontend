package org.tues.tudy.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tues.tudy.data.repository.AuthRepository

class MoreMenuViewModel : ViewModel() {
    private val authRepository: AuthRepository by lazy {
        AuthRepository()
    }

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    fun loadUser(userId: String) {
        viewModelScope.launch {
            try {
                val userResponse = authRepository.getUser(userId)
                _username.value = userResponse.username
                _email.value = userResponse.email
            } catch (_: Exception) {
                _username.value = "Username"
                _email.value = "Email"
            }
        }
    }
    fun updateUsername(userId: String, newUsername: String, onError: (String) -> Unit) {

        viewModelScope.launch {
            try {
                authRepository.updateUsername(userId, newUsername)
                _username.value = newUsername
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Failed to update username"
                onError(errorMessage)
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                authRepository.logout()
                onSuccess()
            } catch (e: Exception) {
                Log.e("Logout", "Logout failed", e)
            }
        }
    }



    fun deleteAccount(
        userId: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                authRepository.deleteUser(userId)
                onSuccess()
            } catch (e: Exception) {
                Log.e("MoreMenu", "Failed to delete account: ${e.message}")
            }
        }
    }

}