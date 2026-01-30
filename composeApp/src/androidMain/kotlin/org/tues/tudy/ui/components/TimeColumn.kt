package org.tues.tudy.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.Dimens

@Composable
fun TimeColumn(
    startHour: Int,
    endHour: Int,
    hourHeightDp: Dp
) {
    Column (modifier = Modifier.padding(vertical = Dimens.Space50)) {
        for (hour in startHour..endHour) {
            Text(
                text = "%02d:00".format(hour),
                modifier = Modifier
                    .height(hourHeightDp)
                    .padding(end = Dimens.Space50),
                style = AppTypography.Caption1,
                color = BaseColor100
            )
        }
    }
}