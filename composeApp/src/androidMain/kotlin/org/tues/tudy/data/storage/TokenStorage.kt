package org.tues.tudy.data.storage

import android.util.Log

object TokenStorage {

    @Volatile
    private var accessToken: String? = null

    @Volatile
    private var refreshToken: String? = null

    fun getAccessToken() = accessToken
    fun getRefreshToken() = refreshToken

    fun saveAccessToken(token: String) {
        accessToken = token
        Log.d("TokenStorage", "Access token saved: $accessToken")
    }

    fun saveRefreshToken(token: String) {
        refreshToken = token
        Log.d("TokenStorage", "Refresh token saved: $refreshToken")
    }

    fun clear() {
        accessToken = null
        refreshToken = null
    }
}

