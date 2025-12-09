package org.tues.tudy.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.PrimaryColor1

@Composable
fun LinkButton (
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .clickable() { onClick() }
            .padding(Dimens.Space25),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            style = AppTypography.UnderlinedHeading6.copy(color = PrimaryColor1)
        )
    }
}