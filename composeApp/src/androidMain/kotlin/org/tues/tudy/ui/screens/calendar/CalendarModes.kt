package org.tues.tudy.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import org.tues.tudy.ui.components.CalendarMode
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius250
import org.tues.tudy.ui.theme.shadow1

@Composable
fun CalendarModes(
    navController: NavController,
    selectedMode: CalendarMode,
    userId: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.Space75),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .shadow1()
            .fillMaxWidth()
            .clip(RoundedCornerShape(BorderRadius250))
            .background(BaseColor0),
    ) {
        CalendarModeButton(
            text = "month",
            selected = selectedMode == CalendarMode.MONTH,
            onClick = {navController.navigate(Routes.calendarRoute(userId))},
            modifier = Modifier.weight(1f)
        )
        CalendarModeButton(
            text = "week",
            selected = selectedMode == CalendarMode.WEEK,
            onClick = {navController.navigate(Routes.calendarWeekRoute(userId))},
            modifier = Modifier.weight(1f)
        )
        CalendarModeButton(
            text = "day",
            selected = selectedMode == CalendarMode.DAY,
            onClick = {},
            modifier = Modifier.weight(1f)
        )
    }
}