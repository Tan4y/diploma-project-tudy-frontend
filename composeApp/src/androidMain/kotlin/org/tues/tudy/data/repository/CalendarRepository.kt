package org.tues.tudy.data.repository

import android.util.Log
import org.tues.tudy.data.model.CalendarItem
import org.tues.tudy.data.model.Event
import org.tues.tudy.data.model.UserResponse
import org.tues.tudy.data.remote.ApiService
import org.tues.tudy.utils.toLocalDateSafe
import java.time.LocalDate

class CalendarRepository(
    private val api: ApiService
) {
    private var _currentUser: UserResponse? = null
    val currentUser: UserResponse? get() = _currentUser

    suspend fun getCurrentUser(userId: String): UserResponse? {
        return try {
            val response = api.getUser(userId)
            if (response.isSuccessful) {
                _currentUser = response.body()?.user
                _currentUser
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    suspend fun getCalendarItems(): List<CalendarItem> {
        return try {
            val response = api.getCalendarItems()

            if (response.isSuccessful) {
                val items = response.body() ?: emptyList()
                Log.d("CalendarRepository", "getCalendarItems returned ${items.size} items:")
                items.forEach { item ->
                    Log.d("CalendarRepository", "  - ${item.title} (id: ${item.id}, isStudySession: ${item.isStudySession})")
                }
                items
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
