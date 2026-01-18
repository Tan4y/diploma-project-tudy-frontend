package org.tues.tudy.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tues.tudy.R
import org.tues.tudy.data.model.CalendarItem
import org.tues.tudy.data.model.StudyPlanResponse
import org.tues.tudy.data.model.TypeSubject
import org.tues.tudy.data.model.TypeSubjectRequest
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.repository.CalendarRepository
import org.tues.tudy.data.repository.TypeSubjectRepository
import org.tues.tudy.utils.sortByTudiesThenAlphabetical


class HomeViewModel : ViewModel() {

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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Track loaded userId instead of just a boolean
    private var loadedUserId: String? = null

    // Predefined icons
    // Predefined icons as Strings (icon names)
    private val availableTypeIcons = listOf(
        "type_exam",
        "type_quiz",
        "type_homework",
    )

    private val availableSubjectIcons = listOf(
        "subject_art",
        "subject_biology",
        "subject_chemistry",
        "subject_computer_science",
        "subject_crafts",
        "subject_economics",
        "subject_english",
        "subject_enterpreneur",
        "subject_frances",
        "subject_geography",
        "subject_german",
        "subject_history",
        "subject_italian",
        "subject_japanese",
        "subject_language",
        "subject_literature",
        "subject_mathematics",
        "subject_music",
        "subject_physics",
        "subject_spanish",
        "subject_speaking",
        "subject_sport"
    )

    fun mapIconNameToDrawable(iconName: String): Int {
        return when(iconName) {
            "type_exam" -> R.drawable.type_exam
            "type_quiz" -> R.drawable.type_quiz
            "type_homework" -> R.drawable.type_homework

            "subject_art" -> R.drawable.subject_art
            "subject_biology" -> R.drawable.subject_biology
            "subject_chemistry" -> R.drawable.subject_chemistry
            "subject_computer_science" -> R.drawable.subject_computer_science
            "subject_crafts" -> R.drawable.subject_crafts
            "subject_economics" -> R.drawable.subject_economics
            "subject_english" -> R.drawable.subject_english
            "subject_enterpreneur" -> R.drawable.subject_enterpreneur
            "subject_frances" -> R.drawable.subject_frances
            "subject_geography" -> R.drawable.subject_geography
            "subject_german" -> R.drawable.subject_german
            "subject_history" -> R.drawable.subject_history
            "subject_italian" -> R.drawable.subject_italian
            "subject_japanese" -> R.drawable.subject_japanese
            "subject_language" -> R.drawable.subject_language
            "subject_literature" -> R.drawable.subject_literature
            "subject_mathematics" -> R.drawable.subject_mathematics
            "subject_music" -> R.drawable.subject_music
            "subject_physics" -> R.drawable.subject_physics
            "subject_spanish" -> R.drawable.subject_spanish
            "subject_speaking" -> R.drawable.subject_speaking
            "subject_sport" -> R.drawable.subject_sport

            else -> R.drawable.type_homework // fallback only
        }
    }


    private val _studySessions = MutableStateFlow<List<CalendarItem>>(emptyList())
    val studySessions: StateFlow<List<CalendarItem>> = _studySessions.asStateFlow()


    init {
        Log.d("IconRes", "Homework icon = ${R.drawable.type_homework}")
        Log.d("IconRes", "Exam icon = ${R.drawable.type_exam}")
        Log.d("IconRes", "Quiz icon = ${R.drawable.type_quiz}")

        Log.d("IconRes", "Biology icon = ${R.drawable.subject_biology}")
        Log.d("IconRes", "Chemistry icon = ${R.drawable.subject_chemistry}")
        Log.d("IconRes", "Computer Science icon = ${R.drawable.subject_computer_science}")
        Log.d("IconRes", "English icon = ${R.drawable.subject_english}")
        Log.d("IconRes", "Geography icon = ${R.drawable.subject_geography}")
        Log.d("IconRes", "History icon = ${R.drawable.subject_history}")
        Log.d("IconRes", "Literature icon = ${R.drawable.subject_literature}")
        Log.d("IconRes", "Mathematics icon = ${R.drawable.subject_mathematics}")
        Log.d("IconRes", "Physics icon = ${R.drawable.subject_physics}")
        Log.d("IconRes", "Sport icon = ${R.drawable.subject_sport}")
    }

    fun getTypeIcons() = availableTypeIcons
    fun getSubjectIcons() = availableSubjectIcons

    val types: List<TypeSubject>
        get() = _items.value.filter { it.type == "type" }

    val subjects: List<TypeSubject>
        get() = _items.value.filter { it.type == "subject" }

    fun ensureLoaded(userId: String) {
        if (loadedUserId != userId) {
            loadedUserId = userId
            loadData(userId)
        }
    }

