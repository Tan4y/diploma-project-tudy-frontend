package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.tues.tudy.data.model.Subject
import org.tues.tudy.data.model.Type
import org.tues.tudy.R
import org.tues.tudy.utils.sortByTudiesThenAlphabetical

class HomeViewModel : ViewModel() {

    private val _types = MutableStateFlow<List<Type>>(emptyList())
    val types: StateFlow<List<Type>> = _types

    private val _subjects = MutableStateFlow<List<Subject>>(emptyList())
    val subjects: StateFlow<List<Subject>> = _subjects

    // Map: Subject name -> list of event dates
    private val _events = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val events: StateFlow<Map<String, List<String>>> = _events

    init {
        loadData()
    }

    private fun loadData() {
        val defaultTypes = listOf(
            Type("Exam", 0, R.drawable.type_exam),
            Type("Quiz", 0, R.drawable.type_quiz),
            Type("Assignment", 0, R.drawable.type_homework)
        )

        val defaultSubjects = listOf(
            Subject("Mathematics", 0, R.drawable.subject_mathematics),
            Subject("Physics", 0, R.drawable.subject_physics),
            Subject("Biology", 0, R.drawable.subject_biology),
            Subject("Chemistry", 0, R.drawable.subject_chemistry),
            Subject("History", 0, R.drawable.subject_history),
            Subject("Geography", 0, R.drawable.subject_geography),
            Subject("English", 0, R.drawable.subject_english),
            Subject("Art", 0, R.drawable.subject_art),
            Subject("Music", 0, R.drawable.subject_music),
            Subject("Computer Science", 0, R.drawable.subject_computer_science)
        )

        val userTypes = loadUserTypes()
        val userSubjects = loadUserSubjects()

        val combinedTypes = defaultTypes + userTypes
        val combinedSubjects = defaultSubjects + userSubjects

        _types.value = sortByTudiesThenAlphabetical(
            combinedTypes,
            getName = { it.name },
            getTudies = { it.tudies }
        )

        _subjects.value = sortByTudiesThenAlphabetical(
            combinedSubjects,
            getName = { it.name },
            getTudies = { it.tudies }
        )
    }

    private fun loadUserTypes(): List<Type> = emptyList()
    private fun loadUserSubjects(): List<Subject> = emptyList()

    fun getEventsForSubject(subject: Subject): List<String> {
        return if (subject.tudies > 0) {
            _events.value[subject.name] ?: emptyList()
        } else {
            emptyList()
        }
    }
}
