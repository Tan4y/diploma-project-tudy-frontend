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


@SuppressLint("UnrememberedMutableState")
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

    LaunchedEffect(userId) {
        viewModel.loadData(userId)
        eventViewModel.loadEvents(userId)
    }

    val types = items.filter { it.type == "type" }
    val subjects = items.filter { it.type == "subject" }

    val errorMessage by viewModel.errorMessage.collectAsState()

    val events by eventViewModel.events.collectAsState()
    val subjectDates by eventViewModel.subjectDates.collectAsState()

    fun resolveSubject(subjectIdOrName: String?): String {
        if (subjectIdOrName.isNullOrBlank()) return "Unknown"
        val fromId = subjects.find { it._id == subjectIdOrName }?.name
        return fromId ?: subjectIdOrName
    }


    val studyItems: List<CalendarItem> = viewModel.studySessions.collectAsState().value

    val allItems: List<CalendarItem> by derivedStateOf {
        val eventsItems = events
            .filter { it.type != "study" }
            .map { event ->
                CalendarItem(
                    id = event._id,
                    title = event.title,
                    description = event.description,
                    date = event.date,
                    startTime = event.startTime,
                    endTime = event.endTime,
                    type = event.type,
                    isStudySession = false,
                    subject = event.subject ?: "Unknown",
                    category = event.category
                )
            }

        val combined = studyItems + eventsItems
        combined
    }


    //val uniqueEvents = events.distinctBy { it._id }
    val validSubjects = subjects.map { it.name }.toSet()

//    fun normalizeSubject(raw: String?): String? {
//        val s = raw?.trim()
//        return if (s != null && s in validSubjects) s else null
//    }

    // Combine events and study sessions

// Prevent duplicate event counting
    val countedEventTitles = mutableSetOf<String>()

// Group sessions by title once (fast & safe)
    val sessionsByTitle = studyItems.groupBy { it.title }

    //val countedEventIds = mutableSetOf<String>()

    //Log.d("TUDY_COUNT", "===== COUNTING SUBJECT EVENTS =====")

//    events.forEach { event ->
//        val subject = event.subject ?: return@forEach
//
//        // 🚫 prevent duplicate events
//        if (!countedEventIds.add(event._id)) return@forEach
//
//        val sessionCount =
//            if ((event.totalPages ?: 0) > 0)
//                studyItems.count { it.subject == subject }
//            else 0
//
//        val total = 1 + sessionCount
//        subjectCounts[subject] = (subjectCounts[subject] ?: 0) + total
//
//        Log.d(
//            "TUDY_COUNT",
//            "Event '${event.title}' [$subject]: 1 event + $sessionCount sessions = $total"
//        )
//    }



    // 1️⃣ Map eventId -> subject
    val eventSubjectMap = events.associate { it._id to it.subject }


    val eventById = events
        .filter { it.type == "study" }
        .associateBy { it._id }

// 2️⃣ Count events per subject
    //val subjectCounts = mutableMapOf<String, Int>()

    val subjectCounts = mutableMapOf<String, Int>()
    val typeCounts = mutableMapOf<String, Int>()

    val sessionsByEventId = studyItems.groupBy { it.id.substringBefore("-") }

    eventById.forEach { (eventId, event) ->
        val subject = event.subject ?: return@forEach

        // Count all sessions related to this event
        val sessionsCount = sessionsByEventId[eventId]?.size ?: 0

        // total = 1 event + all sessions
        val total = 1 + sessionsCount
        subjectCounts[subject] = (subjectCounts[subject] ?: 0) + total

        Log.d(
            "TUDY_COUNT",
            "Event '${event.title}' [$subject]: 1 event + $sessionsCount sessions = $total"
        )
    }



    Log.d("TUDY_COUNT", "FINAL SUBJECT COUNTS: $subjectCounts")


    Log.d("TUDY_COUNT", "FINAL SUBJECT COUNTS: $subjectCounts")

    eventById.forEach { (eventId, event) ->
        val category = event.category ?: return@forEach

        // Only count sessions if totalPages > 0
        val sessionsCount = if ((event.totalPages ?: 0) > 0) {
            sessionsByEventId[eventId]?.size ?: 0
        } else {
            0
        }

        val total = 1 + sessionsCount

        typeCounts[category] = (typeCounts[category] ?: 0) + total

        Log.d(
            "TUDY_COUNT",
            "Event '${event.title}' (category=$category): 1 event + $sessionsCount sessions = $total"
        )
    }



    val updatedSubjects = subjects.map { subject ->
        subject.copy(
            tudies = subjectCounts[subject.name] ?: 0
        )
    }

    val updatedTypes = types.map { type ->
        type.copy(
            tudies = typeCounts[type.name] ?: 0
        )
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

    val activeTypes by remember(updatedTypes) {
        derivedStateOf {
            updatedTypes.filter { it.tudies > 0 }
                .sortedWith(compareByDescending<TypeSubject> { it.tudies }.thenBy { it.name })
        }
    }

    val inactiveTypes by remember(updatedTypes) {
        derivedStateOf {
            updatedTypes.filter { it.tudies == 0 }
                .sortedBy { it.name }
        }
    }

    Log.d("HomeContent", "===== LOGGING updatedSubjects =====")
    updatedSubjects.forEach { sub ->
        Log.d("HomeContent", "Subject '${sub.name}' has tudies=${sub.tudies}")
    }

    Log.d("HomeContent", "Active subjects: ${activeSubjects.map { it.name }}")
    Log.d("HomeContent", "Inactive subjects: ${inactiveSubjects.map { it.name }}")


    LaunchedEffect(activeSubjects) {
        if (activeSubjects.isNotEmpty()) {
            eventViewModel.loadDatesForSubjects(userId, activeSubjects.map { it.name })
        }
    }


    val activeSubjectsWithDates: Map<TypeSubject, Pair<List<LocalDate>, Int>> =
        activeSubjects.associateWith { subject ->
            val eventDates =
                events.filter { it.subject == subject.name || it.category == subject.name }
                    .map { it.date.toLocalDateSafe() }

            val sessionDates = studyItems.filter { it.subject == subject.name }
                .map { it.date.toLocalDateSafe() }

            val allDates = (eventDates + sessionDates)
                .filter { it.isAfter(LocalDate.now().minusDays(1)) } // today and future
                .sorted()

            val totalEvents = allDates.size
            val displayedDates = allDates.take(3)

            displayedDates to totalEvents
        }


    val allIcons = viewModel.getTypeIcons() + viewModel.getSubjectIcons()

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Loading...",
                style = AppTypography.Heading4,
                color = BaseColor80
            )
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
                            TypeCard(
                                navController = navController,
                                value = type.name,
                                numberOfTudies = type.tudies,
                                icon = painterResource(id = type.iconRes),
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
                                SubjectCard(
                                    navController = navController,
                                    value = subject.name,
                                    numberOfTudies = subject.tudies,
                                    icon = painterResource(subject.iconRes),
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
                        SubjectCard(
                            navController = navController,
                            value = subject.name,
                            numberOfTudies = subject.tudies,
                            icon = painterResource(subject.iconRes),
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
