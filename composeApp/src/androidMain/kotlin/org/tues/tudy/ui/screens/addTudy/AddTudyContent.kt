package org.tues.tudy.ui.screens.addTudy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.tues.tudy.data.model.TypeSubject
import org.tues.tudy.ui.components.CustomButton
import org.tues.tudy.ui.components.CustomTextField
import org.tues.tudy.ui.components.DropdownField
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.viewmodel.AddTudyViewModel
import org.tues.tudy.viewmodel.HomeViewModel
@Composable
fun AddTudyContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AddTudyViewModel,
    homeViewModel: HomeViewModel,
    userId: String
) {
    LaunchedEffect(userId) {
        homeViewModel.ensureLoaded(userId)
    }
    var typeExpanded by remember { mutableStateOf(false) }
    var typeSelected by remember { mutableStateOf<TypeSubject?>(null) }

    var subjectExpanded by remember { mutableStateOf(false) }
    var subjectSelected by remember { mutableStateOf<TypeSubject?>(null) }


    val activeTypeColor =
        if (typeExpanded) PrimaryColor1 else if (typeSelected != null) BaseColor100 else BaseColor80
    val activeSubjectColor =
        if (subjectExpanded) PrimaryColor1 else if (subjectSelected != null) BaseColor100 else BaseColor80

    val allItems by homeViewModel.items.collectAsState()
    val types = allItems.filter { it.type == "type" }
    val safeTypes = types.ifEmpty { emptyList() }

    val subjects = allItems.filter { it.type == "subject" }
    val safeSubjects = subjects.ifEmpty { emptyList() }

    var title by remember { mutableStateOf("") }

    var description by remember { mutableStateOf("") }


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.Space100),
        verticalArrangement = Arrangement.Top
    ) {
        item {
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DropdownField(
                    selectedItem = typeSelected?.name,
                    expanded = typeExpanded,
                    onToggleExpand = { typeExpanded = !typeExpanded },
                    onItemSelected = { selected ->
                        when (selected) {
                            is TypeSubject -> {
                                typeSelected = selected
                            }

                            is String -> {
                                typeSelected = types.find { it.name == selected }
                            }
                        }
                        typeExpanded = !typeExpanded
                    },
                    activeColor = activeTypeColor,
                    items = safeTypes
                )

                Spacer(modifier = Modifier.height(Dimens.Space125))

                DropdownField(
                    selectedItem = subjectSelected?.name,
                    expanded = subjectExpanded,
                    onToggleExpand = { subjectExpanded = !subjectExpanded },
                    onItemSelected = { selected ->
                        when (selected) {
                            is TypeSubject -> {
                                subjectSelected = selected
                            }

                            is String -> {
                                subjectSelected = subjects.find { it.name == selected }
                            }
                        }
                        subjectExpanded = !subjectExpanded
                    },
                    activeColor = activeSubjectColor,
                    items = safeSubjects
                )

                Spacer(modifier = Modifier.height(Dimens.Space125))

                CustomTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = "Title",
                    textLength = 16
                )

                Spacer(modifier = Modifier.height(Dimens.Space125))

                CustomTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    label = "Title",
                    textLength = 16
                )

                Spacer(modifier = Modifier.height(Dimens.Space125))


                val isButtonEnabled =
                    typeSelected != null &&
                            subjectSelected != null &&
                            title.isNotEmpty() &&
                            description.isNotEmpty()

                CustomButton(
                    value = "Add Tudy",
                    enabled = isButtonEnabled,
                    onClick = {
//                            onSubmit(name, selectedIcon!!)
//                            name = ""
//                            selectedIcon = null
//                            onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}