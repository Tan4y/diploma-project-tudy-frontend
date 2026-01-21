package org.tues.tudy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius250
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.ui.theme.shadow1

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .shadow1()
            .fillMaxWidth()
            .background(BaseColor0, shape = RoundedCornerShape(BorderRadius250))
            .clip(shape = RoundedCornerShape(BorderRadius250))
            .padding(Dimens.Space125),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.Space75)
    ) {
        Text(text = title, style = AppTypography.Heading5, color = BaseColor100)
        Text(text = value, style = AppTypography.Big, color = PrimaryColor1)
    }
}