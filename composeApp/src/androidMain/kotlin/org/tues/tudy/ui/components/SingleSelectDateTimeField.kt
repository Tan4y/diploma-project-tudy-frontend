package org.tues.tudy.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius200

@Composable
fun SingleSelectDateTimeField(
    date: String,
    color: Color
) {
    val displayText = date.toIntOrNull()?.let { "%02d".format(it) } ?: date
    Box(
        modifier = Modifier
            //.fillMaxWidth()
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(BorderRadius200)
            )
            .padding(horizontal = Dimens.Space125, vertical = Dimens.Space100),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            style = AppTypography.Paragraph1,
            color = color,
        )
    }
}