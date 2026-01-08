package org.tues.tudy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius200
import org.tues.tudy.ui.theme.Dimens.BorderRadius250
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.ui.theme.shadow1

@Composable
fun TypeCard(
    navController: NavController,
    value: String,
    icon: Painter,
    numberOfTudies: Int,
    onClick: () -> Unit
) {
    val activeColor = if (numberOfTudies > 0) PrimaryColor1 else BaseColor80
    Column(
        modifier = Modifier
            .shadow1()
            .height(164.dp)
            .clip(RoundedCornerShape(BorderRadius250))
            .background(BaseColor0)
            .clickable { onClick() }
            .padding(vertical = Dimens.Space125, horizontal = Dimens.Space250),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (numberOfTudies > 0)
            Arrangement.spacedBy(Dimens.Space100)
        else
            Arrangement.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.Space75)
        ) {
            Text(
                text = value,
                style = AppTypography.Heading5,
                color = activeColor
            )

            Icon(
                painter = icon,
                contentDescription = value,
                tint = activeColor,
            )
        }

        if (numberOfTudies > 0) {
            Text(
                text = "$numberOfTudies Tudies",
                style = AppTypography.Heading7,
                color = BaseColor100
            )
        }
    }
}