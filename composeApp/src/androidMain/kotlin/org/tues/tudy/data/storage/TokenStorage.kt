package org.tues.tudy.data.storage

object TokenStorage {

    @Volatile
    private var accessToken: String? = null

    @Volatile
    private var refreshToken: String? = null

    fun getAccessToken() = accessToken
    fun getRefreshToken() = refreshToken

    fun saveAccessToken(token: String) {
        accessToken = token
    }

    fun saveRefreshToken(token: String) {
        refreshToken = token
    }

    fun clear() {
        accessToken = null
        refreshToken = null
    }
}

