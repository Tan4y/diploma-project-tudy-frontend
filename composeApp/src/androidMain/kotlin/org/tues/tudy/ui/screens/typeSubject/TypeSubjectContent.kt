package org.tues.tudy.ui.screens.typeSubject

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.tues.tudy.R
import org.tues.tudy.data.model.CalendarItem
import org.tues.tudy.ui.components.BasePopUp
import org.tues.tudy.ui.components.Sessions
import org.tues.tudy.ui.components.TudyCard
import org.tues.tudy.ui.components.TwoMidButtons
import org.tues.tudy.ui.components.TypeSubjectTitle
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.utils.formatDate
import org.tues.tudy.utils.isUpcoming
import org.tues.tudy.viewmodel.TypeSubjectViewModel
import org.tues.tudy.viewmodel.EventViewModel

@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TypeSubjectContent(
    modifier: Modifier,
    navController: NavController,
    viewModel: TypeSubjectViewModel,
    eventViewModel: EventViewModel,
    userId: String,
    title: String,
    clickedIsType: Boolean,
) {
    var showConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        eventViewModel.loadEvents(userId)
        viewModel.loadItems(userId, if (clickedIsType) "type" else "subject")
    }


    val events by eventViewModel.events.collectAsState()
    val isLoading by eventViewModel.isLoading.collectAsState()


    val studyEvents by remember(events) {
        derivedStateOf { events.filter { it.type == "study" && it.isUpcoming() } }
    }

    fun resolveSubject(item: CalendarItem): String = item.subject ?: item.subject ?: "Unknown"
    fun resolveCategory(item: CalendarItem): String = item.category ?: item.category ?: "Unknown"

    val studyItems by eventViewModel.studySessions.collectAsState()

    val studySessionToEventId by remember(events, studyItems) {
        derivedStateOf {
            studyItems.associate { session ->
                // Find the parent event by matching title
                val parentEvent = events.find { event ->
                    event.title == session.title
                }
                session.id to parentEvent?._id
            }
        }
    }


    val allItems: List<CalendarItem> by derivedStateOf {
        val eventsItems = events
            .filter { it.date?.isUpcoming() == true }
            .map { event ->
            CalendarItem(
                id = event._id,
                title = event.title,
                description = event.description,
                date = event.date,
                startTime = event.startTime,
                endTime = event.endTime,
                type = event.type,
                isStudySession = studyItems.any { it.id == event._id },
                category = event.category,
                subject = event.subject ?: "Unknown",
            )
        }

        val combined = studyItems + eventsItems
        combined
    }

    val groupedItems: List<Pair<String, List<CalendarItem>>> = if (clickedIsType) {
        allItems
            .filter { resolveCategory(it).equals(title, ignoreCase = true) }
            .groupBy { resolveSubject(it) }
            .map { it.key to it.value }
    } else {
        allItems
            .filter { resolveSubject(it).equals(title, ignoreCase = true) }
            .groupBy { resolveCategory(it) }
            .map { it.key to it.value }
    }

    val sessionsByTitle = remember(studyItems) {
        studyItems.groupBy { it.title }
    }

    val calendarItemsByTitle = remember(allItems) {
        allItems.associateBy { it.title }
    }

    val filteredGroups = remember(sessionsByTitle, title, clickedIsType) {
        sessionsByTitle
            .filterKeys { eventTitle ->
                // Get first session of this event to check subject/category
                val firstSession =
                    sessionsByTitle[eventTitle]?.firstOrNull() ?: return@filterKeys false

                if (clickedIsType) {
                    firstSession.category?.equals(title, ignoreCase = true) ?: false
                } else {
                    firstSession.subject?.equals(title, ignoreCase = true) ?: false
                }
            }
            .toList()
    }

    val isEmpty = filteredGroups.isEmpty()


