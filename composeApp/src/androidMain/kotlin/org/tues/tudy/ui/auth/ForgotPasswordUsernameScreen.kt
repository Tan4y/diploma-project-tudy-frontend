package org.tues.tudy.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.tues.tudy.ui.components.CustomButton
import org.tues.tudy.ui.components.CustomTextField
import org.tues.tudy.ui.components.LogoPlusTitle
import org.tues.tudy.ui.components.ProgressBar
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.viewmodel.ForgotPasswordViewModel
import androidx.compose.ui.res.stringResource
import org.tues.tudy.ui.navigation.navigateToSuccessError

@Composable
fun ForgotPasswordUsernameScreen(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var username by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.emailSent) {
        if (state.emailSent) {
            navController.navigateToSuccessError(
                title = "Email Sent",
                subtitle = "Check Your Inbox",
                description = "A password reset link has been sent to your email.",
                buttonText = "Continue",
                buttonDestination = Routes.LOGIN,
                arrow = false,
                success = true
            ) {
                popUpTo(Routes.FORGOT_PASSWORD) { inclusive = true }
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoPlusTitle("Forgot Password")

        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(
            value = username,
            onValueChange = {
                username = it
                usernameError = ""
            },
            label = "Username",
            error = usernameError.ifEmpty { null }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomButton(
            value = "Send Reset Link",
            enabled = username.isNotEmpty() && usernameError.isEmpty(),
            onClick = {
                if (username.isEmpty()) usernameError = "Username is required"
                else viewModel.forgotPassword(username)
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
        // Navigate on email sent
        if (state.emailSent) {
            Text(
                text = "Check your email for a password reset link.",
                color = MaterialTheme.colorScheme.primary
            )
        }

    }
}
