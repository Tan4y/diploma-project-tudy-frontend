package org.tues.tudy.data.model

data class UserResponse(
    val _id: String,
    val username: String,
    val email: String,
    val studyWindowStart: String = "08:00",
    val studyWindowEnd: String = "22:00"
)
