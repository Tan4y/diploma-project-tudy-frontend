package org.tues.tudy.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.tudy.R
import org.tues.tudy.ui.components.CustomButton
import org.tues.tudy.ui.components.CustomTextField
import org.tues.tudy.ui.components.LinkButton
import org.tues.tudy.ui.components.LogoPlusTitle
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.navigation.navigateToSuccessError
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.viewmodel.LoginState
import org.tues.tudy.viewmodel.LoginViewModel

@Composable
fun LogInScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel(),
    previewState: LoginState? = null
) {
    val state = previewState ?: viewModel.state.collectAsState().value

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    LaunchedEffect(state.success) {
        if (state.success) {
            navController.navigateToSuccessError(
                title = "Log In",
                subtitle = "Welcome Back!",
                description = "You have successfully logged in.",
                buttonText = "Continue",
                buttonDestination = "home",
                arrow = false,
                success = true,
            )
        }
    }

    val context = LocalContext.current

    LaunchedEffect(state.error) {
        state.error?.let { errorResId ->
            val errorMsg = context.getString(errorResId)
            passwordError = when {
                errorMsg.contains("username", ignoreCase = true) ||
                        errorMsg.contains("password", ignoreCase = true) ||
                        errorMsg.contains("credentials", ignoreCase = true) -> {
                    errorMsg
                }

                else -> {
                    errorMsg
                }
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
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoPlusTitle("Log In")

            Spacer(modifier = Modifier.height(Dimens.Space450))

            CustomTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = ""
                },
                label = "Username",
                error = usernameError
            )

            Spacer(modifier = Modifier.height(Dimens.Space125))

            var passwordVisible by remember { mutableStateOf(false) }

            CustomTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = ""
                },
                label = "Password",
                error = passwordError,
                forgotPassword = true,
                onForgotPassword = {
                    navController.navigate(Routes.FORGOT_PASSWORD)
                },
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
        }

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
                        password.isNotEmpty() &&
                        usernameError.isEmpty() &&
                        passwordError.isEmpty() &&
                        !state.loading

            CustomButton(
                value = "Log In",
                enabled = isButtonEnabled,
                onClick = {
                    var valid = true

                    if (username.isEmpty()) {
                        usernameError = "Username is required"
                        valid = false
                    } else if (!username.matches(Regex("^[a-zA-Z0-9_]+$"))) {
                        usernameError =
                            "Username can only contain letters, numbers, and underscores"
                        valid = false
                    }

                    if (password.isEmpty()) {
                        passwordError = "Password is required"
                        valid = false
                    } else if (password.length < 8) {
                        passwordError = "Password must be at least 8 characters long"
                        valid = false
                    }

                    if (valid) {
                        viewModel.login(username, password)
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
                    text = "Do not have an account?",
                    style = AppTypography.Caption1,
                    color = BaseColor100
                )
                LinkButton(
                    value = "Create Account",
                    onClick = { navController.navigate(Routes.CREATE_ACCOUNT) }
                )
            }
        }
    }
}