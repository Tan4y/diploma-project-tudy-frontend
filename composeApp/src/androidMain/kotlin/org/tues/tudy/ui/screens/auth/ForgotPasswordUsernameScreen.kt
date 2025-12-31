package org.tues.tudy.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.tudy.ui.components.CustomButton
import org.tues.tudy.ui.components.CustomTextField
import org.tues.tudy.ui.components.LogoPlusTitle
import org.tues.tudy.ui.components.ProgressBar
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.viewmodel.ForgotPasswordViewModel
import androidx.compose.ui.res.stringResource
import org.tues.tudy.ui.components.TopBar
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
    var showSuccessDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.emailSent) {
        if (state.emailSent) {
            showSuccessDialog = true
        }
    }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Space100)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Column(modifier = Modifier.weight(1f)) {
            TopBar(
                modifier = Modifier,
                navController = navController,
                primary = false,
                heading = ""
            )
        }
        Column(modifier = Modifier.weight(1f)) {
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
            ProgressBar(2, 1)

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

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Email sent") },
                text = { Text("Check your inbox for the password reset link.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.FORGOT_PASSWORD) { inclusive = true }
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
