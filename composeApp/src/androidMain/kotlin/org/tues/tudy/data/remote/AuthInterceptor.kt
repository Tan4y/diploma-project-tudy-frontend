package org.tues.tudy.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import org.tues.tudy.data.model.RefreshRequest
import org.tues.tudy.data.model.TokenManager
import org.tues.tudy.data.storage.TokenStorage
import java.io.IOException

class AuthInterceptor(
    private val authApiService: AuthApiService, // има метод refreshToken
    private val tokenManager: TokenManager
) : Interceptor {

    companion object {
        private var isRefreshing = false
        private val refreshLock = Object()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // 1️⃣ Добавяме access token към всяка заявка
        tokenManager.getAccessToken()?.let { token ->
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }

        var response = chain.proceed(request)

        // 2️⃣ Ако получим 401/403 – опит за рефреш
        if (response.code == 401 || response.code == 403) {
            response.close()

            synchronized(refreshLock) {
                if (!isRefreshing) {
                    isRefreshing = true
                    try {
                        val refreshToken = tokenManager.getRefreshToken()

                        if (!refreshToken.isNullOrEmpty()) {
                            val refreshResponse = authApiService
                                .refreshToken(RefreshRequest(refreshToken))
                                .execute()

                            if (refreshResponse.isSuccessful) {
                                val body = refreshResponse.body()!!
                                // Записваме новите токени
                                tokenManager.saveAccessToken(body.accessToken)
                                body.refreshToken?.let { tokenManager.saveRefreshToken(it) }
                            } else {
                                tokenManager.clearAll() // невалиден refresh token
                                return response
                            }
                        } else {
                            tokenManager.clearAll() // няма refresh token
                            return response
                        }
                    } finally {
                        isRefreshing = false
                    }
                }
            }

            // 3️⃣ Retry със новия access token
            tokenManager.getAccessToken()?.let { newToken ->
                val newRequest = request.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $newToken")
                    .build()
                return chain.proceed(newRequest)
            }
        }

        return response
    }
}
