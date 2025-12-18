package org.tues.tudy.data.remote

import okhttp3.ResponseBody
import org.tues.tudy.data.model.CreateAccountRequest
import org.tues.tudy.data.model.LogInRequest
import org.tues.tudy.data.model.RequestResetPasswordRequest
import org.tues.tudy.data.model.ResetPasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(
        @Body request: CreateAccountRequest
    ): Response<ResponseBody>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LogInRequest
    ): Response<ResponseBody>

    @GET("verify-email")
    suspend fun verifyEmail(
        @Query("token") token: String
    ): Response<Unit>

    @POST("api/auth/request-reset")
    suspend fun requestResetPassword(
        @Body request: RequestResetPasswordRequest
    ): Response<ResponseBody>

    @POST("api/auth/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<ResponseBody>
}