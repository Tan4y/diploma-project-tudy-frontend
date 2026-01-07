package org.tues.tudy.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.ErrorColor

@Composable
fun ConfirmDeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Are you sure?",
                style = AppTypography.Heading5,
                color = BaseColor100
            )
        },
        text = {
            Text(
                "This tudy will be permanently deleted.",
                style = AppTypography.Caption1,
                color = BaseColor80
            )
        },
        confirmButton = {
            CustomButton(
                value = "Yes, delete",
                enabled = true,
                color = ErrorColor,
                onClick = onConfirm,
                big = false
            )
        },
        dismissButton = {
            CustomButton(
                value = "Cancel",
                enabled = true,
                onClick = onDismiss,
                big = false
            )
        }
    )
}
