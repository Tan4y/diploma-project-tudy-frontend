package org.tues.tudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tues.tudy.data.model.Event
import org.tues.tudy.data.model.TypeSubject
import org.tues.tudy.data.remote.ApiServiceBuilder
import org.tues.tudy.data.repository.EventRepository
import org.tues.tudy.data.repository.TypeSubjectRepository

class TypeSubjectViewModel: ViewModel() {

    private val repository = TypeSubjectRepository(ApiServiceBuilder.apiService)

    private val _items = MutableStateFlow<List<TypeSubject>>(emptyList())
    val items: StateFlow<List<TypeSubject>> = _items

    // StateFlow for errors
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    fun loadItems(userId: String, type: String) {
        viewModelScope.launch {
            try {
                val response = repository.getItems(userId, type)
                if (response.isSuccessful) {
                    _items.value = response.body()?.map {
                        TypeSubject(
                            _id = it._id,
                            name = it.name,
                            tudies = it.tudies,
                            iconRes = it.iconRes,
                            type = it.type,
                            userId = it.userId
                        )
                    } ?: emptyList()
                } else {
                    _items.value = emptyList()
                    _errorMessage.value = "Failed to load items: ${response.code()}"
                }
            } catch (e: Exception) {
                _items.value = emptyList()
                _errorMessage.value = "Error loading items: ${e.localizedMessage}"
            }
        }
    }

    fun loadEventsForUser(userId: String) {
        viewModelScope.launch {
            try {
                val response = ApiServiceBuilder.apiService.getEvents()
                if (response.isSuccessful) {
                    _events.value = response.body()?.filter { it.user == userId } ?: emptyList()
                } else {
                    _events.value = emptyList()
                }
            } catch (e: Exception) {
                _events.value = emptyList()
            }
        }
    }

    // Returns events grouped by subject for a given type
    fun getSubjectsWithEventsForType(type: String): Map<String, List<Event>> {
        return _events.value
            .filter { it.type == "study" && it.category == type }
            .groupBy { it.subject ?: "Unknown Subject" }
    }

    // Returns events grouped by type for a given subject
    fun getTypesWithEventsForSubject(subject: String): Map<String, List<Event>> {
        return _events.value
            .filter { it.type == "study" && it.subject == subject }
            .groupBy { it.category ?: "Unknown Type" }
    }

    fun deleteTypeSubject(userId: String, item: TypeSubject) {
        viewModelScope.launch {
            try {
                val response = repository.deleteItem(userId, item._id!!)
                if (response.isSuccessful) {
                    _items.value = _items.value.filter { it._id != item._id }
                    loadEventsForUser(userId)
                } else {
                    _errorMessage.value = "Failed to delete item: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error deleting item: ${e.localizedMessage}"
            }
        }
    }
}