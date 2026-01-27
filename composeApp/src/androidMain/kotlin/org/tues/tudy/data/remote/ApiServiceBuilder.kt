package org.tues.tudy.data.remote

import okhttp3.OkHttpClient
import org.tues.tudy.data.model.TokenManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceBuilder {
    private const val BASE_URL = "http://10.0.2.2:5050/"

    private val authRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApiService: AuthApiService = authRetrofit.create(AuthApiService::class.java)

    private val tokenManager = TokenManager()

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(authApiService, tokenManager))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
