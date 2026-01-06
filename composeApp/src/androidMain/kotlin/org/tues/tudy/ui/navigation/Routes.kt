package org.tues.tudy.ui.navigation

import android.net.Uri

object Routes {
    const val LOGIN = "login"
    const val CREATE_ACCOUNT = "createAccount"
    const val EMAIL_VERIFICATION = "emailVerification"
    const val SUCCESS_ERROR = "successError"
    const val FORGOT_PASSWORD = "forgotPassword"
    const val RESET_PASSWORD = "resetPassword"

    const val STUDY = "study"
    const val CALENDAR = "calendar"
    const val ADD_TUDY = "addTudy"
    const val ADD_TUDY_WITH_USER = "addTudy/{userId}"
    const val PROFILE = "profile"
    const val HOME = "home"
    const val HOME_WITH_USER = "home/{userId}"
    const val TYPE_SUBJECT = "typeSubjectPage/{userId}/{title}/{clickedIsType}"

    fun homeRoute(userId: String) = "home/$userId"
    fun addTudyRoute(userId: String) = "addTudy/$userId"
    fun typeSubjectPageRoute(userId: String, title: String, clickedIsType: Boolean) = "typeSubjectPage/$userId/${Uri.encode(title)}/$clickedIsType"
}
