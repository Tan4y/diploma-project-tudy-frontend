package org.tues.tudy.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import org.tues.tudy.data.model.RefreshRequest
import org.tues.tudy.data.storage.TokenStorage

class AuthInterceptor(
    private val authApiService: AuthApiService
) : Interceptor {

    companion object {
        private var isRefreshing = false
        private val refreshLock = Object()
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()

        // attach access token
        TokenStorage.getAccessToken()?.let { token ->
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }

        var response = chain.proceed(request)

        // expired token?
        if (response.code == 401 || response.code == 403) {

            response.close()

            synchronized(refreshLock) {

                // if already refreshing, just wait
                if (!isRefreshing) {

                    isRefreshing = true
                    Log.d("AuthInterceptor", "Access token expired → refreshing… (Status: ${response.code})")

                    try {
                        val refreshToken = TokenStorage.getRefreshToken()

                        if (refreshToken != null) {

                            val refreshResponse =
                                authApiService.refreshToken(
                                    RefreshRequest(refreshToken)
                                ).execute()

                            if (refreshResponse.isSuccessful) {

                                val body = refreshResponse.body()!!

                                TokenStorage.saveAccessToken(body.accessToken)

                                body.refreshToken?.let {
                                    TokenStorage.saveRefreshToken(it)
                                }

                                Log.d("AuthInterceptor", "Token refreshed successfully")

                            } else {
                                Log.e("AuthInterceptor", "Refresh failed (${refreshResponse.code()}) → clearing tokens")
                                TokenStorage.clear()
                                return response  // ← Return error response
                            }
                        } else {
                            Log.e("AuthInterceptor", "No refresh token available")
                            TokenStorage.clear()
                            return response  // ← Return error response
                        }

                    } catch (e: Exception) {
                        Log.e("AuthInterceptor", "Token refresh exception: ${e.message}")
                        TokenStorage.clear()
                        return response  // ← Return error response
                    } finally {
                        isRefreshing = false
                    }
                }
            }

            // retry request with new token
            return TokenStorage.getAccessToken()?.let { newToken ->
                val newRequest = request.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $newToken")
                    .build()

                chain.proceed(newRequest)
            } ?: run {
                Log.e("AuthInterceptor", "No token after refresh attempt")
                response  // ← Return error if no token
            }
        }

        return response
    }
}