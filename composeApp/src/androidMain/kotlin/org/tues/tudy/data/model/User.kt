package org.tues.tudy.data.model

import java.util.Date

data class User(
    val username: String,
    val email: String,
    val password: String,
    val isVerified: Boolean,
    val verificationToken: String,
    val resetToken: String,
    val resetTokenExpiry: Date,
    val wakeTime: String,
    val sleepTime: String,
    val studyWindowStart: String,
    val studyWindowEnd: String,
    val preferredMinSessionMinutes: Int,
    val preferredMaxSessionMinutes: Int,
    val studySessions: StudySessionResponse,
    val totalStudyMinutes: Int,
    val completedStudySessions: Int,
)