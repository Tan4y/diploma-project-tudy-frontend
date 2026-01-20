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
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun PrivacyPolicy() {
    val terms = mapOf(
        "Information We Collect" to "When you use Tudy, we collect the following types of information:\n\n" +
        "Personal Information:\n"+
        "Provided by you during registration or account setup:\n" +
                "   • Name\n" +
                "   • Email address\n" +
                "   • Password (encrypted and never shared)\n\n"+
        "Activity Data\n"+
        "Automatically or manually collected while you use the app:\n" +
                "   • Study time records\n" +
                "   • Tasks created and completed\n" +
                "   • Progress tracking data",
        "How We Use Your Information" to "Your data is used solely to provide and improve the app’s core features, including:\n" +
                "   • Managing your account and saving your progress\n" +
                "   • Generating personalized study insights and statistics\n" +
                "   • Enhancing your study planning experience\n" +
                "We do not sell, rent, or share your personal information with third parties.",
        "Data Storage and Security" to "We use secure methods to protect your information from unauthorized access, alteration, or disclosure. Passwords are encrypted, and personal data is stored safely within the app’s database.\n" +
                "However, as Tudy is a school diploma project, you acknowledge that some limitations may exist regarding enterprise-level security infrastructure. We take all reasonable steps to maintain data safety.",
        "Your Rights" to "You have the right to:\n" +
                "   • Access the data stored about you\n" +
                "   • Request correction of inaccurate information\n" +
                "   • Request deletion of your account and all associated data\n" +
                "To exercise these rights, please contact us.",
        "Data Retention" to "Your personal data will be retained as long as your account remains active. If you delete your account, all associated personal information and study records will be permanently removed.",
        "Children’s Privacy" to "Tudy is designed for students, but not specifically directed at children under 13. If we learn that we have unintentionally collected personal data from a child under 13, we will promptly delete it.",
        "Changes to This Policy" to "We may update this Privacy Policy from time to time. All updates will be reflected in this document with a new “Effective Date.” Your continued use of the app after such updates means you accept the revised policy.",
        "Contact Information" to "If you have questions or concerns about this Privacy Policy or how we handle your data, please contact:",
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
            EmailProfile(owner = true)

            Spacer(modifier = Modifier.height(Dimens.Space125))
        }
    }
}
