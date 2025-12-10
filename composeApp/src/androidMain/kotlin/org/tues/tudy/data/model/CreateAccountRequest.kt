package org.tues.tudy.data.model

data class CreateAccountRequest(
    val username: String,
    val email: String,
    val password: String
)
