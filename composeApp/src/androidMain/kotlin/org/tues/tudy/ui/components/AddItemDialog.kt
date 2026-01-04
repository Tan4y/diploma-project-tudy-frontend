package org.tues.tudy.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.R
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius200

@Composable
fun AddItemDialog(
    icons: List<Int>,
    existingTitles: List<String>,
    onDismiss: () -> Unit,
    onSubmit: (String, Int) -> Unit,
    title: String
) {
    var name by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedIcon by remember { mutableStateOf<Int?>(null) }
    val activeColor =
        if (expanded) PrimaryColor1 else if (selectedIcon != null) BaseColor100 else BaseColor80

    val focusManager = LocalFocusManager.current
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(Dimens.Space50),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                painter = painterResource(android.R.drawable.ic_menu_close_clear_cancel),
                                contentDescription = "Close",
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.TopEnd)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }) {
                                        onDismiss()
                                    }
                            )
                        }

                        Text(
                            text = title,
                            style = AppTypography.Heading4,
                            color = PrimaryColor1
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(Dimens.Space125)) {
                        CustomTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                errorMessage = ""
                            },
                            label = "Title",
                            error = errorMessage
                        )

                        Column {

                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.spacedBy(Dimens.Space50)
                            ) {
                                if (selectedIcon != null) {
                                    Text(
                                        text = "Icon",
                                        style = AppTypography.Caption1,
                                        color = activeColor,
                                        modifier = Modifier.padding(horizontal = Dimens.Space75)
                                    )
                                }
                                DropdownField(
                                    selectedItem = selectedIcon,
                                    expanded = expanded,
                                    onToggleExpand = { expanded = !expanded },
                                    onItemSelected = { selectedIcon = it as Int },
                                    activeColor = activeColor,
                                    placeholder = "Icon",
                                    icons = icons
                                )
//                                Row(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .border(
//                                            width = 1.dp,
//                                            color = activeColor,
//                                            shape = RoundedCornerShape(BorderRadius200)
//                                        )
//                                        .clip(RoundedCornerShape(BorderRadius200))
//                                        .clickable(
//                                            indication = null,
//                                            interactionSource = remember { MutableInteractionSource() }) {
//                                            expanded = !expanded
//                                        }
//                                        .padding(horizontal = 12.dp, vertical = 16.dp),
//                                    verticalAlignment = Alignment.CenterVertically,
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    if (selectedIcon != null) {
//                                        Icon(
//                                            painter = painterResource(selectedIcon!!),
//                                            contentDescription = null,
//                                            tint = BaseColor100,
//                                            modifier = Modifier.size(36.dp)
//                                        )
//                                    } else {
//                                        Box(
//                                            modifier = Modifier.height(36.dp),
//                                            contentAlignment = Alignment.Center
//                                        ) {
//                                            Text(
//                                                text = "Icon",
//                                                style = AppTypography.Caption1,
//                                                color = BaseColor80,
//                                            )
//                                        }
//                                    }
//                                    Icon(
//                                        painter = painterResource(R.drawable.arrow_down),
//                                        contentDescription = "Dropdown arrow",
//                                        tint = activeColor,
//                                        modifier = Modifier
//                                            .rotate(if (expanded) 180f else 0f)
//                                    )
//
//                                }
//                            }

//                            if (expanded) {
//                                LazyVerticalGrid(
//                                    columns = GridCells.Fixed(4),
//                                    modifier = Modifier
//                                        .padding(top = Dimens.Space75)
//                                        .border(
//                                            width = 1.dp,
//                                            color = PrimaryColor1,
//                                            shape = RoundedCornerShape(BorderRadius200)
//                                        )
//                                        .clip(RoundedCornerShape(BorderRadius200))
//                                        .heightIn(max = 300.dp)
//                                ) {
//                                    items(icons) { icon ->
//                                        Box(
//                                            modifier = Modifier
//                                                .padding(Dimens.Space50)
//                                                .clickable(
//                                                    indication = null,
//                                                    interactionSource = remember { MutableInteractionSource() }) {
//                                                    selectedIcon = icon
//                                                    expanded = false
//                                                },
//                                            contentAlignment = Alignment.Center
//                                        ) {
//                                            Box(contentAlignment = Alignment.Center) {
//                                                Icon(
//                                                    painter = painterResource(icon),
//                                                    contentDescription = null,
//                                                    modifier = Modifier.size(36.dp)
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
                            }
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

