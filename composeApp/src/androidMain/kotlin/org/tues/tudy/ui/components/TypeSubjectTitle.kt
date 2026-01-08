package org.tues.tudy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.PrimaryColor1

@Composable
fun TypeSubjectTitle (
    title: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.Space75),
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = PrimaryColor1,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = title,
            style = AppTypography.Heading5,
            color = BaseColor100
        )
        HorizontalDivider(
            thickness = 1.dp,
            color = PrimaryColor1,
            modifier = Modifier.weight(11f),
        )
    }
}