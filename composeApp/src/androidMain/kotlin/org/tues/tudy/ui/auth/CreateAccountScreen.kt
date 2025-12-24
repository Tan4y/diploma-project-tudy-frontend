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
import androidx.compose.ui.platform.LocalContext
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
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.Dimens

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

    var usernameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(state.error) {
        state.error?.let { errorResId ->
            val errorMsg = context.getString(errorResId)
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
                    usernameError = ""
                },
                label = "Username",
                error = usernameError
            )

            Spacer(modifier = Modifier.height(Dimens.Space125))

            CustomTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = ""
                },
                label = "Email",
                error = emailError
            )

            Spacer(modifier = Modifier.height(Dimens.Space125))

            var passwordVisible by remember { mutableStateOf(false) }

            CustomTextField(
                value = password,
                onValueChange = { password = it; passwordError = "" },
                label = "Password",
                error = passwordError,
                trailingIcon = {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.eye_open else R.drawable.eye_closed
                        ),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { passwordVisible = !passwordVisible }
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation()
            )
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
                        usernameError.isEmpty() &&
                        emailError.isEmpty() &&
                        passwordError.isEmpty() &&
                        !state.loading

            CustomButton(
                value = "Create Account",
                enabled = isButtonEnabled,
                onClick = {
                    if (username.isEmpty()) {
                        usernameError = R.string.username_is_required.toString()
                    } else if (!username.matches(Regex("^[a-zA-Z0-9_]+$"))) {
                        usernameError =
                            R.string.username_can_only_contain.toString()
                    }

                    if (email.isEmpty()) {
                        emailError = R.string.email_is_required.toString()
                    } else if (!email.matches(Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+\$"))) {
                        emailError = R.string.invalid_email.toString()
                    }

                    if (password.isEmpty()) {
                        passwordError = R.string.password_is_required.toString()
                    } else if (password.length < 8) {
                        passwordError = R.string.password_length.toString()
                    }

                    if (usernameError.isEmpty() && emailError.isEmpty() && passwordError.isEmpty()) {
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
                LinkButton(value = "Log In", onClick = { navController.navigate(Routes.LOGIN) })
            }
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Verify your email") },
                text = { Text("Check your inbox and confirm your email to finish registration.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            viewModel.resetEmailSent()
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.CREATE_ACCOUNT) { inclusive = true }
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
