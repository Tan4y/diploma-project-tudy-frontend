package org.tues.tudy.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.tues.tudy.data.model.CalendarItem
import org.tues.tudy.data.model.Event
import org.tues.tudy.data.model.TypeSubject
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.repository.CalendarRepository
import org.tues.tudy.data.repository.EventRepository
import org.tues.tudy.data.repository.TypeSubjectRepository
import org.tues.tudy.utils.isUpcoming
import org.tues.tudy.utils.sortByTudiesThenAlphabetical

class EventViewModel : ViewModel() {
    private val repository = EventRepository(ApiServiceBuilder.apiService)
    private val _tudiesCount = MutableStateFlow(0)
    val tudiesCount = _tudiesCount.asStateFlow()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events = _events.asStateFlow()

    private val _subjectDates = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val subjectDates = _subjectDates.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    val calendarItems = MutableStateFlow<List<CalendarItem>>(emptyList())

//    fun loadCalendar(userId: String) {
//        viewModelScope.launch {
//            val repository = repository.getCalendarItems()
//            calendarItems.value = repository
//        }
//    }


    private val _studySessions = MutableStateFlow<List<CalendarItem>>(emptyList())
    val studySessions: StateFlow<List<CalendarItem>> = _studySessions.asStateFlow()

    private val typeSubjectRepository: TypeSubjectRepository by lazy {
        TypeSubjectRepository(ApiServiceBuilder.apiService)
    }

    private val calendarRepository: CalendarRepository by lazy {
        CalendarRepository(ApiServiceBuilder.apiService)
    }

    private val _items = MutableStateFlow<List<TypeSubject>>(emptyList())
    val items: StateFlow<List<TypeSubject>> = _items.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()


    private fun extractEventIdFromSessionId(sessionId: String): String {
        return sessionId.substringBeforeLast("-")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadEvents(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1️⃣ Get all events from the backend
                val eventsResponse = repository.getEventsForUser(userId)
                _events.value = eventsResponse.sortedBy { it.date } // use actual event date

                // 2️⃣ Get all calendar sessions separately (for splitting study plan sessions)
                val calendarItems = calendarRepository.getCalendarItems()
                _studySessions.value = calendarItems
                    .filter { it.type == "study" } // include all study events, even 0 pages
                    .sortedBy { it.startDateTime }

                // 3️⃣ Load TypeSubjects
                val typesResponse = typeSubjectRepository.getItems(userId, "type")
                val subjectsResponse = typeSubjectRepository.getItems(userId, "subject")
                val typeSubjects =
                    (typesResponse.body() ?: emptyList()).map { r ->
                        TypeSubject(r._id, r.name, r.tudies, r.iconRes, r.type, r.userId)
                    } +
                            (subjectsResponse.body() ?: emptyList()).map { r ->
                                TypeSubject(r._id, r.name, r.tudies, r.iconRes, r.type, r.userId)
                            }

                _items.value = sortByTudiesThenAlphabetical(
                    typeSubjects,
                    getName = { it.name },
                    getTudies = { it.tudies }
                )

            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.localizedMessage}"
                Log.e("EventVM", "loadEvents ERROR", e)
            } finally {
                _isLoading.value = false
            }
        }
    }



    fun loadTudiesByCategory(userId: String, category: String) {
        viewModelScope.launch {
            try {
                val count = repository.getTudiesCountByCategory(userId, category)
                _tudiesCount.value = count
            } catch (e: Exception) {
                _tudiesCount.value = 0
            }
        }
    }

    fun loadTudiesBySubject(userId: String, subject: String) {
        viewModelScope.launch {
            try {
                val count = repository.getTudiesCountBySubject(userId, subject)
                _tudiesCount.value = count
            } catch (e: Exception) {
                _tudiesCount.value = 0
            }
        }
    }

    fun loadDatesForSubjects(userId: String, subjects: List<String>) {
        viewModelScope.launch {
            val map = mutableMapOf<String, List<String>>()
            subjects.forEach { subject ->
                try {
                    val dates = repository.getEventDatesForSubject(userId, subject)
                    map[subject] = dates
                } catch (e: Exception) {
                    map[subject] = emptyList()
                }
            }
            _subjectDates.value = map
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteEvent(
        userId: String,
        eventId: String,
        onSuccess: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                repository.deleteEvent(eventId)
                loadEvents(userId)
                onSuccess?.invoke()
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "Unknown error")
            }
        }
    }


}
