package org.tues.tudy.ui.screens.moreMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.tues.tudy.ui.components.EmailProfile
import org.tues.tudy.ui.components.TitleDescription
import org.tues.tudy.ui.theme.Dimens

@Composable
fun Terms() {
    val terms = mapOf(
        "Acceptance of Terms" to "By accessing or using Tudy, you acknowledge that you have read, understood, and agree to be bound by these Terms of Service and our Privacy Policy. If you do not agree, you must not use the app.",
        "Purpose of the App" to "Tudy is developed as part of a school diploma project by Tanya Koleva. The app’s purpose is educational — to assist students in managing their study schedules, tracking performance, and improving their learning habits.",
        "User Accounts" to "To use Tudy’s main features, you must create an account. When registering, you agree to provide accurate, complete, and current information, including your name, email, and password. You are responsible for maintaining the confidentiality of your login credentials and for all activities under your account.",
        "Data Collection and Use" to "Tudy collects and stores limited personal and activity data to improve the user experience. Collected information includes:\n" +
                "   • Name\n" +
                "   • Email address\n" +
                "   • Password (securely stored and encrypted)\n" +
                "   • Study time records\n" +
                "   • Tasks created and completed\n" +
                "Your data is used solely for the purpose of providing app functionality and is not shared with third parties.",
        "Acceptable Use" to "You agree to use Tudy only for lawful educational purposes and in accordance with these terms. You may not:\n" +
                "   • Attempt to copy, modify, or distribute the app or its content\n" +
                "   • Use the app to upload or share offensive or illegal content\n" +
                "   • Interfere with the app’s security or performance",
        "Intellectual Property" to "All content, design, and features within Tudy are the intellectual property of Tanya Koleva. You may use the app only as permitted under these Terms and for personal, non-commercial purposes.",
        "Limitation of Liability" to "Tudy is provided “as is” without warranties of any kind. As this is an educational project, Tanya Koleva shall not be held responsible for any data loss, downtime, or errors occurring during app use.",
        "Termination" to "You may delete your account at any time. Tudy reserves the right to suspend or terminate accounts that violate these Terms.",
        "Changes to Terms" to "These Terms may be updated from time to time to reflect improvements or legal requirements. Continued use of the app after such updates constitutes your acceptance of the revised Terms.",
        "Contact" to "For any questions or concerns regarding these Terms or your data, please contact:"
    )
    LazyColumn(verticalArrangement = Arrangement.spacedBy(Dimens.Space125)) {
        items(terms.entries.toList().withIndex().toList()) { indexedValue ->
            val index = indexedValue.index
            val (title, description) = indexedValue.value
            TitleDescription(
                title = "${index + 1}. $title",
                description = description
            )
        }

        item {
            EmailProfile()

            Spacer(modifier = Modifier.height(Dimens.Space125))
        }
    }
}