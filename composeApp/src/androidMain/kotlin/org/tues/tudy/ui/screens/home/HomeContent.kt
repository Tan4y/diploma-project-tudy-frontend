package org.tues.tudy.ui.screens.home

import android.R.attr.type
import android.net.Uri
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
import org.tues.tudy.data.model.TypeSubject
import org.tues.tudy.ui.components.AddItemDialog
import org.tues.tudy.ui.components.SubjectCard
import org.tues.tudy.ui.components.TitlePlus
import org.tues.tudy.ui.components.TypeCard
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius250
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.utils.formatDate
import org.tues.tudy.viewmodel.EventViewModel
import org.tues.tudy.viewmodel.HomeViewModel
import org.tues.tudy.viewmodel.TypeSubjectViewModel

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel,
    eventViewModel: EventViewModel,
    userId: String,
    items: List<TypeSubject>
) {
    var showAddTypeDialog by remember { mutableStateOf(false) }
    var showAddSubjectDialog by remember { mutableStateOf(false) }

    val types = items.filter { it.type == "type" }
    val subjects = items.filter { it.type == "subject" }

    val errorMessage by viewModel.errorMessage.collectAsState()

    val events by eventViewModel.events.collectAsState()
    val subjectDates by eventViewModel.subjectDates.collectAsState()

    LaunchedEffect(Unit) {
        eventViewModel.loadEvents()
    }

    val updatedSubjects by remember(items, events) {
        derivedStateOf {
            subjects.map { subject ->
                val newCount = events.count { it.subject == subject.name && it.type == "study" }
                subject.copy(tudies = newCount)
            }
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
        eventViewModel.loadDatesForSubjects(activeSubjects.map { it.name })
        viewModel.loadData(userId)
    }

    val activeSubjectsWithDates = activeSubjects.associateWith { subject ->
        val dates = subjectDates[subject.name] ?: emptyList()
        dates.take(3) to dates.size
    }


    val allIcons = viewModel.getTypeIcons() + viewModel.getSubjectIcons()

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
                            eventViewModel.loadEvents()
                        },
                        icons = allIcons,
                        existingTitles = types.map { it.name }
                    )
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Space125),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.Space100),
                ) {
                    items(types) { type ->
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
                            eventViewModel.loadEvents()
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
                                            text = formatDate(date),
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
