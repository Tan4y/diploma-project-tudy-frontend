package org.tues.tudy.data.model

data class Event(
    val _id: String,
    val title: String,
    val description: String?,
    val type: String,
    val category: String?,
    val subject: String?,
    val date: String,
    val startTime: String,
    val endTime: String,
    val totalPages: Int?,
    val user: String
)
