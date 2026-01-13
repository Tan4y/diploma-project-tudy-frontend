package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tues.tudy.data.model.CalendarDay
import org.tues.tudy.data.model.CalendarItem
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.repository.CalendarRepository
import org.tues.tudy.data.repository.EventRepository
import org.tues.tudy.data.repository.TypeSubjectRepository
import org.tues.tudy.utils.toLocalDateSafe
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters

class CalendarViewModel(
    private val repository: CalendarRepository,
    private val userId: String
) : ViewModel() {


    private val eventRepository: EventRepository by lazy {
        EventRepository(ApiServiceBuilder.apiService)
    }
    private val _days = MutableStateFlow<List<CalendarDay>>(emptyList())
    val days: StateFlow<List<CalendarDay>> = _days.asStateFlow()

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()

    private val _selectedMonth = MutableStateFlow(YearMonth.now())
    val selectedMonth = _selectedMonth.asStateFlow()


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


//    fun loadMonth(userId: String) {
//        viewModelScope.launch {
//            val month = _currentMonth.value
//            val eventsByDate = repository.getEventsForMonth(
//                userId = userId,
//                year = month.year,
//                month = month.monthValue
//            )
//
//            _days.value = buildMonthGrid(month, eventsByDate)
//        }
//    }


    fun loadMonth(month: YearMonth = _selectedMonth.value) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1️⃣ Fetch all study sessions
                val sessions = repository.getCalendarItems()

                // 2️⃣ Fetch all parent events using EventRepository
                val events = eventRepository.getEventsForUser(userId)

                val allItems = mutableListOf<CalendarItem>()

                // 3️⃣ Add all sessions
                allItems.addAll(sessions)

                // 4️⃣ Add events with totalPages = 0 or events with no sessions
                events.forEach { event ->
                    val hasSession = sessions.any { it.id.startsWith(event._id) }
                    if (!hasSession || (event.totalPages ?: 0) == 0) {
                        allItems.add(
                            CalendarItem(
                                id = event._id,
                                title = event.title,
                                description = event.description ?: "",
                                date = event.date,
                                startTime = event.startTime ?: "",
                                endTime = event.endTime ?: "",
                                type = event.type,
                                isStudySession = false,
                                subject = event.subject ?: "Unknown",
                                category = event.category
                            )
                        )
                    }
                }

                // 5️⃣ Map each item to the correct date
                val itemsByDate: Map<LocalDate, List<CalendarItem>> = allItems.groupBy { item ->
                    if (item.isStudySession) {
                        val parentId = item.id.substringBefore("-")
                        events.find { it._id == parentId }?.date?.toLocalDateSafe() ?: item.date.toLocalDateSafe()
                    } else {
                        item.date.toLocalDateSafe()
                    }
                }

                // 6️⃣ Build month grid
                _days.value = buildMonthGrid(month, itemsByDate)

            } catch (e: Exception) {
                _days.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun changeMonth(month: YearMonth) {
        _selectedMonth.value = month
        loadMonth(month)
    }

//    fun nextMonth(userId: String) {
//        _currentMonth.value = _currentMonth.value.plusMonths(1)
//        loadMonth(userId)
//    }
//
//    fun previousMonth(userId: String) {
//        _currentMonth.value = _currentMonth.value.minusMonths(1)
//        loadMonth(userId)
//    }
//
//    fun setMonth(userId: String, month: YearMonth) {
//        _currentMonth.value = month
//        loadMonth(userId)
//    }


    private fun buildMonthGrid(
        month: YearMonth,
        itemsByDate: Map<LocalDate, List<CalendarItem>>
    ): List<CalendarDay> {

        val firstDayOfMonth = month.atDay(1)
        val startDay = firstDayOfMonth.with(
            TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
        )

        val days = mutableListOf<CalendarDay>()
        var currentDate = startDay

        repeat(42) { // 6 weeks grid
            val itemsForDay = itemsByDate[currentDate] ?: emptyList()

            days.add(
                CalendarDay(
                    date = currentDate,
                    isCurrentMonth = currentDate.month == month.month,
                    items = itemsForDay,
                    eventsCount = itemsForDay.size
                )
            )

            currentDate = currentDate.plusDays(1)
        }

        return days
    }


//    private fun buildMonthGrid(
//        month: YearMonth,
//        events: Map<LocalDate, List<Any>>
//    ): List<CalendarDay> {
//
//        val firstOfMonth = month.atDay(1)
//        val lastOfMonth = month.atEndOfMonth()
//
//        val startOffset =
//            (firstOfMonth.dayOfWeek.value % 7) // Monday = 1 → 0
//
//        val startDate = firstOfMonth.minusDays(startOffset.toLong())
//
//        return (0 until 42).map { index ->
//            val date = startDate.plusDays(index.toLong())
//            CalendarDay(
//                date = date,
//                isCurrentMonth = date.month == month.month,
//                eventsCount = events[date]?.size ?: 0
//            )
//        }
//    }
}
