package org.tues.tudy.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tues.tudy.R
import org.tues.tudy.data.model.TypeSubject
import org.tues.tudy.data.model.TypeSubjectRequest
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.remote.TypeSubjectRepository
import org.tues.tudy.utils.sortByTudiesThenAlphabetical

class HomeViewModel : ViewModel() {

    private val repository: TypeSubjectRepository by lazy {
        try {
            TypeSubjectRepository(ApiServiceBuilder.apiService)
        } catch (e: Exception) {
            Log.e("HomeVM", "Failed to initialize repository", e)
            throw e
        }
    }

    private val _items = MutableStateFlow<List<TypeSubject>>(emptyList())
    val items: StateFlow<List<TypeSubject>> = _items.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // Track loaded userId instead of just a boolean
    private var loadedUserId: String? = null

    // Predefined icons
    private val availableTypeIcons = listOf(
        R.drawable.type_exam,
        R.drawable.type_quiz,
        R.drawable.type_homework,
    )

    private val availableSubjectIcons = listOf(
        R.drawable.subject_art,
        R.drawable.subject_biology,
        R.drawable.subject_chemistry,
        R.drawable.subject_computer_science,
        R.drawable.subject_crafts,
        R.drawable.subject_economics,
        R.drawable.subject_english,
        R.drawable.subject_enterpreneur,
        R.drawable.subject_frances,
        R.drawable.subject_geography,
        R.drawable.subject_german,
        R.drawable.subject_history,
        R.drawable.subject_italian,
        R.drawable.subject_japanese,
        R.drawable.subject_language,
        R.drawable.subject_literature,
        R.drawable.subject_mathematics,
        R.drawable.subject_music,
        R.drawable.subject_physics,
        R.drawable.subject_spanish,
        R.drawable.subject_speaking,
        R.drawable.subject_sport
    )

//    private val _subjectItems = MutableStateFlow<List<NameIcon>>(emptyList())
//    val subjectItems: StateFlow<List<NameIcon>> = _subjectItems.asStateFlow()
//
//    private val _typeItems = MutableStateFlow<List<NameIcon>>(emptyList())
//    val typeItems: StateFlow<List<NameIcon>> = _typeItems.asStateFlow()

    init {
//        viewModelScope.launch {
//            _items.collect { list ->
//                _subjectItems.value = list
//                    .filter { it.type == "subject" }
//                    .map { NameIcon(it.name, getIconForName(it.name)) }
//
//                _typeItems.value = list
//                    .filter { it.type == "type" }
//                    .map { NameIcon(it.name, getIconForName(it.name)) }
//            }
//        }


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
        Log.d("HomeVM", "loadData CALLED with userId=$userId")
        viewModelScope.launch {
            try {
                val typesResponse = repository.getItems(userId, "type")
                Log.d("HomeVM", "typesResponse = ${typesResponse.code()} body=${typesResponse.body()}")

                val subjectsResponse = repository.getItems(userId, "subject")
                Log.d("HomeVM", "subjectsResponse = ${subjectsResponse.code()} body=${subjectsResponse.body()}")

                val userServerItems: List<TypeSubject> = listOf(
                    (typesResponse.body() ?: emptyList()).map { response ->
                        TypeSubject(
                            _id = response._id,
                            name = response.name,
                            tudies = response.tudies,
                            iconRes = response.iconRes,
                            type = response.type,
                            userId = response.userId
                        )
                    },
                    (subjectsResponse.body() ?: emptyList()).map { response ->
                        TypeSubject(
                            _id = response._id,
                            name = response.name,
                            tudies = response.tudies,
                            iconRes = response.iconRes,
                            type = response.type,
                            userId = response.userId
                        )
                    }
                ).flatten()

                val localPending = _items.value.filter { it._id == null }

                val mergedPending = localPending.filter { pending ->
                    userServerItems.none { it.name == pending.name && it.type == pending.type }
                }

                val merged = sortByTudiesThenAlphabetical(
                    userServerItems + mergedPending,
                    getName = { it.name },
                    getTudies = { it.tudies }
                )

                Log.d("HomeVM", "Setting items to: ${merged.map { "${it.name}(${it.type})" }}")
                _items.value = merged

                _items.value.forEach {
                    Log.d("HomeVM", "Loaded item: ${it.name}, type: ${it.type}, id: ${it._id}")
                }

            } catch (e: Exception) {
                Log.e("HomeVM", "loadData ERROR", e)
                _errorMessage.value = "Network error: ${e.localizedMessage}"
            }
        }
    }

    fun addTypeSubject(userId: String, name: String, iconRes: Int, type: String) {
        Log.d("HomeVM", "addTypeSubject called for $name")
        val newItem = TypeSubject(
            _id = null,
            name = name,
            tudies = 0,
            iconRes = iconRes,
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
                val response = repository.addItem(TypeSubjectRequest(name, iconRes, userId, type))
                if (response.isSuccessful) {
                    val returned = response.body()!!
                    val confirmedItem = TypeSubject(
                        _id = returned._id,
                        name = returned.name,
                        tudies = returned.tudies,
                        iconRes = returned.iconRes,
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

    fun getEventsForSubject(subject: TypeSubject): List<String> {
        return if (subject.tudies > 0) {
            emptyList()
        } else emptyList()
    }
}