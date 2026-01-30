package org.tues.tudy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.ui.theme.Dimens

@Composable
fun AddItemDialog(
    icons: List<String>,
    existingTitles: List<String>,
    onDismiss: () -> Unit,
    onSubmit: (String, String) -> Unit,
    title: String
) {
    var name by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedIcon by remember { mutableStateOf<String?>(null) }
    val activeColor =
        if (expanded) PrimaryColor1 else if (selectedIcon != null) BaseColor100 else BaseColor80

    var errorMessage by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Space200)
        ) {

            Surface(
                shape = RoundedCornerShape(Dimens.BorderRadius250),
                color = BaseColor0,
                tonalElevation = 6.dp,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.Space200),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Space200)
                ) {
                    CrossTitlePopUp(
                        onClick = { onDismiss() },
                        title = title
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(Dimens.Space125)) {
                        CustomTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                errorMessage = ""
                            },
                            label = "Title",
                            error = errorMessage,
                            textLength = 16
                        )

                        Column {
                            DropdownField(
                                selectedItem = selectedIcon,
                                expanded = expanded,
                                onToggleExpand = { expanded = !expanded },
                                onItemSelected = { selectedIcon = it as String },
                                activeColor = activeColor,
                                placeholder = "Icon",
                                icons = icons
                            )
                        }
                    }
                    val isButtonEnabled =
                        name.isNotEmpty() &&
                                selectedIcon != null &&
                                errorMessage.isEmpty()
                    CustomButton(
                        value = "Add",
                        enabled = isButtonEnabled,
                        onClick = {
                            var valid = true

                            if (existingTitles.any { it.equals(name, ignoreCase = true) }) {
                                errorMessage = "Title already exists"
                                valid = false
                            }

                            if (valid) {
                                onSubmit(name, selectedIcon!!)
                                name = ""
                                selectedIcon = null
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

