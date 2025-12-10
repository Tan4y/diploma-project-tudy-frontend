package org.tues.tudy.data.remote

import org.tues.tudy.data.model.CreateAccountRequest
import org.tues.tudy.data.model.LogInRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthRepository {

    private val api = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5050/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    suspend fun createAccount(username: String, email: String, password: String): String {
        val response = api.register(CreateAccountRequest(username, email, password))

        if (response.isSuccessful) {
            return "Check your email to verify your account"
        } else {
            val error = response.errorBody()?.string()
            return error ?: "Unknown error"
        }
    }

    suspend fun login(username: String, password: String): String {
        val response = api.login(LogInRequest(username, password))

        if (response.isSuccessful) {
            return "Successful log in"
        } else {
            val error = response.errorBody()?.string()
            return error ?: "Unknown error"
        }
    }
}
