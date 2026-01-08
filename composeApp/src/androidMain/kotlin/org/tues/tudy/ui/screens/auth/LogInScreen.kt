package org.tues.tudy.ui.screens.auth

import android.util.Log
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
    val state by viewModel.state.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    LaunchedEffect(state.success, state.error) {
        when {
            state.success -> {
                val currentUserId = state.userId ?: ""
                Log.d("LogInScreen", "Login success with userId: '$currentUserId'")

                if (currentUserId.isEmpty()) {
                    Log.e("LogInScreen", "ERROR: userId is empty after successful login!")
                    return@LaunchedEffect
                }

                navController.navigateToSuccessError(
                    title = "Log In",
                    subtitle = "Welcome Back!",
                    description = "You have successfully logged in.",
                    buttonText = "Continue",
                    buttonDestination = Routes.homeRoute(currentUserId),
                    arrow = false,
                    success = true
                ) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            }

            state.error != null -> {
                navController.navigateToSuccessError(
                    title = "Log In",
                    subtitle = "Log in unsuccessful",
                    description = "There was an error while trying to log in.",
                    buttonText = "Try Again",
                    buttonDestination = Routes.LOGIN,
                    arrow = false,
                    success = false
                )
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
                navController = navController,
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
                        usernameError = R.string.username_is_required.toString()
                        valid = false
                    } else if (!username.matches(Regex("^[a-zA-Z0-9_]+$"))) {
                        usernameError = R.string.username_can_only_contain.toString()
                        valid = false
                    }

                    if (password.isEmpty()) {
                        passwordError = R.string.password_is_required.toString()
                        valid = false
                    } else if (password.length < 8) {
                        passwordError = R.string.password_length.toString()
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