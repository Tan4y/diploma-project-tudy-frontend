package org.tues.tudy.data.remote

import okhttp3.ResponseBody
import org.tues.tudy.data.model.CreateAccountRequest
import org.tues.tudy.data.model.LogInRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(
        @Body request: CreateAccountRequest
    ): Response<ResponseBody>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LogInRequest
    ): Response<ResponseBody>
}
