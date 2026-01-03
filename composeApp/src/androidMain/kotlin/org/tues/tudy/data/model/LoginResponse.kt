package org.tues.tudy.data.model

data class LoginResponse(
    val message: String,
    val accessToken: String,
    val refreshToken: String,
    val user: UserDto
)