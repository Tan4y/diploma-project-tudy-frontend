package org.tues.tudy.data.model

data class TypeSubjectResponse(
    val _id: String,
    val name: String,
    val tudies: Int,
    val iconRes: Int,
    val userId: String,
    val type: String
)