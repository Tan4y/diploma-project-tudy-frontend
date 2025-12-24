package org.tues.tudy.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.tues.tudy.ui.components.CustomButton
import org.tues.tudy.ui.components.CustomTextField
import org.tues.tudy.ui.components.LogoPlusTitle
import org.tues.tudy.ui.components.ProgressBar
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.viewmodel.ResetPasswordViewModel
import org.tues.tudy.R
import org.tues.tudy.ui.common.SuccessErrorScreen
import org.tues.tudy.ui.navigation.navigateToSuccessError

@Composable
fun ResetPasswordScreen(
    navController: NavController,
    viewModel: ResetPasswordViewModel
) {
    val state by viewModel.state.collectAsState()
    var token by remember { mutableStateOf("") } // <--- добавяме token
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmError by remember { mutableStateOf("") }

    LaunchedEffect(state.success) {
        if (state.success) {
            navController.navigateToSuccessError(
                    title = "Success",
                    subtitle = "Password Changed",
                    description = "Your password has been updated successfully.",
                    buttonText = "Log In",
                    buttonDestination = Routes.LOGIN,
                    arrow = false,
                    arrowDestination = "",
                    success = true
            ) {
                popUpTo(Routes.RESET_PASSWORD) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoPlusTitle("Reset Password")

        Spacer(modifier = Modifier.height(24.dp))

        // Поле за token
        CustomTextField(
            value = token,
            onValueChange = { token = it },
            label = "Reset Token",
            error = if (token.isEmpty()) "Required" else null
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = newPassword,
            onValueChange = { newPassword = it; passwordError = "" },
            label = "New Password",
            error = passwordError.ifEmpty { null },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it; confirmError = "" },
            label = "Confirm Password",
            error = confirmError.ifEmpty { null },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(
            value = "Reset Password",
            enabled = newPassword.isNotEmpty() && confirmPassword.isNotEmpty() && token.isNotEmpty(),
            onClick = {
                if (newPassword != confirmPassword) {
                    passwordError = "Passwords do not match"
                } else {
                    viewModel.resetPassword(token, newPassword, confirmPassword)
                }
            }
        )

        if (state.loading) {
            Spacer(modifier = Modifier.height(16.dp))
            ProgressBar(1,1)
        }

        state.error?.let { errorResId ->
            Text(
                text = stringResource(id = errorResId),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
