package org.tues.tudy.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.repository.AuthRepository
import org.tues.tudy.data.repository.CalendarRepository

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
            } catch (e: Exception) {
                // handle error, maybe log
                _username.value = "Username"
                _email.value = "Email"
            }
        }
    }
    fun updateUsername(userId: String, newUsername: String) {

        viewModelScope.launch {
            try {
                authRepository.updateUsername(userId, newUsername)
                // optionally update local state if needed
            } catch (e: Exception) {
                Log.e("MoreMenu", "Failed to update username: ${e.message}")
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