package org.tues.tudy.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens

@Composable
fun DateTimePicker(
    day: Int,
    month: Int,
    year: Int,
    onDayChange: (Int) -> Unit,
    onMonthChange: (Int) -> Unit,
    onYearChange: (Int) -> Unit,
    hour: Int,
    minute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    onDatePicked: (Boolean) -> Unit = {},
    onTimePicked: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var datePicked by remember { mutableStateOf(false) }
    val activeColorDate = if (datePicked) BaseColor100 else BaseColor80

    var timePicked by remember { mutableStateOf(false) }
    val activeColorTime = if (timePicked) BaseColor100 else BaseColor80

    Row(
        modifier = Modifier.padding(start = Dimens.Space75),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Date: ",
            style = AppTypography.Paragraph1,
            color = BaseColor100
        )

        Spacer(modifier = Modifier.width(Dimens.Space100))

        Button(
            onClick = {
                DatePickerDialog(
                    context,
                    { _, selectedYear, selectedMonth, selectedDay ->
                        onDayChange(selectedDay)
                        onMonthChange(selectedMonth + 1)
                        onYearChange(selectedYear)
                        datePicked = true
                        onDatePicked(true)
                    },
                    year,
                    month - 1,
                    day
                ).show()
            },
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BaseColor0,
                contentColor = BaseColor0
            ),
            shape = RoundedCornerShape(Dimens.BorderRadius200),
            modifier = Modifier.wrapContentWidth(),
        ) {
            FullSelectDateTimeField(day, month, year, activeColorDate)
        }
    }


    Spacer(modifier = Modifier.height(Dimens.Space150))

// Time input row (HH : MM : SS)
    Row(
        modifier = Modifier.padding(start = Dimens.Space75),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Time: ",
            style = AppTypography.Paragraph1,
            color = BaseColor100
        )

        Spacer(modifier = Modifier.width(Dimens.Space100))

        Button(
            onClick = {
                TimePickerDialog(
                    context,
                    { _, selectedHour, selectedMinute ->
                        onHourChange(selectedHour)
                        onMinuteChange(selectedMinute)
                        timePicked = true
                        onTimePicked(true)
                    },
                    hour,
                    minute,
                    true
                ).show()
            },
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BaseColor0,
                contentColor = BaseColor0
            ),
            shape = RoundedCornerShape(Dimens.BorderRadius200),
            modifier = Modifier.wrapContentWidth(),
        ) {
            FullSelectDateTimeField(hour,minute, null,activeColorTime, time = true)
        }
    }
}
