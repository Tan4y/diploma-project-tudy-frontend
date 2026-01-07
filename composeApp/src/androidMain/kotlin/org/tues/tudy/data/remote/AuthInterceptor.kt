package org.tues.tudy.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import org.tues.tudy.data.model.RefreshRequest
import org.tues.tudy.data.storage.TokenStorage

class AuthInterceptor(
    private val authApiService: AuthApiService
) : Interceptor {

    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("AuthInterceptor", "Refreshing token…")

        var request = chain.request()

        // Attach access token
        TokenStorage.getAccessToken()?.let { token ->
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }

        var response = chain.proceed(request)

        // Token expired?
        if (response.code == 401 || response.code == 403) {
            response.close()

            val refreshToken = TokenStorage.getRefreshToken()

            if (refreshToken != null) {

                val refreshResponse =
                    authApiService.refreshToken(RefreshRequest(refreshToken)).execute()

                if (refreshResponse.isSuccessful) {

                    val body = refreshResponse.body()!!

                    TokenStorage.saveAccessToken(body.accessToken)

                    // If backend rotates refresh token
                    body.refreshToken?.let {
                        TokenStorage.saveRefreshToken(it)
                    }

                    val newRequest = request.newBuilder()
                        .removeHeader("Authorization")
                        .addHeader("Authorization", "Bearer ${body.accessToken}")
                        .build()

                    return chain.proceed(newRequest)
                } else {
                    TokenStorage.clear()
                }
            }
        }

        return response
    }
}
