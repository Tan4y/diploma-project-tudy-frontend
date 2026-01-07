package org.tues.tudy.data.repository

import org.tues.tudy.data.model.Event
import org.tues.tudy.data.remote.ApiService

class EventRepository(private val api: ApiService) {

    suspend fun getEvents(): List<Event> {
        val response = api.getEvents()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Failed to fetch events")
        }
    }


    suspend fun getTudiesCountByCategory(category: String): Int {
        val response = api.getEvents()
        if (response.isSuccessful) {
            val events = response.body() ?: emptyList()
            return events.count { it.type == "study" && it.category == category }
        } else {
            throw Exception("Failed to fetch events")
        }
    }

    suspend fun getTudiesCountBySubject(subject: String): Int {
        val response = api.getEvents()
        if (response.isSuccessful) {
            val events = response.body() ?: emptyList()
            return events.count { it.type == "study" && it.subject == subject }
        } else {
            throw Exception("Failed to fetch events")
        }
    }

    suspend fun getEventDatesForSubject(subject: String): List<String> {
        val response = api.getEvents()
        if (response.isSuccessful) {
            val events = response.body() ?: emptyList()
            // Filter by subject and type "study", return their date strings
            return events.filter { it.type == "study" && it.subject == subject }
                .map { it.date } // assuming 'date' is String in ISO format
        } else {
            throw Exception("Failed to fetch events")
        }
    }

    suspend fun deleteEvent(eventId: String) {
        val response = api.deleteEvent(eventId)
        if (!response.isSuccessful) {
            throw Exception("Failed to delete event")
        }
    }
}