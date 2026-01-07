package org.tues.tudy.data.model

data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String? = null
)