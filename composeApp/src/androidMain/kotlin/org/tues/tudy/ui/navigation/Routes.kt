package org.tues.tudy.ui.navigation

import android.net.Uri

object Routes {
    const val LOGIN = "login"
    const val CREATE_ACCOUNT = "createAccount"
    const val EMAIL_VERIFICATION = "emailVerification"
    const val SUCCESS_ERROR = "successError"
    const val FORGOT_PASSWORD = "forgotPassword"
    const val RESET_PASSWORD = "resetPassword"

    const val STUDY = "study/{userId}"
    const val CALENDAR = "calendar"
    const val CALENDAR_WITH_USER = "calendar/{userId}"
    const val CALENDAR_DAY = "calendar/{userId}/day/{date}"

    const val ADD_TUDY = "addTudy"
    const val ADD_TUDY_WITH_USER = "addTudy/{userId}"
    const val PROFILE = "profile"
    const val HOME = "home"
    const val HOME_WITH_USER = "home/{userId}"
    const val TYPE_SUBJECT = "typeSubjectPage/{userId}/{title}/{clickedIsType}"
    const val CALENDAR_WEEK = "calendar/{userId}/week"

    const val MORE = "more/{userId}"


    fun homeRoute(userId: String) = "home/$userId"
    fun addTudyRoute(userId: String) = "addTudy/$userId"
    fun typeSubjectPageRoute(userId: String, title: String, clickedIsType: Boolean) = "typeSubjectPage/$userId/${Uri.encode(title)}/$clickedIsType"
    fun calendarRoute(userId: String) = "calendar/$userId"
    fun calendarDayRoute(userId: String, date: String) =
        "calendar/$userId/day/$date"

    fun calendarWeekRoute(userId: String) = "calendar/$userId/week"
    fun studyRoute(userId: String) = "study/$userId"
    fun moreRoute(userId: String) = "more/$userId"
}
