package org.tues.tudy.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceBuilder {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5050/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
