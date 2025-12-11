package org.tues.tudy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius200
import org.tues.tudy.ui.theme.PrimaryColor1

@Composable
fun CustomButton(
    value: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (enabled) PrimaryColor1 else BaseColor80

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = backgroundColor,
                shape = RoundedCornerShape(BorderRadius200)
            )
            .background(backgroundColor, RoundedCornerShape(BorderRadius200))
            .clip(RoundedCornerShape(BorderRadius200))
            .clickable(enabled = enabled) { onClick() }
            .padding(vertical = Dimens.Space50),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            style = AppTypography.Heading4.copy(color = BaseColor0)
        )
    }
}