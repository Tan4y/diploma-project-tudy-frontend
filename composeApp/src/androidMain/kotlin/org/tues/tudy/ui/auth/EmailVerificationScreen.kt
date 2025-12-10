package org.tues.tudy.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.tudy.ui.components.LogoPlusTitle
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.viewmodel.EmailVerificationViewModel

@Composable
fun EmailVerificationScreen(
    navController: NavController,
    token: String?,
    viewModel: EmailVerificationViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    // Verify email when screen opens
    LaunchedEffect(token) {
        if (token != null) {
            viewModel.verifyEmail(token)
        } else {
            viewModel.setError("Invalid verification link")
        }
    }

    // Navigate to login when verified
    LaunchedEffect(state.success) {
        if (state.success) {
            kotlinx.coroutines.delay(2000) // Show success message for 2 seconds
            navController.navigate("login") {
                popUpTo(0) { inclusive = true } // Clear back stack
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Space100),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.Space200)
        ) {
            LogoPlusTitle("Email Verification")

            Spacer(modifier = Modifier.height(Dimens.Space200))

            when {
                state.loading -> {
                    CircularProgressIndicator()
                    Text(
                        text = "Verifying your email...",
                        style = AppTypography.Paragraph1,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                state.success -> {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(
                            id = android.R.drawable.ic_dialog_info
                        ),
                        contentDescription = "Success",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "Email verified successfully!",
                        style = AppTypography.Heading2,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Redirecting to login...",
                        style = AppTypography.Caption1,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                state.error != null -> {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(
                            id = android.R.drawable.ic_dialog_alert
                        ),
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "Verification Failed",
                        style = AppTypography.Heading2,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = state.error ?: "Unknown error",
                        style = AppTypography.Paragraph1,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(Dimens.Space200))

                    Button(
                        onClick = {
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    ) {
                        Text("Go to Login")
                    }
                }
            }
        }
    }
}