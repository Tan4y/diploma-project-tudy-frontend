package org.tues.tudy.data.remote

import org.tues.tudy.data.model.RefreshRequest
import org.tues.tudy.data.model.RefreshResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/auth/refresh")
    fun refreshToken(
        @Body request: RefreshRequest
    ): Call<RefreshResponse>
}