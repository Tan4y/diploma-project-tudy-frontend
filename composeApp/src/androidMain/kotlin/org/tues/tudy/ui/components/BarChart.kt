package org.tues.tudy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius250
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.ui.theme.shadow1


@Composable
fun BarChart(
    data: Map<String, Int>,
    modifier: Modifier = Modifier,
    maxBarHeight: Dp = 160.dp,
    title: String,
    weekRange: String
) {
    val maxValue = (data.values.maxOrNull() ?: 0).coerceAtLeast(1)

    val daysOfWeek = listOf(
        "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
    )

    Column(
        modifier = modifier
            .shadow1()
            .fillMaxWidth()
            .background(BaseColor0, shape = RoundedCornerShape(BorderRadius250))
            .clip(shape = RoundedCornerShape(BorderRadius250))
            .padding(Dimens.Space125),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = title,
            style = AppTypography.Heading5,
            color = BaseColor100
        )

        Spacer(modifier = Modifier.height(Dimens.Space50))

        Text(
            text = weekRange,
            style = AppTypography.Caption1,
            color = BaseColor80
        )

        Spacer(modifier = Modifier.height(Dimens.Space125))

        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            daysOfWeek.forEach { day ->
                val minutes = data[day] ?: 0
                val barHeight =
                    maxOf(
                        (minutes.toFloat() / maxValue) * maxBarHeight.value,
                    )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = if (minutes > 0) "$minutes" else "",
                        style = AppTypography.Caption1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .width(16.dp)
                            .height(barHeight.dp)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = 0.dp,
                                    bottomEnd = 0.dp
                                )
                            )
                            .background(PrimaryColor1)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}
