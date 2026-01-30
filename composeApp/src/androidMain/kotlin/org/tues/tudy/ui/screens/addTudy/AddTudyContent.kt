package org.tues.tudy.ui.screens.addTudy

import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.tudy.data.model.CreateEventRequest
import org.tues.tudy.data.model.TypeSubject
import org.tues.tudy.ui.components.ButtonSize
import org.tues.tudy.ui.components.CustomButton
import org.tues.tudy.ui.components.CustomTextField
import org.tues.tudy.ui.components.DateTimePicker
import org.tues.tudy.ui.components.DropdownField
import org.tues.tudy.ui.components.FullSelectDateTimeField
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.utils.BuildIsoDate
import org.tues.tudy.viewmodel.AddTudyViewModel
import org.tues.tudy.viewmodel.HomeViewModel
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.navigation.navigateToSuccessError
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.ErrorColor
import org.tues.tudy.utils.toLocalDateSafe
import org.tues.tudy.viewmodel.EventViewModel
import java.util.Calendar

const val MIN_EVENT_DURATION_MS = 15 * 60 * 1000L

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTudyContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AddTudyViewModel,
    homeViewModel: HomeViewModel,
    userId: String
) {
    var hasHandledSuccess by remember { mutableStateOf(false) }
    LaunchedEffect(userId) {
        homeViewModel.ensureLoaded(userId)
    }

    val eventViewModel: EventViewModel = viewModel()

    var eventType by remember { mutableStateOf("study") }

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
    var pagesText: String by remember { mutableStateOf("") }

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

    var endHour by remember { mutableStateOf(hour) }
    var endMinute by remember { mutableStateOf(minute) }
    var endTimePicked by remember { mutableStateOf(false) }
    val activeColorEndTime = if (endTimePicked) BaseColor100 else BaseColor80

    fun toMillis(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
        return Calendar.getInstance().apply {
            set(year, month - 1, day, hour, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }


    LaunchedEffect(uiState.success) {
        if (uiState.success == true && !hasHandledSuccess) {
            hasHandledSuccess = true

            homeViewModel.loadData(userId)

            navController.navigateToSuccessError(
                title = "Success",
                subtitle = "Event Created!",
                description = "Your event has been successfully added.",
                buttonText = "Go Home",
                buttonDestination = Routes.homeRoute(userId),
                arrow = false,
                success = true
            ) {
                popUpTo(Routes.homeRoute(userId)) { inclusive = true }
            }

            viewModel.resetState()
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

                Spacer(modifier = Modifier.height(Dimens.Space100))
                // ===== NEW: Event Type Toggle =====
                Text(
                    text = "Event Type",
                    style = AppTypography.Heading6,
                    color = BaseColor100,
                    modifier = Modifier.padding(start = Dimens.Space75)
                )

                Spacer(modifier = Modifier.height(Dimens.Space50))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Space75)
                ) {
                    CustomButton(
                        value = "Study",
                        enabled = true,
                        onClick = { eventType = "study" },
                        modifier = Modifier.weight(1f),
                        color = if (eventType == "study") PrimaryColor1 else BaseColor80,
                        size = ButtonSize.MEDIUM
                    )

                    CustomButton(
                        value = "Personal",
                        enabled = true,
                        onClick = { eventType = "personal" },
                        modifier = Modifier.weight(1f),
                        color = if (eventType == "personal") PrimaryColor1 else BaseColor80,
                        size = ButtonSize.MEDIUM
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.Space150))

                // ===== Show study-specific fields only if "study" is selected =====
                if (eventType == "study") {
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
                        value = pagesText,
                        onValueChange = {
                            pagesText = it
                        },
                        label = "Pages (optional)",
                        digitsOnly = true,
                        error = if (pagesText.isNotEmpty() && (pagesText.toIntOrNull() !in 0..999)) {
                            "Pages must be between 0 and 999"
                        } else null,
                    )

                    Spacer(modifier = Modifier.height(Dimens.Space100))
                }

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

                val startTimeMillis = Calendar.getInstance().apply {
                    set(year, month - 1, day, hour, minute)
                }.timeInMillis

                val endTimeMillis = Calendar.getInstance().apply {
                    set(year, month - 1, day, endHour, endMinute)
                }.timeInMillis

                fun addMinutes(hour: Int, minute: Int, minutesToAdd: Int): Pair<Int, Int> {
                    val calendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                        add(Calendar.MINUTE, minutesToAdd)
                    }
                    return calendar.get(Calendar.HOUR_OF_DAY) to calendar.get(Calendar.MINUTE)
                }

                DateTimePicker(
                    day = day, month = month, year = year,
                    onDayChange = { day = it },
                    onMonthChange = { month = it },
                    onYearChange = { year = it },
                    hour = hour, minute = minute,
                    onHourChange = { newHour ->
                        hour = newHour
                        viewModel.resetState()

                        if (!endTimePicked) {
                            val (newEndHour, newEndMinute) =
                                addMinutes(newHour, minute, 15)
                            endHour = newEndHour
                            endMinute = newEndMinute
                        }
                    },
                    onMinuteChange = { newMinute ->
                        minute = newMinute
                        viewModel.resetState()

                        if (!endTimePicked) {
                            val (newEndHour, newEndMinute) =
                                addMinutes(hour, newMinute, 15)
                            endHour = newEndHour
                            endMinute = newEndMinute
                        }
                    },
                    endHour = endHour, endMinute = endMinute,
                    onEndHourChange = { endHour = it; viewModel.resetState() },
                    onEndMinuteChange = { endMinute = it; viewModel.resetState() },
                    onDatePicked = { datePicked = it },
                    onTimePicked = { timePicked = it },
                    onEndTimePicked = { endTimePicked = it }
                )

                val isStartBeforeEnd = startTimeMillis < endTimeMillis
                val isDurationTooShort = endTimeMillis - startTimeMillis < MIN_EVENT_DURATION_MS


                val allEvents = eventViewModel.studySessions.collectAsState().value
                val hasOverlap = allEvents.any { event ->
                    val eventDate = event.date.toLocalDateSafe()
                    if (eventDate.year != year || eventDate.monthValue != month || eventDate.dayOfMonth != day) {
                        false
                    } else {
                        val eventStartParts =
                            event.startTime?.split(":")?.map { it.toInt() } ?: return@any false
                        val eventEndParts =
                            event.endTime?.split(":")?.map { it.toInt() } ?: return@any false

                        val eventStartMillis = toMillis(
                            eventDate.year,
                            eventDate.monthValue,
                            eventDate.dayOfMonth,
                            eventStartParts[0],
                            eventStartParts[1]
                        )

                        val eventEndMillis = toMillis(
                            eventDate.year,
                            eventDate.monthValue,
                            eventDate.dayOfMonth,
                            eventEndParts[0],
                            eventEndParts[1]
                        )

                        startTimeMillis < eventEndMillis && endTimeMillis > eventStartMillis
                    }
                }

                val isInvalidTime = startTimeMillis >= endTimeMillis

                val errorMessage = when {
                    isInvalidTime && timePicked && endTimePicked -> "Start time must be before end time"
                    isDurationTooShort && timePicked && endTimePicked -> "Event duration must be at least 15 minutes"
                    !uiState.error.isNullOrEmpty() -> uiState.error
                    else -> null
                }

                val isButtonEnabled = when {
                    title.isEmpty() -> false
                    !datePicked -> false
                    !timePicked -> false
                    !endTimePicked -> false
                    !isStartBeforeEnd -> false
                    isDurationTooShort -> false
                    errorMessage != null -> false

                    // Study-specific validations
                    eventType == "study" -> {
                        typeSelected != null && subjectSelected != null
                    }

                    // Personal events need nothing special
                    else -> true
                }

                Spacer(modifier = Modifier.height(Dimens.Space200))

                if (!errorMessage.isNullOrEmpty()) {
                    Text(
                        text = errorMessage,
                        color = ErrorColor,
                        style = AppTypography.Caption1,
                        modifier = Modifier.padding(bottom = Dimens.Space50, start = Dimens.Space75)
                    )
                }

                CustomButton(
                    value = "Add Event",
                    enabled = isButtonEnabled,
                    onClick = {
                        val dateIso = BuildIsoDate(year, month, day)
                        val startTimeIso = BuildIsoDate(year, month, day, hour, minute)
                        val endTimeIso = BuildIsoDate(year, month, day, endHour, endMinute)

                        val pagesInt =
                            if (eventType == "study") pagesText.toIntOrNull() ?: 0 else null

                        val request = CreateEventRequest(
                            title = title,
                            description = description.takeIf { it.isNotEmpty() },
                            type = eventType,  // ← NOW USES SELECTED TYPE!
                            category = if (eventType == "study") typeSelected?.name else null,
                            subject = if (eventType == "study") subjectSelected?.name else null,
                            date = dateIso,
                            startTime = startTimeIso,
                            endTime = endTimeIso,
                            pages = pagesInt
                        )

                        viewModel.createTudy(
                            request,
                            userId = userId
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(Dimens.Space125))
            }
        }
    }
}