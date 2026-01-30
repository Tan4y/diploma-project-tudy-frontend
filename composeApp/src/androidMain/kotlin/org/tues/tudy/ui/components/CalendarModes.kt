package org.tues.tudy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius250
import org.tues.tudy.ui.theme.shadow1

@Composable
fun CalendarModes(
    selectedMode: CalendarMode,
    onModeSelected: (CalendarMode) -> Unit
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
            onClick = {onModeSelected(CalendarMode.MONTH)},
            modifier = Modifier.weight(1f)
        )
        CalendarModeButton(
            text = "week",
            selected = selectedMode == CalendarMode.WEEK,
            onClick = {onModeSelected(CalendarMode.WEEK)},
            modifier = Modifier.weight(1f)
        )
        CalendarModeButton(
            text = "day",
            selected = selectedMode == CalendarMode.DAY,
            onClick = {onModeSelected(CalendarMode.DAY)},
            modifier = Modifier.weight(1f)
        )
    }
}