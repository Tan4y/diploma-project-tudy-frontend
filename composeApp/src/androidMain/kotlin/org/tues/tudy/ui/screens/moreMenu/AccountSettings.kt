package org.tues.tudy.ui.screens.moreMenu

import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import org.tues.tudy.R
import org.tues.tudy.ui.components.BasePopUp
import org.tues.tudy.ui.components.CustomTextField
import org.tues.tudy.ui.components.MoreMenuField
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.ErrorColor
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.viewmodel.MoreMenuViewModel

@Composable
fun AccountSettings(
    navController: NavController,
    viewModel: MoreMenuViewModel,
    userId: String,
    modifier: Modifier = Modifier,
) {
    val usernameFromVM by viewModel.username.collectAsState()
    var username by remember { mutableStateOf("") }
    var isEditingUsername by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val emailFromVM by viewModel.email.collectAsState()
    var email by remember { mutableStateOf("") }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(usernameFromVM) {
        username = usernameFromVM ?: ""
    }

    LaunchedEffect(emailFromVM) {
        email = emailFromVM ?: ""
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                    isEditingUsername = false
                }
            }) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Dimens.Space125)
        ) {
            var usernameError by remember { mutableStateOf<String?>(null) }

            LaunchedEffect(username) {
                if (isEditingUsername) {
                    usernameError = when {
                        username.isBlank() -> "Username is required"
                        !username.matches(Regex("^[a-zA-Z0-9_]+$")) ->
                            "Username can only contain letters, numbers, and underscores"

                        else -> null
                    }
                }
            }

            CustomTextField(
                value = username,
                onValueChange = { username = it },
                enabled = isEditingUsername,
                label = "Username",
                modifier = Modifier.fillMaxWidth(),
                error = usernameError,
                trailingIcon = {
                    Icon(
                        painter = painterResource(
                            id = if (isEditingUsername) R.drawable.tick else R.drawable.pencil
                        ),
                        contentDescription = if (isEditingUsername) "Save" else "Edit",
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (isEditingUsername && usernameError == null) { // only save if valid
                                viewModel.updateUsername(userId, username)
                                focusManager.clearFocus()
                                isEditingUsername = false
                            } else if (!isEditingUsername) {
                                focusRequester.requestFocus()
                                isEditingUsername = true
                            }
                        },
                        tint = if (usernameError != null) BaseColor80 else BaseColor100
                    )
                }
            )

            CustomTextField(
                value = email,
                enabled = false,
                label = "Email",
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        MoreMenuField(
            text = "Change Password",
            color = BaseColor100,
            onClick = { navController.navigate(Routes.FORGOT_PASSWORD) }
        )

        MoreMenuField(
            text = "Log Out",
            color = ErrorColor,
            onClick = { showLogoutDialog = true }
        )
        MoreMenuField(
            text = "Delete Account",
            color = ErrorColor,
            onClick = { showDeleteDialog = true }
        )
        if (showDeleteDialog) {
            BasePopUp(
                onDismiss = { showDeleteDialog = false },
                onConfirm = {
                    showDeleteDialog = false
                    viewModel.deleteAccount(userId) {
                        navController.navigate(Routes.CREATE_ACCOUNT) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                title = "Delete Account",
                description = "Are you sure you want to delete your account?"
            )
        }

        if (showLogoutDialog) {
            BasePopUp(
                onDismiss = { showLogoutDialog = false },
                onConfirm = {
                    showLogoutDialog = false
                    viewModel.logout {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                title = "Log Out",
                description = "Are you sure you want to log out?",
                buttonText = "Log Out"
            )
        }

    }
    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }
}
