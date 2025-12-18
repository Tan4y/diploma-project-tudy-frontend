package org.tues.tudy.data.model

data class ResetPasswordRequest(
    val token: String,
    val newPassword: String
)