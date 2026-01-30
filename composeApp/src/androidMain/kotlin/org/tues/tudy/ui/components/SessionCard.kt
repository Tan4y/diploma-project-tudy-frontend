package org.tues.tudy.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.tues.tudy.data.model.CalendarItem
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius200
import org.tues.tudy.ui.theme.shadow1
import org.tues.tudy.utils.formatDate
import org.tues.tudy.utils.formatTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SessionCard(
    session: CalendarItem,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow1()
            .background(BaseColor0, RoundedCornerShape(BorderRadius200))
            .padding(horizontal = Dimens.Space100, vertical = Dimens.Space75),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = formatDate(session.startTime ?: ""),
            style = AppTypography.Caption1,
            color = BaseColor80
        )

        Text(
            text = "${formatTime(session.startTime ?: "")} - ${
                formatTime(session.endTime ?: "")
            }",
            style = AppTypography.Caption1,
            color = BaseColor80
        )

        session.pagesFrom?.let { from ->
            session.pagesTo?.let { to ->
                Text(
                    text = " Pages: $from – $to",
                    style = AppTypography.Caption1,
                    color = BaseColor80
                )
            }
        }

        CustomButton(
            value = "Study",
            enabled = true,
            onClick = { /* STUDY */ },
            size = ButtonSize.SMALL,
        )
    }
}
