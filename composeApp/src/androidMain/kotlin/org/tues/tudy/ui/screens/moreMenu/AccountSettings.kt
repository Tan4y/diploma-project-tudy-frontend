package org.tues.tudy.ui.screens.moreMenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.ErrorColor
import org.tues.tudy.viewmodel.MoreMenuViewModel

@Composable
fun AccountSettings(
    navController: NavController,
    viewModel: MoreMenuViewModel,
    userId: String,
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

    var frontendError by remember { mutableStateOf<String?>(null) }
    var backendError by remember { mutableStateOf<String?>(null) }

    val usernameError = frontendError ?: backendError

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
            }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Dimens.Space125)
        ) {
            // Frontend validation
            LaunchedEffect(username) {
                if (isEditingUsername) {
                    frontendError = when {
                        username.isBlank() -> "Username is required"
                        !username.matches(Regex("^[a-zA-Z0-9_]+$")) ->
                            "Username can only contain letters, numbers, and underscores"
                        else -> null
                    }
                }
            }

            CustomTextField(
                value = username,
                onValueChange = {
                    username = it
                    frontendError = null
                    backendError = null
                },
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
                            if (!isEditingUsername) {
                                focusRequester.requestFocus()
                                isEditingUsername = true
                            } else if (usernameError == null) {
                                viewModel.updateUsername(userId, username) { errorFromBackend ->
                                    backendError = errorFromBackend
                                }
                                if (backendError == null) {
                                    isEditingUsername = false
                                }
                            }
                        },
                        tint = if (usernameError != null) ErrorColor else BaseColor100
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
                description = "Are you sure you want to delete your account?",
                buttonText = "Delete"
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
