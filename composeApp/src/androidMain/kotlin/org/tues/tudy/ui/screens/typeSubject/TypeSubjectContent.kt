package org.tues.tudy.ui.screens.typeSubject

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.tues.tudy.R
import org.tues.tudy.data.model.Event
import org.tues.tudy.ui.components.CustomButton
import org.tues.tudy.ui.components.TudyCard
import org.tues.tudy.ui.components.TypeCard
import org.tues.tudy.ui.components.TypeSubjectTitle
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.utils.isUpcoming
import org.tues.tudy.viewmodel.TypeSubjectViewModel

@Composable
fun TypeSubjectContent(
    modifier: Modifier,
    navController: NavController,
    viewModel: TypeSubjectViewModel,
    userId: String,
    title: String,
    clickedIsType: Boolean,
    events: List<Event>
) {
    val studyEvents = events.filter {
        it.type == "study" && it.isUpcoming()
    }

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


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.Space100),
        verticalArrangement = Arrangement.spacedBy(Dimens.Space150)
    ) {
        item {
            if (groupedItems.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillParentMaxHeight()
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.weight(3f))

                            Text(
                                text = "You do not have $title tudies",
                                style = AppTypography.Heading4,
                                color = BaseColor80,
                            )

                        Spacer(modifier = Modifier.weight(2f))

                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = Dimens.Space150)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.giraffe_happy),
                                contentDescription = "Happy Giraffe",
                                modifier = Modifier.size(150.dp),
                                contentScale = ContentScale.Fit
                            )
                            CustomButton(
                                value = "Create Tudy",
                                enabled = true,
                                onClick = { navController.navigate(Routes.addTudyRoute(userId)) }
                            )
                        }
                    }
                }
            }
        }
        groupedItems.forEach { (itemName, itemEvents) ->
            // Parent card (subject or type)
            item {
                TypeSubjectTitle(itemName)
            }

            itemEvents.forEach { event ->
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(Dimens.Space150),
                        modifier = Modifier
                            //.padding(start = 32.dp)
                            .fillMaxWidth()
                        //.clip(RoundedCornerShape(8.dp))
                        //.background(BaseColor0)
                        //.padding(Dimens.Space125)
                    ) {
                        TudyCard(
                            title = event.title,
                            date = event.date,
                            description = event.description,
                            onClick = {
                                navController.navigate(
                                    Routes.STUDY
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}