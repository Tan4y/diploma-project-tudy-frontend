package org.tues.tudy.ui.screens.moreMenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.tues.tudy.ui.components.MoreMenuField
import org.tues.tudy.ui.components.TopBar
import org.tues.tudy.ui.components.TopBarMode
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.viewmodel.MoreMenuViewModel

@Composable
fun MoreMenuContent(
    navController: NavController,
    viewModel: MoreMenuViewModel,
    userId: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    visible: Boolean = true
) {
    var selectedSection by rememberSaveable { mutableStateOf("menu") }
    Column {
        TopBar(
            heading = if (selectedSection == "menu") "More" else selectedSection,
            navController = navController,
            userId = userId,
            modifier = modifier,
            mode = if (selectedSection == "menu") TopBarMode.MENU else TopBarMode.BACK,
            isMenuOpen = visible,
            onMenuClick = { onClick() },
            onBack = { selectedSection = "menu" }
        )
        Column(
            modifier = modifier.background(BaseColor0).padding(vertical = Dimens.Space125, horizontal = Dimens.Space100)
        ) {
            when (selectedSection) {
                "menu" -> {
                    MoreMenuField(
                        text = "Account Settings",
                        color = BaseColor100,
                        onClick = { selectedSection = "Account Settings" }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    MoreMenuField(
                        text = "Terms",
                        color = PrimaryColor1,
                        onClick = { selectedSection = "Terms"}
                    )
                    MoreMenuField(
                        text = "Privacy Policy",
                        color = PrimaryColor1,
                        onClick = { selectedSection = "Privacy Policy"}
                    )
                }

                "Account Settings" -> {
                    AccountSettings(
                        navController = navController,
                        viewModel = viewModel,
                        userId = userId,
                        modifier = modifier
                    )
                }

                "Terms" -> {
                    Terms()
                }

                "Privacy Policy" -> {
                    PrivacyPolicy()
                }
            }
        }
    }
}