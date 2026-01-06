package org.tues.tudy.ui.screens.typeSubject

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.tues.tudy.data.model.Event
import org.tues.tudy.ui.components.TypeCard
import org.tues.tudy.ui.components.TypeSubjectTitle
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
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
    val studyEvents = events.filter { it.type == "study" }
    Log.d(TAG, "=== TypeSubjectContent Debug ===")
    Log.d(TAG, "Title: '$title'")
    Log.d(TAG, "clickedIsType: $clickedIsType")
    Log.d(TAG, "Total events: ${events.size}")
    Log.d(TAG, "Study events: ${studyEvents.size}")
    Log.d(TAG, "All subjects: ${studyEvents.map { it.subject }}")
    Log.d(TAG, "All categories: ${studyEvents.map { it.category }}")
    // Group by subject or type
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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        groupedItems.forEach { (itemName, itemEvents) ->
            // Parent card (subject or type)
            item {
                TypeSubjectTitle(itemName)
            }

            itemEvents.forEach { event ->
                item {
                    Column(
                        modifier = Modifier
                            .padding(start = 32.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(BaseColor0)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = event.title,
                            style = AppTypography.Heading7
                        )
                        Text(
                            text = "${event.date} | ${event.startTime} - ${event.endTime}",
                            style = AppTypography.Body2
                        )
                    }
                }
            }
        }
    }
}