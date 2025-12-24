package org.tues.tudy.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


fun NavController.navigateToSuccessError(
    title: String,
    subtitle: String,
    description: String,
    buttonText: String,
    buttonDestination: String,
    arrow: Boolean,
    arrowDestination: String? = null,
    success: Boolean,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
    val encodedSubtitle = URLEncoder.encode(subtitle, StandardCharsets.UTF_8.toString())
    val encodedDescription = URLEncoder.encode(description, StandardCharsets.UTF_8.toString())
    val encodedButtonText = URLEncoder.encode(buttonText, StandardCharsets.UTF_8.toString())
    val encodedButtonDest = URLEncoder.encode(buttonDestination, StandardCharsets.UTF_8.toString())
    val encodedArrowDest = arrowDestination?.let {
        URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
    } ?: ""

    navigate(
        "${Routes.SUCCESS_ERROR}/$encodedTitle/$encodedSubtitle/$encodedDescription/$encodedButtonText/$encodedButtonDest/$arrow/$encodedArrowDest/$success",
        builder
    )
}