package org.tues.tudy.ui.components

import android.graphics.Color
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
import org.tues.tudy.ui.theme.Dimens.BorderRadius150
import org.tues.tudy.ui.theme.Dimens.BorderRadius200
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.ui.theme.shadow1

@Composable
fun CustomButton(
    value: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: ButtonSize = ButtonSize.LARGE,
    color: androidx.compose.ui.graphics.Color? = null
) {
    val enabledColor = color ?: PrimaryColor1
    val backgroundColor = if (enabled) enabledColor else BaseColor80

    val textStyle = when (size) {
        ButtonSize.SMALL -> AppTypography.Heading7
        ButtonSize.MEDIUM -> AppTypography.Heading5
        ButtonSize.LARGE -> AppTypography.Heading4
    }

    val verticalPadding = when (size) {
        ButtonSize.SMALL -> Dimens.Space0
        ButtonSize.MEDIUM -> Dimens.Space50
        ButtonSize.LARGE -> Dimens.Space50
    }

    val buttonWidth = when (size) {
        ButtonSize.SMALL -> Modifier
        ButtonSize.MEDIUM -> Modifier.fillMaxWidth()
        ButtonSize.LARGE -> Modifier.fillMaxWidth()
    }

    val borderRadius = when (size) {
        ButtonSize.SMALL -> BorderRadius150
        ButtonSize.MEDIUM -> BorderRadius200
        ButtonSize.LARGE -> BorderRadius200
    }

    Box(
        modifier = modifier
            .then(buttonWidth)
            .shadow1()
//            .border(
//                width = 1.dp,
//                color = backgroundColor,
//                shape = RoundedCornerShape(BorderRadius200)
//            )
            .background(backgroundColor, RoundedCornerShape(borderRadius))
            .clip(RoundedCornerShape(BorderRadius200))
            .clickable(enabled = enabled) { onClick() }
            .padding(
                vertical = verticalPadding,
                horizontal = Dimens.Space50
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            style = textStyle,
            color = BaseColor0
        )
    }
}