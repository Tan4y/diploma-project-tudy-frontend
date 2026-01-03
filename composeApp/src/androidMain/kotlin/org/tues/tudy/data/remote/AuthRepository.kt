package org.tues.tudy.data.remote

import org.json.JSONObject
import org.tues.tudy.data.model.CreateAccountRequest
import org.tues.tudy.data.model.LogInRequest
import org.tues.tudy.data.model.LoginResponse
import org.tues.tudy.data.model.RequestResetPasswordRequest
import org.tues.tudy.data.model.ResetPasswordRequest
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthRepository {

    private val api = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5050/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    suspend fun createAccount(username: String, email: String, password: String) {
        val response = api.register(CreateAccountRequest(username, email, password))

        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }

    suspend fun login(username: String, password: String): LoginResponse {
        val response = api.login(LogInRequest(username, password))

        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        return response.body() ?: throw Exception("Empty response body")
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
}