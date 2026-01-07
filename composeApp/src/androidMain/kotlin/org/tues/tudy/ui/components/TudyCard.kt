package org.tues.tudy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius250
import org.tues.tudy.ui.theme.shadow1
import org.tues.tudy.utils.formatDate

@Composable
fun TudyCard(
    title: String,
    date: String,
    description: String?,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(Dimens.Space125),
        modifier = Modifier
            .shadow1()
            .clip(RoundedCornerShape(BorderRadius250))
            .background(BaseColor0)
            .padding(Dimens.Space125)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = AppTypography.Heading6,
                color = BaseColor100
            )
            Text(
                text = formatDate(date, true),
                style = AppTypography.Caption1,
                color = BaseColor80
            )
        }

        if (description != null) {
            Text(
                text = description,
                style = AppTypography.Caption1,
                color = BaseColor100,
                modifier = Modifier.fillMaxWidth()
            )
        }

        CustomButton(
            value = "Study",
            enabled = true,
            onClick = onClick,
            big = false
        )
    }
}