    fun loadData(userId: String) {
        if (userId.isBlank()) {
            _errorMessage.value = "Invalid user ID"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // ✅ Single source of truth
                val calendarItems = calendarRepository.getCalendarItems()

                // ✅ Study sessions are already CalendarItem
                _studySessions.value = calendarItems
                    .filter { it.type == "study" }
                    .sortedBy { it.startDateTime }

                // ✅ Load TypeSubjects (unchanged)
                val typesResponse = typeSubjectRepository.getItems(userId, "type")
                val subjectsResponse = typeSubjectRepository.getItems(userId, "subject")

                val typeSubjects =
                    (typesResponse.body() ?: emptyList()).map { r ->
                        TypeSubject(r._id, r.name, r.tudies, r.iconName ?: "type_homework", r.type, r.userId)
                    } +
                            (subjectsResponse.body() ?: emptyList()).map { r ->
                                TypeSubject(r._id, r.name, r.tudies, r.iconName ?: "type_homework", r.type, r.userId)
                            }

                _items.value = sortByTudiesThenAlphabetical(
                    typeSubjects,
                    getName = { it.name },
                    getTudies = { it.tudies }
                )
                Log.d("HomeVM", "Loaded items: ${_items.value.map { it.name + "(${it.type})" }}")


            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.localizedMessage}"
                Log.e("HomeVM", "loadData ERROR", e)
            } finally {
                _isLoading.value = false
            }
        }
    }



//    fun loadData(userId: String) {
//        Log.d("HomeVM", "loadData CALLED with userId='$userId' (length=${userId.length})")
//
//        if (userId.isBlank()) {
//            Log.e("HomeVM", "ERROR: userId is blank or empty!")
//            _errorMessage.value = "Invalid user ID"
//            return
//        }
//
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//
//                Log.d("HomeVM", "Calling repository.getItems with userId='$userId'")
//                val typesResponse = repository.getItems(userId, "type")
//                Log.d("HomeVM", "typesResponse = ${typesResponse.code()} body=${typesResponse.body()}")
//
//                val subjectsResponse = repository.getItems(userId, "subject")
//                Log.d("HomeVM", "subjectsResponse = ${subjectsResponse.code()} body=${subjectsResponse.body()}")
//
//                val userServerItems: List<TypeSubject> = listOf(
//                    (typesResponse.body() ?: emptyList()).map { response ->
//                        TypeSubject(
//                            _id = response._id,
//                            name = response.name,
//                            tudies = response.tudies,
//                            iconRes = response.iconRes,
//                            type = response.type,
//                            userId = response.userId
//                        )
//                    },
//                    (subjectsResponse.body() ?: emptyList()).map { response ->
//                        TypeSubject(
//                            _id = response._id,
//                            name = response.name,
//                            tudies = response.tudies,
//                            iconRes = response.iconRes,
//                            type = response.type,
//                            userId = response.userId
//                        )
//                    }
//                ).flatten()
//
//                val localPending = _items.value.filter { it._id == null }
//
//                val mergedPending = localPending.filter { pending ->
//                    userServerItems.none { it.name == pending.name && it.type == pending.type }
//                }
//
//                val merged = sortByTudiesThenAlphabetical(
//                    userServerItems + mergedPending,
//                    getName = { it.name },
//                    getTudies = { it.tudies }
//                )
//
//                Log.d("HomeVM", "Setting items to: ${merged.map { "${it.name}(${it.type})" }}")
//                _items.value = merged
//
//                _items.value.forEach {
//                    Log.d("HomeVM", "Loaded item: ${it.name}, type: ${it.type}, id: ${it._id}")
//                }
//
//            } catch (e: Exception) {
//                Log.e("HomeVM", "loadData ERROR", e)
//                _errorMessage.value = "Network error: ${e.localizedMessage}"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

    fun addTypeSubject(userId: String, name: String, iconName: String, type: String) {
        Log.d("HomeVM", "addTypeSubject called for $name")
        val newItem = TypeSubject(
            _id = null,
            name = name,
            tudies = 0,
            iconName = iconName,
            type = type,
            userId = userId
        )

        _items.value = sortByTudiesThenAlphabetical(
            _items.value + newItem,
            getName = { it.name },
            getTudies = { it.tudies }
        )

        Log.d("HomeVM", "After local add, _items = ${_items.value.map { it.name + "(${it._id})" }}")

        viewModelScope.launch {
            try {
                val response = typeSubjectRepository.addItem(TypeSubjectRequest(name, iconName, userId, type))
                if (response.isSuccessful) {
                    val returned = response.body()!!
                    val confirmedItem = TypeSubject(
                        _id = returned._id,
                        name = returned.name,
                        tudies = returned.tudies,
                        iconName = returned.iconName,
                        type = returned.type,
                        userId = returned.userId
                    )

                    _items.value = _items.value.map {
                        if (it._id == null && it.name == confirmedItem.name && it.type == confirmedItem.type && it.userId == confirmedItem.userId)
                            confirmedItem
                        else it
                    }
                } else {
                    _items.value = _items.value.filter { it._id != null || it.name != name || it.type != type }
                }
            } catch (e: Exception) {
                Log.e("HomeVM", "Failed to sync item: $name", e)
            }
        }
    }

//    fun deleteTypeSubject(userId: String, item: TypeSubject) {
//        viewModelScope.launch {
//            try {
//                repository.deleteItem(item._id!!)
//            } finally {
//                loadData(userId)
//            }
//        }
//    }

}