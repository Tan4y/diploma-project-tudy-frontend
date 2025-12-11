package org.tues.tudy.ui.navigation

import androidx.navigation.NavController
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
    success: Boolean
) {
    val encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString())
    val encodedSubtitle = URLEncoder.encode(subtitle, StandardCharsets.UTF_8.toString())
    val encodedDescription = URLEncoder.encode(description, StandardCharsets.UTF_8.toString())
    val encodedButtonText = URLEncoder.encode(buttonText, StandardCharsets.UTF_8.toString())
    val encodedButtonDest = URLEncoder.encode(buttonDestination, StandardCharsets.UTF_8.toString())
    val encodedArrowDest = arrowDestination?.let {
        URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
    } ?: ""

    navigate("successError/$encodedTitle/$encodedSubtitle/$encodedDescription/" +
            "$encodedButtonText/$encodedButtonDest/$arrow/$encodedArrowDest/$success") {
        // Clear back stack to prevent going back
        popUpTo(0) { inclusive = false }
    }
}