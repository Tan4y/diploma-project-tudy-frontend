package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tues.tudy.data.model.CalendarDay
import org.tues.tudy.data.model.CalendarItem
import org.tues.tudy.data.model.User
import org.tues.tudy.data.model.UserResponse
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.repository.CalendarRepository
import org.tues.tudy.data.repository.EventRepository
import org.tues.tudy.data.repository.TypeSubjectRepository
import org.tues.tudy.ui.components.CalendarMode
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

    private val _calendarMode = MutableStateFlow(CalendarMode.MONTH)
    val calendarMode: StateFlow<CalendarMode> = _calendarMode.asStateFlow()

    private val _selectedDay = MutableStateFlow(LocalDate.now())
    val selectedDay = _selectedDay.asStateFlow()


    private val _currentUser = MutableStateFlow<UserResponse?>(null)
    val currentUser: StateFlow<UserResponse?> = _currentUser.asStateFlow()

    init {
        viewModelScope.launch {
            _currentUser.value = repository.getCurrentUser(userId)
        }
    }


    fun setSelectedDay(date: LocalDate) {
        _selectedDay.value = date

        val dayMonth = YearMonth.from(date)
        if (_selectedMonth.value != dayMonth) {
            _selectedMonth.value = dayMonth
            loadMonth(dayMonth)
        }

        _selectedWeekStart.value =
            date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    }


    fun setCalendarMode(mode: CalendarMode) {
        _calendarMode.value = mode

        when (mode) {
            CalendarMode.WEEK -> {
                val baseDate =
                    if (_selectedDay.value.month == _selectedMonth.value.month) {
                        _selectedDay.value
                    } else {
                        _selectedMonth.value.atDay(1)
                    }

                val weekStart =
                    baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

                _selectedWeekStart.value = weekStart

                val weekMonth = YearMonth.from(weekStart)
                if (_selectedMonth.value != weekMonth) {
                    _selectedMonth.value = weekMonth
                    loadMonth(weekMonth)
                }
            }

            CalendarMode.DAY -> {
                val selected = _selectedDay.value
                val selectedMonth = YearMonth.from(selected)

                if (_selectedMonth.value != selectedMonth) {
                    _selectedMonth.value = selectedMonth
                    loadMonth(selectedMonth)
                }

                _selectedWeekStart.value =
                    selected.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            }


            CalendarMode.MONTH -> {
                // no-op
            }
        }
    }

    private val _selectedWeekStart = MutableStateFlow<LocalDate?>(null)
    val selectedWeekStart = _selectedWeekStart.asStateFlow()

    val userStudyWindowStart: String
        get() = _currentUser.value?.studyWindowStart ?: "08:00"

    val userStudyWindowEnd: String
        get() = _currentUser.value?.studyWindowEnd ?: "22:00"


    fun setSelectedWeek(date: LocalDate) {
        val weekStart = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        _selectedWeekStart.value = weekStart

        val weekMonth = YearMonth.from(weekStart)
        if (_selectedMonth.value != weekMonth) {
            _selectedMonth.value = weekMonth
            loadMonth(weekMonth)
        }
    }


    fun getWeekDays(startOfWeek: LocalDate?): List<CalendarDay> {
        if (startOfWeek == null) return emptyList()

        return (0..6).map { offset ->
            val date = startOfWeek.plusDays(offset.toLong())
            val itemsForDay = _days.value.find { it.date == date }?.items ?: emptyList()

            CalendarDay(
                date = date,
                isCurrentMonth = true, // optional: you can check month if needed
                items = itemsForDay,
                eventsCount = itemsForDay.size
            )
        }
    }




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

        _selectedWeekStart.value =
            if (month == YearMonth.now()) {
                LocalDate.now()
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            } else {
                month.atDay(1)
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            }

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
