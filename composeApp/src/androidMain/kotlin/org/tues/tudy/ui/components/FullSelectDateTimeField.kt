package org.tues.tudy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.Dimens

@Composable
fun FullSelectDateTimeField(
    day: Int,
    month: Int,
    year: Int?,
    color: Color,
    time: Boolean = false
) {
    val monthNames = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )
    val monthText = if (month in 1..12) monthNames[month - 1] else month.toString()

    Row(
        //modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Space75)
    ) {
        SingleSelectDateTimeField(day.toString(), color)
        if (time) {
            Text(
                text = ":",
                style = AppTypography.Paragraph1,
                color = color
            )
            SingleSelectDateTimeField(month.toString(), color)
        } else {
            SingleSelectDateTimeField(monthText, color)
        }
        if (year != null) {
            SingleSelectDateTimeField(year.toString(), color)
        }
    }
}