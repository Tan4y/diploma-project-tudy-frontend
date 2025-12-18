package org.tues.tudy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.tues.tudy.ui.theme.BaseColor40
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius200
import org.tues.tudy.ui.theme.PrimaryColor1

@Composable
fun ProgressBar(
    stages: Int,
    activeStages: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Space50),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(stages) { index ->
            val isActive = index < activeStages

            Box(
                modifier = modifier
                    .weight(1f)
                    .height(Dimens.Space50)
                    .clip(RoundedCornerShape(BorderRadius200))
                    .background(
                        if (isActive) PrimaryColor1 else BaseColor40
                    )
                    .border(
                        width = 1.dp,
                        color = if (isActive) PrimaryColor1 else BaseColor40,
                        shape = RoundedCornerShape(BorderRadius200)
                    )
            )
        }
    }
}