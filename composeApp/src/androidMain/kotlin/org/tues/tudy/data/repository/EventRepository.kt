package org.tues.tudy.data.repository

import org.tues.tudy.data.model.Event
import org.tues.tudy.data.remote.ApiService

class EventRepository(private val api: ApiService) {

    suspend fun getEventsForUser(userId: String): List<Event> {
        return api.getEvents().body()?.filter { it.user == userId } ?: emptyList()
    }

    suspend fun getTudiesCountByCategory(userId: String, category: String): Int {
        val response = api.getEvents()
        if (response.isSuccessful) {
            val events = response.body() ?: emptyList()
            return events.count { it.type == "study" && it.user == userId && it.category == category }
        } else throw Exception("Failed to fetch events")
    }

    suspend fun getTudiesCountBySubject(userId: String, subject: String): Int {
        val response = api.getEvents()
        if (response.isSuccessful) {
            val events = response.body() ?: emptyList()
            return events.count { it.type == "study" && it.user == userId && it.subject == subject }
        } else throw Exception("Failed to fetch events")
    }

    suspend fun getEventDatesForSubject(userId: String, subject: String): List<String> {
        val response = api.getEvents()
        if (response.isSuccessful) {
            val events = response.body() ?: emptyList()
            return events.filter { it.type == "study" && it.user == userId && it.subject == subject }
                .map { it.date }
        } else throw Exception("Failed to fetch events")
    }


    suspend fun deleteEvent(eventId: String) {
        val response = api.deleteEvent(eventId)
        if (!response.isSuccessful) {
            throw Exception("Failed to delete event")
        }
    }
}