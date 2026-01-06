package org.tues.tudy.data.model

data class CreateEventRequest(
    val title: String,
    val description: String?,
    val type: String,
    val category: String?,
    val subject: String?,
    val date: String,       // ISO string yyyy-MM-dd
    val startTime: String,  // ISO string yyyy-MM-dd'T'HH:mm:ss
    val endTime: String,    // ISO string yyyy-MM-dd'T'HH:mm:ss
    val pages: Int? = null
)