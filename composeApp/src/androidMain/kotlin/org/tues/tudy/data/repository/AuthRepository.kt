package org.tues.tudy.data.repository

import org.json.JSONObject
import org.tues.tudy.data.model.CreateAccountRequest
import org.tues.tudy.data.model.LogInRequest
import org.tues.tudy.data.model.LoginResponse
import org.tues.tudy.data.model.RequestResetPasswordRequest
import org.tues.tudy.data.model.ResetPasswordRequest
import org.tues.tudy.data.model.UserResponse
import org.tues.tudy.data.remote.ApiService
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.model.TokenManager
import retrofit2.HttpException

class AuthRepository {

    private val api: ApiService = ApiServiceBuilder.apiService
    private val tokenManager = TokenManager()

    suspend fun createAccount(username: String, email: String, password: String) {
        val response = api.register(CreateAccountRequest(username, email, password))

        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

    suspend fun getUser(userId: String): UserResponse {
        val response = api.getUser(userId)
        if (!response.isSuccessful) throw HttpException(response)
        return response.body()?.user
            ?: throw Exception("Empty user response")
    }


    suspend fun login(username: String, password: String): LoginResponse {
        val response = api.login(LogInRequest(username, password))
        if (!response.isSuccessful) throw HttpException(response)
        val body = response.body() ?: throw Exception("Empty body")

        tokenManager.saveAccessToken(body.accessToken)
        body.refreshToken?.let { tokenManager.saveRefreshToken(it) }
        tokenManager.saveUserId(body.user.id)

        return body
    }


    suspend fun logout() {
        api.logout()
        tokenManager.clearAll()
    }

    suspend fun verifyEmail(token: String) {
        val response = api.verifyEmail(token)

        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

    suspend fun forgotPasswordEnterUsername(username: String) {
        val request = RequestResetPasswordRequest(username)
        val response = api.requestResetPassword(request)

        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

    suspend fun resetPassword(token: String, newPassword: String) {
        val request = ResetPasswordRequest(token, newPassword)
        val response = api.resetPassword(request)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

    suspend fun updateUsername(userId: String, newUsername: String) {
        try {
            val response = api.updateUsername(userId, newUsername) // Retrofit call
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val message = errorBody?.let {
                    JSONObject(it).optString("message", "Unknown error")
                } ?: "Unknown error"
                throw Exception(message)
            }
        } catch (e: Exception) {
            throw e
        }
    }


    suspend fun deleteUser(userId: String) {
        val response = api.deleteUser(userId)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

}