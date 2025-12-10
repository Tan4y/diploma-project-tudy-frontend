package org.tues.tudy.ui.auth

import android.os.Build
import androidx.annotation.RequiresExtension
import org.tues.tudy.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import org.tues.tudy.ui.components.CustomButton
import org.tues.tudy.ui.components.LinkButton
import org.tues.tudy.viewmodel.CreateAccountState
import org.tues.tudy.viewmodel.CreateAccountViewModel
import androidx.navigation.NavController
import org.tues.tudy.ui.components.CustomTextField
import org.tues.tudy.ui.components.LogoPlusTitle
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.ErrorColor

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun CreateAccountScreen(
    navController: NavController,
    viewModel: CreateAccountViewModel = viewModel(),
    previewState: CreateAccountState? = null
) {
    val state = previewState ?: viewModel.state.collectAsState().value

    var showSuccessDialog by remember { mutableStateOf(false) }
    val emailSent by viewModel.emailSent.collectAsState()
    LaunchedEffect(emailSent) {
        if (emailSent) {
            showSuccessDialog = true
        }
    }

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(state.error) {
        state.error?.let { errorMsg ->
            when {
                errorMsg.contains("Email", ignoreCase = true) -> emailError = errorMsg
                errorMsg.contains("Username", ignoreCase = true) -> usernameError = errorMsg
                else -> passwordError = errorMsg
            }
        }
    }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Space100)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() }
    ) {

        // MAIN CONTENT
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LogoPlusTitle("Create Account")

            Spacer(modifier = Modifier.height(Dimens.Space450))

            CustomTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = null
                },
                label = "Username",
                error = usernameError
            )

            Spacer(modifier = Modifier.height(Dimens.Space125))

            CustomTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                label = "Email",
                error = emailError
            )

            Spacer(modifier = Modifier.height(Dimens.Space125))

            var passwordVisible by remember { mutableStateOf(false) }

            CustomTextField(
                value = password,
                onValueChange = { password = it; passwordError = null },
                label = "Password",
                error = passwordError,
                trailingIcon = {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.eye_open else R.drawable.eye_closed
                        ),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation()
            )

            if (state.error != null) {
                Text(
                    text = state.error,
                    color = ErrorColor,
                    style = AppTypography.Caption1,
                )
            }
        }


        // BOTTOM SECTION
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = Dimens.Space75)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.Space75)
        ) {

            val isButtonEnabled =
                username.isNotEmpty() &&
                        email.isNotEmpty() &&
                        password.isNotEmpty() &&
                        usernameError == null &&
                        emailError == null &&
                        passwordError == null &&
                        !state.loading

            CustomButton(
                value = "Create Account",
                enabled = isButtonEnabled,
                onClick = {
                    if (username.isEmpty()) {
                        usernameError = "Username is required"
                    } else if (!username.matches(Regex("^[a-zA-Z0-9_]+$"))) {
                        usernameError = "Username can only contain letters, numbers, and underscores"
                    }

                    if (email.isEmpty()) {
                        emailError = "Email is required"
                    } else if (!email.matches(Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+\$"))) {
                        emailError = "Invalid email address"
                    }

                    if (password.isEmpty()) {
                        passwordError = "Password is required"
                    } else if (password.length < 8) {
                        passwordError = "Password must be at least 8 characters long"
                    }

                    if (usernameError == null && emailError == null && passwordError == null) {
                        viewModel.createAccount(username, email, password)
                    }
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Space25),
                horizontalArrangement = Arrangement.spacedBy(
                    Dimens.Space25,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account?",
                    style = AppTypography.Caption1,
                    color = BaseColor100
                )
                LinkButton(value = "Log In", onClick = { navController.navigate("login") })
            }
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = {
                    showSuccessDialog = false
                    viewModel.resetEmailSent()
                },
                title = { Text("Account Created") },
                text = { Text("An email has been sent to your inbox.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            viewModel.resetEmailSent()
                            navController.navigate("login") {
                                popUpTo("createAccount") { inclusive = true }
                            }
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