// Create a 3-level hierarchy: Type/Subject → Other Dimension → Events → Sessions
    val hierarchicalGroups by remember(sessionsByTitle, title, clickedIsType, events) {
        derivedStateOf {
            val upcomingEvents = events.filter { it.date?.isUpcoming() == true }
            // All relevant events (filter by type/subject)
            val relevantEvents = upcomingEvents.filter { event ->
                if (clickedIsType) {
                    event.category?.equals(title, ignoreCase = true) ?: false
                } else {
                    event.subject?.equals(title, ignoreCase = true) ?: false
                }
            }

            // Build the map: Other dimension → Map<Event title, sessions>
            val groupedByOtherDimension =
                mutableMapOf<String, MutableMap<String, List<CalendarItem>>>()

            relevantEvents.forEach { event ->
                val sessions = sessionsByTitle[event.title] ?: emptyList()
                val otherDimension = if (clickedIsType) {
                    event.subject ?: "Unknown"
                } else {
                    event.category ?: "Unknown"
                }

                if (!groupedByOtherDimension.containsKey(otherDimension)) {
                    groupedByOtherDimension[otherDimension] = mutableMapOf()
                }

                groupedByOtherDimension[otherDimension]!![event.title] = sessions
            }

            groupedByOtherDimension
        }
    }


    Log.d(TAG, "Study events (upcoming only): ${studyEvents.size}")

    val selectedItem = viewModel.items.collectAsState().value.find {
        it.name.equals(title, ignoreCase = true) &&
                it.type == if (clickedIsType) "type" else "subject"
    }

    if (showConfirm) {
        BasePopUp(
            onDismiss = { showConfirm = false },
            onConfirm = {
                showConfirm = false
                selectedItem?.let {
                    viewModel.deleteTypeSubject(userId, it)
                    navController.navigate(Routes.homeRoute(userId)) {
                        popUpTo(
                            Routes.typeSubjectPageRoute(
                                userId,
                                title,
                                clickedIsType
                            )
                        ) { inclusive = true }
                    }
                }
                viewModel.loadEventsForUser(userId)
            },
            title = "Delete Tudy",
            description = "Are you sure you want to delete Tudy about $title"
        )
    }

    when {
        isLoading -> {
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
        }

        groupedItems.isEmpty() -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens.Space100),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillParentMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.weight(3f))

                        Text(
                            text = "You do not have $title Tudies",
                            style = AppTypography.Heading4,
                            color = BaseColor80,
                        )

                        Spacer(modifier = Modifier.weight(2f))

                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.giraffe_happy),
                                contentDescription = null,
                                modifier = Modifier.size(150.dp),
                                contentScale = ContentScale.Fit
                            )

                            TwoMidButtons(
                                text1 = "Delete",
                                text2 = "Add Tudy",
                                color1 = BaseColor80,
                                color2 = PrimaryColor1,
                                onClick1 = { showConfirm = true },
                                onClick2 = { navController.navigate(Routes.addTudyRoute(userId)) }
                            )

                            Spacer(modifier = Modifier.height(Dimens.Space125))
                        }
                    }
                }
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens.Space100),
            ) {
                // Level 2: Group by subject (if type clicked) or category (if subject clicked)
                hierarchicalGroups.forEach { (otherDimension, eventMap) ->
                    item {
                        Spacer(modifier = Modifier.height(Dimens.Space150))
                        TypeSubjectTitle(otherDimension)
                    }

                    // Level 3: Events within this dimension
                    eventMap.forEach { (eventTitle, sessions) ->
                        item {
                            val firstSession = sessions.firstOrNull()
                            val calendarItem = calendarItemsByTitle[eventTitle]

                            Spacer(modifier = Modifier.height(Dimens.Space150))

                            TudyCard(
                                title = eventTitle,
                                date = calendarItem?.date?.let { formatDate(it) } ?: "",
                                startTime = calendarItem?.startTime,
                                endTime = calendarItem?.endTime,
                                description = firstSession?.subject ?: firstSession?.category,
                                onClick = { navController.navigate(Routes.STUDY) },
                                onDelete = {
                                    val parentEvent = events.find { it.title == eventTitle }

                                    if (parentEvent != null) {
                                        Log.d("DeleteEvent", "Deleting event: ${parentEvent._id}")
                                        eventViewModel.deleteEvent(
                                            eventId = parentEvent._id,
                                            userId = userId,
                                            onSuccess = {
                                                eventViewModel.loadEvents(userId)
                                                Log.d("DeleteEvent", "Event deleted successfully")
                                            },
                                            onError = { error ->
                                                Log.e("DeleteEvent", "Failed to delete: $error")
                                            }
                                        )
                                    } else {
                                        Log.e(
                                            "DeleteEvent",
                                            "Could not find parent event with title: $eventTitle"
                                        )
                                    }
                                }
                            )
                        }

                        item {
                            if (sessions.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(Dimens.Space75))
                                Sessions(sessions)
                            } else {
                                //Spacer(modifier = Modifier.height(Dimens.Space150))
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(Dimens.Space125))
                    }
                }
            }
        }
    }
}
