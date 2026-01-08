package org.tues.tudy.ui.screens.typeSubject

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import org.tues.tudy.data.model.Event
import org.tues.tudy.ui.components.BasePopUp
import org.tues.tudy.ui.components.CustomButton
import org.tues.tudy.ui.components.TudyCard
import org.tues.tudy.ui.components.TwoMidButtons
import org.tues.tudy.ui.components.TypeSubjectTitle
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.ErrorColor
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.utils.isUpcoming
import org.tues.tudy.viewmodel.TypeSubjectViewModel
import org.tues.tudy.viewmodel.EventViewModel

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
    val events by eventViewModel.events.collectAsState()
    val studyEvents = events.filter { it.type == "study" && it.isUpcoming() }

    val isLoading by eventViewModel.isLoading.collectAsState()



    Log.d(TAG, "Study events (upcoming only): ${studyEvents.size}")

    val groupedItems: List<Pair<String, List<Event>>> = if (clickedIsType) {
        studyEvents
            .filter { it.category?.trim().equals(title.trim(), ignoreCase = true) }
            .groupBy { it.subject ?: "Unknown" }
            .map { it.key to it.value }
    } else {
        studyEvents
            .filter { it.subject?.trim().equals(title.trim(), ignoreCase = true) }
            .groupBy { it.category ?: "Unknown" }
            .map { it.key to it.value }
    }


    LaunchedEffect(Unit) {
        eventViewModel.loadEvents(userId)
        viewModel.loadItems(userId, if (clickedIsType) "type" else "subject")
    }

    var showConfirm by remember { mutableStateOf(false) }
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
                        popUpTo(Routes.typeSubjectPageRoute(userId, title, clickedIsType)) { inclusive = true }
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
            // LoadingView()
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
                verticalArrangement = Arrangement.spacedBy(Dimens.Space150)
            ) {
                groupedItems.forEach { (itemName, itemEvents) ->

                    item {
                        TypeSubjectTitle(itemName)
                    }

                    items(itemEvents.size) { index ->
                        val event = itemEvents[index]

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(Dimens.Space150),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TudyCard(
                                title = event.title,
                                date = event.date,
                                description = event.description,
                                onClick = {
                                    navController.navigate(Routes.STUDY)
                                },
                                onDelete = {
                                    eventViewModel.deleteEvent(
                                        eventId = event._id,
                                        onSuccess = { eventViewModel.loadEvents(userId) },
                                        onError = { error ->
                                            Log.e("DeleteEvent", error)
                                        },
                                        userId = userId
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}