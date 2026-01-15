package org.tues.tudy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tues.tudy.data.model.CalendarItem
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens

@Composable
fun Sessions(
    sessions: List<CalendarItem>
) {
    val sessionsCount = sessions.size
    val height = Dimens.Space250 * sessionsCount + Dimens.Space75 * (sessionsCount - 1)
    Row(
        modifier = Modifier.fillMaxWidth().height(height).padding(start = Dimens.Space150),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Space75),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VerticalDivider(
            thickness = 1.dp,
            color = BaseColor80,
            modifier = Modifier.fillMaxHeight()
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.Space75),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            sessions.forEach { session ->
                SessionCard(session)
            }
        }
    }
}