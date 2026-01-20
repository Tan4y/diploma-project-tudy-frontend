package org.tues.tudy.data.model

import android.content.Context
import android.content.SharedPreferences
import org.tues.tudy.App

class TokenManager(private val context: Context = App.Companion.context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveAccessToken(token: String) = prefs.edit().putString("access_token", token).apply()
    fun getAccessToken(): String? = prefs.getString("access_token", null)
    fun clearAccessToken() = prefs.edit().remove("access_token").apply()

    fun saveUserId(userId: String) = prefs.edit().putString("user_id", userId).apply()
    fun getUserId(): String? = prefs.getString("user_id", null)
    fun clearUserId() = prefs.edit().remove("user_id").apply()

    fun clearAll() = prefs.edit().clear().apply()
}