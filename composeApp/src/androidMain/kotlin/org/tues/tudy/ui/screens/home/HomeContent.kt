package org.tues.tudy.ui.screens.home

import android.R.attr.type
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.tudy.data.model.CalendarItem
import org.tues.tudy.data.model.TypeSubject
import org.tues.tudy.ui.components.AddItemDialog
import org.tues.tudy.ui.components.SubjectCard
import org.tues.tudy.ui.components.TitlePlus
import org.tues.tudy.ui.components.TypeCard
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius250
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.utils.formatDate
import org.tues.tudy.viewmodel.EventViewModel
import org.tues.tudy.viewmodel.HomeViewModel
import org.tues.tudy.viewmodel.TypeSubjectViewModel
import java.time.LocalDate
import org.tues.tudy.utils.toLocalDateSafe
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState


@SuppressLint("UnrememberedMutableState", "LocalContextResourcesRead", "DiscouragedApi")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel,
    eventViewModel: EventViewModel,
    userId: String,
) {
    val items by viewModel.items.collectAsState()
    var showAddTypeDialog by remember { mutableStateOf(false) }
    var showAddSubjectDialog by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.collectAsState()

    var reloadTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(userId, reloadTrigger) {
        viewModel.loadData(userId)
        eventViewModel.loadEvents(userId)
    }


    val types = items.filter { it.type == "type" }
    val subjects = items.filter { it.type == "subject" }

    val events by eventViewModel.events.collectAsState()

    val studyItems: List<CalendarItem> = viewModel.studySessions.collectAsState().value

    val today = LocalDate.now()

// Only keep study sessions in the future (today and later)
    val futureStudyItems =
        studyItems.filter { it.date.toLocalDateSafe().isAfter(today.minusDays(1)) }

// Only keep events in the future
    val futureEvents = events.filter { it.date.toLocalDateSafe().isAfter(today.minusDays(1)) }

// Group future study sessions by eventId
    val sessionsByEventId = futureStudyItems.groupBy { it.id.substringBefore("-") }

    val subjectCounts = mutableMapOf<String, Int>()
    val typeCounts = mutableMapOf<String, Int>()

    futureEvents.forEach { event ->
        val eventDate = event.date.toLocalDateSafe()

        if (eventDate.isBefore(today)) return@forEach

        val sessionsCount = sessionsByEventId[event._id]?.size ?: 0
        val total = 1 + sessionsCount

        val subject = event.subject
        if (!subject.isNullOrBlank()) {
            Log.d("HomeContent", "Event subject key: '$subject', total: $total")
            subjectCounts[subject] = (subjectCounts[subject] ?: 0) + total
        }

        val category = event.category
        if (!category.isNullOrBlank()) {
            typeCounts[category] = (typeCounts[category] ?: 0) + total
        }
    }

    val updatedSubjects = subjects.map { subject ->
        val key = subject.name.trim().lowercase()
        val count = subjectCounts.entries
            .firstOrNull { it.key.trim().lowercase() == key }
            ?.value ?: 0
        Log.d("HomeContent", "Mapping subject: '${subject.name}' -> count: $count")
        subject.copy(tudies = count)
    }


    val cleanedTypeCounts = mutableMapOf<String, Int>()
    futureEvents.forEach { event ->
        val category = event.category?.trim() ?: return@forEach
        val sessionsCount = sessionsByEventId[event._id]?.size ?: 0
        val total = 1 + sessionsCount
        cleanedTypeCounts[category] = (cleanedTypeCounts[category] ?: 0) + total
    }

    val updatedTypes = types.map { type ->
        val key = type.name.trim()
        type.copy(tudies = cleanedTypeCounts[key] ?: 0)
    }

    futureEvents.forEach { event ->
        val category = event.category?.trim() ?: return@forEach
        if (types.none { it.name.trim() == category }) {
            Log.d("HomeContent", "Category '${category}' not matching any type!")
        }
    }

    val activeSubjects by remember(updatedSubjects) {
        derivedStateOf {
            updatedSubjects.filter { it.tudies > 0 }
                .sortedWith(compareByDescending<TypeSubject> { it.tudies }.thenBy { it.name })
        }
    }

    val inactiveSubjects by remember(updatedSubjects) {
        derivedStateOf {
            updatedSubjects.filter { it.tudies == 0 }
                .sortedBy { it.name }
        }
    }

    LaunchedEffect(activeSubjects) {
        if (activeSubjects.isNotEmpty()) {
            eventViewModel.loadDatesForSubjects(userId, activeSubjects.map { it.name })
        }
    }

    futureEvents.forEach { event ->
        Log.d("HomeContent", "Event: ${event.subject}, Date: ${event.date}, sessions: ${sessionsByEventId[event._id]?.size}")
    }


    val activeSubjectsWithDates: Map<TypeSubject, Pair<List<LocalDate>, Int>> =
        activeSubjects.associateWith { subject ->
            // Only parent events
            val eventDates = futureEvents
                .filter { it.subject == subject.name || it.category == subject.name }
                .map { it.date.toLocalDateSafe() }
                .sorted()

            val displayedDates = eventDates.take(3)
            val totalEvents = eventDates.size

            displayedDates to totalEvents
        }


    val allIcons = viewModel.getTypeIcons() + viewModel.getSubjectIcons()

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryColor1)
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {

            // Types
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.Space50)
                ) {
                    TitlePlus("Type", onAddClick = { showAddTypeDialog = true })

                    if (showAddTypeDialog) {
                        AddItemDialog(
                            title = "Add Type",
                            onDismiss = { showAddTypeDialog = false },
                            onSubmit = { name, icon ->
                                viewModel.addTypeSubject(userId, name, icon, "type")
                                showAddTypeDialog = false
                                viewModel.loadData(userId)
                                eventViewModel.loadEvents(userId)
                            },
                            icons = allIcons,
                            existingTitles = types.map { it.name }
                        )
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Space125),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens.Space100),
                    ) {
                        items(updatedTypes) { type ->
                            val context = LocalContext.current
                            val iconResId = remember(type.iconName) {
                                context.resources.getIdentifier(
                                    type.iconName,
                                    "drawable",
                                    context.packageName
                                )
                            }
                            TypeCard(
                                navController = navController,
                                value = type.name,
                                numberOfTudies = type.tudies,
                                icon = painterResource(id = iconResId),
                                onClick = {
                                    navController.navigate(
                                        Routes.typeSubjectPageRoute(userId, type.name, true)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(Dimens.Space150)) }

            // Subjects
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.Space50)
                ) {
                    TitlePlus("Subject", onAddClick = { showAddSubjectDialog = true })

                    if (showAddSubjectDialog) {
                        AddItemDialog(
                            title = "Add Subject",
                            onDismiss = { showAddSubjectDialog = false },
                            onSubmit = { name, icon ->
                                viewModel.addTypeSubject(userId, name, icon, "subject")
                                showAddSubjectDialog = false
                                viewModel.loadData(userId)
                                eventViewModel.loadEvents(userId)
                            },
                            icons = allIcons,
                            existingTitles = types.map { it.name }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)
                            .padding(horizontal = Dimens.Space100),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Space125)
                    ) {
                        // Dates Column
                        if (activeSubjects.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .width(80.dp)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(BorderRadius250))
                                    .background(PrimaryColor1)
                                    .padding(Dimens.Space125),
                                verticalArrangement = Arrangement.spacedBy(Dimens.Space125),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                activeSubjects.forEachIndexed { index, subject ->
                                    val (dates, totalEvents) = activeSubjectsWithDates[subject]!!
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        dates.forEach { date ->
                                            Text(
                                                text = formatDate("${date}T00:00:00Z"),
                                                style = AppTypography.Caption1,
                                                color = BaseColor0
                                            )
                                        }
                                        if (totalEvents > 3) {
                                            Text(
                                                text = "...",
                                                style = AppTypography.Caption1,
                                                color = BaseColor0
                                            )
                                        }
                                    }
                                    if (index < activeSubjects.lastIndex) {
                                        HorizontalDivider(
                                            thickness = 1.dp,
                                            color = BaseColor0
                                        )
                                    }
                                }
                            }
                        }

                        // SubjectCard Column
                        Column(
                            modifier = Modifier.weight(2f),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            activeSubjects.forEach { subject ->
                                val context = LocalContext.current
                                val iconResId = remember(subject.iconName) {
                                    context.resources.getIdentifier(
                                        subject.iconName,
                                        "drawable",
                                        context.packageName
                                    )
                                }
                                SubjectCard(
                                    navController = navController,
                                    value = subject.name,
                                    numberOfTudies = subject.tudies,
                                    icon = painterResource(iconResId),
                                    onClick = {
                                        navController.navigate(
                                            Routes.typeSubjectPageRoute(userId, subject.name, false)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

            item { if (activeSubjects.isNotEmpty()) Spacer(modifier = Modifier.height(Dimens.Space125)) }

            // Inactive Subjects
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = Dimens.Space100)
                        .padding(bottom = Dimens.Space125),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Space125)
                ) {
                    inactiveSubjects.forEach { subject ->
                        val context = LocalContext.current
                        val iconResId = remember(subject.iconName) {
                            context.resources.getIdentifier(
                                subject.iconName,
                                "drawable",
                                context.packageName
                            )
                        }
                        SubjectCard(
                            navController = navController,
                            value = subject.name,
                            numberOfTudies = subject.tudies,
                            icon = painterResource(iconResId),
                            onClick = {
                                navController.navigate(
                                    Routes.typeSubjectPageRoute(userId, subject.name, false)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
