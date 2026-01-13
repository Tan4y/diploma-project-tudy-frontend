package org.tues.tudy.data.repository

import android.util.Log
import org.tues.tudy.data.model.CalendarItem
import org.tues.tudy.data.model.Event
import org.tues.tudy.data.remote.ApiService
import org.tues.tudy.utils.toLocalDateSafe
import java.time.LocalDate

class CalendarRepository(
    private val api: ApiService
) {

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
