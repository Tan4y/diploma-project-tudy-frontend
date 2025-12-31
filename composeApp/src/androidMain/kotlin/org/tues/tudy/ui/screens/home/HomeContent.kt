package org.tues.tudy.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.tues.tudy.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.tudy.ui.components.SubjectCard
import org.tues.tudy.ui.components.TypeCard
import org.tues.tudy.viewmodel.HomeViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import org.tues.tudy.data.model.Subject
import org.tues.tudy.ui.components.TitlePlus
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius250
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.ui.theme.shadow1
import org.tues.tudy.utils.formatDateToDayMonth

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val types = viewModel.types.collectAsState(emptyList()).value
    val subjects = viewModel.subjects.collectAsState(emptyList()).value

    val activeSubjects = subjects
        .filter { it.tudies > 0 }
        .sortedWith(compareByDescending<Subject> { it.tudies }.thenBy { it.name })

    val inactiveSubjects = subjects
        .filter { it.tudies == 0 }
        .sortedBy { it.name }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimens.Space400, bottom = Dimens.Space450),
        verticalArrangement = Arrangement.Center
    ) {

        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.Space50)
            ) {
                TitlePlus("Type", navController = navController)

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Space125),
                    modifier = Modifier.padding(horizontal = Dimens.Space100)
                ) {
                    items(types) { type ->
                        TypeCard(
                            navController = navController,
                            value = type.name,
                            numberOfTudies = type.tudies,
                            icon = painterResource(id = type.iconRes)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(Dimens.Space150))
        }

        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.Space50),
            ) {
                TitlePlus("Type", navController = navController)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                        .padding(horizontal = Dimens.Space100),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Space125)
                ) {
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
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    val dates = viewModel.getEventsForSubject(subject).take(3)
                                    dates.forEach { date ->
                                        Text(
                                            text = formatDateToDayMonth(date),
                                            style = AppTypography.Caption1,
                                            color = BaseColor0
                                        )
                                    }
                                    if (viewModel.getEventsForSubject(subject).size > 3) {
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


                    // Колона с SubjectCard
                    Column(
                        modifier = Modifier.weight(2f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        activeSubjects.forEach { subject ->
                            SubjectCard(
                                navController = navController,
                                value = subject.name,
                                numberOfTudies = subject.tudies,
                                icon = painterResource(subject.iconRes)
                            )
                        }
                    }
                }
            }
        }

        item {
            if (activeSubjects.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Dimens.Space125))
            }
        }

        // --------- Subjects с Tudies = 0 ----------
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
                        icon = painterResource(subject.iconRes)
                    )
                }
            }
        }

    }
}
