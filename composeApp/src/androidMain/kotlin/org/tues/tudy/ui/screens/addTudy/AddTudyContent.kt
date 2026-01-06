package org.tues.tudy.ui.screens.addTudy

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavController
import org.tues.tudy.data.model.CreateEventRequest
import org.tues.tudy.data.model.TypeSubject
import org.tues.tudy.ui.components.CustomButton
import org.tues.tudy.ui.components.CustomTextField
import org.tues.tudy.ui.components.DateTimePicker
import org.tues.tudy.ui.components.DropdownField
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.utils.BuildIsoDate
import org.tues.tudy.viewmodel.AddTudyViewModel
import org.tues.tudy.viewmodel.HomeViewModel
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.navigation.navigateToSuccessError
import java.util.Calendar


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

    var datePicked by remember { mutableStateOf(false) }
    var timePicked by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()

    var day by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    var month by remember { mutableStateOf(calendar.get(Calendar.MONTH) + 1) }
    var year by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }

    var hour by remember { mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }
    var minute by remember { mutableStateOf(calendar.get(Calendar.MINUTE)) }

    val focusManager = LocalFocusManager.current

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.success, uiState.error) {
        when{
        uiState.success == true -> {
            homeViewModel.loadData(userId)

            navController.navigateToSuccessError(
                title = "Success",
                subtitle = "Tudy Created!",
                description = "Your study session has been successfully added.",
                buttonText = "Go Home",
                buttonDestination = Routes.homeRoute(userId),
                arrow = false,
                success = true
            ) {
                popUpTo(Routes.homeRoute(userId)) { inclusive = true }
            }

            viewModel.resetState()
        }
            uiState.error != null -> {
                navController.navigateToSuccessError(
                    title = "Error",
                    subtitle = "Couldn't Create Tudy",
                    description = "Something went wrong while creating your study session. Please try again.",
                    buttonText = "Try Again",
                    buttonDestination = Routes.addTudyRoute(userId),
                    arrow = false,
                    success = false
                ) {
                    popUpTo(Routes.homeRoute(userId)) { inclusive = true }
                }

                viewModel.resetState()
            }
            }
        }


    LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Space100)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus(force = true)
                    })
                },
            verticalArrangement = Arrangement.Top
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
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
                        items = safeTypes,
                        placeholder = "Type"
                    )

                    Spacer(modifier = Modifier.height(Dimens.Space150))

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
                        items = safeSubjects,
                        placeholder = "Subject"
                    )

                    Spacer(modifier = Modifier.height(Dimens.Space150))

                    CustomTextField(
                        value = title,
                        onValueChange = {
                            title = it
                        },
                        label = "Title",
                        textLength = 25
                    )

                    Spacer(modifier = Modifier.height(Dimens.Space25))

                    CustomTextField(
                        value = description,
                        onValueChange = {
                            description = it
                        },
                        label = "Description (optional)",
                        textLength = 200
                    )

                    Spacer(modifier = Modifier.height(Dimens.Space100))

                    DateTimePicker(
                        day = day,
                        month = month,
                        year = year,
                        onDayChange = { day = it },
                        onMonthChange = { month = it },
                        onYearChange = { year = it },
                        hour = hour,
                        minute = minute,
                        onHourChange = { hour = it },
                        onMinuteChange = { minute = it },
                        onDatePicked = { datePicked = it },
                        onTimePicked = { timePicked = it }
                    )

                    val isButtonEnabled =
                        typeSelected != null &&
                                subjectSelected != null &&
                                title.isNotEmpty() &&
                                datePicked &&
                                timePicked

                    Spacer(modifier = Modifier.height(Dimens.Space200))

                    CustomButton(
                        value = "Add Tudy",
                        enabled = isButtonEnabled,
                        onClick = {
                            val dateIso = BuildIsoDate(year, month, day)
                            val startTimeIso = BuildIsoDate(year, month, day)
                            val endTimeIso = BuildIsoDate(year, month, day)

                            val request = CreateEventRequest(
                                title = title,
                                description = description.takeIf { it.isNotEmpty() },
                                type = "study",
                                category = typeSelected?.name,
                                subject = subjectSelected?.name,
                                date = dateIso,
                                startTime = startTimeIso,
                                endTime = endTimeIso,
                                pages = 0
                            )

                            viewModel.createTudy(
                                request,
                                userId = userId
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
