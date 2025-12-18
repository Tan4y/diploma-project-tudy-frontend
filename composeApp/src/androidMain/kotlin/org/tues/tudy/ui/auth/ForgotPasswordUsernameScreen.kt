package org.tues.tudy.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.tudy.ui.components.CustomButton
import org.tues.tudy.ui.components.CustomTextField
import org.tues.tudy.ui.components.LogoPlusTitle
import org.tues.tudy.ui.components.ProgressBar
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.viewmodel.ForgotPasswordViewModel
import androidx.compose.ui.res.stringResource
import org.tues.tudy.ui.navigation.navigateToSuccessError
import org.tues.tudy.ui.theme.Dimens

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
        modifier = Modifier.fillMaxSize().padding(Dimens.Space100),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Safe space for the top bar
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(Dimens.Space400))
        }

        Column(modifier = Modifier.weight(1f,)) {
            LogoPlusTitle("Forgot Password")
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            CustomTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = ""
                },
                label = "Username",
                error = usernameError.ifEmpty { null }
            )
        }

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Bottom
        ) {
            ProgressBar(2,1)

            Spacer(modifier = Modifier.height(Dimens.Space125))

            CustomButton(
                value = "Send Reset Link",
                enabled = username.isNotEmpty() && usernameError.isEmpty(),
                onClick = {
                    if (username.isEmpty()) usernameError = "Username is required"
                    else viewModel.forgotPassword(username)
                }
            )

            Spacer(modifier = Modifier.height(Dimens.Space225))
        }

        state.error?.let { errorResId ->
            Text(
                text = stringResource(id = errorResId),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
