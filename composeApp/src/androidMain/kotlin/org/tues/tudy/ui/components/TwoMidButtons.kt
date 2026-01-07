package org.tues.tudy.ui.components

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.ErrorColor

@Composable
fun TwoMidButtons (
    text1: String,
    text2: String,
    color1: androidx.compose.ui.graphics.Color? = null,
    color2: androidx.compose.ui.graphics.Color? = null,
    onClick1: () -> Unit,
    onClick2: () -> Unit
) {
    Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(Dimens.Space125),
    ) {
        CustomButton(
            value = text1,
            enabled = true,
            onClick = onClick1,
            size = ButtonSize.MEDIUM,
            color = color1,
            modifier = Modifier.weight(1f)
        )

        CustomButton(
            value = text2,
            enabled = true,
            onClick = onClick2,
            size = ButtonSize.MEDIUM,
            color = color2,
            modifier = Modifier.weight(1f)
        )
    }
}