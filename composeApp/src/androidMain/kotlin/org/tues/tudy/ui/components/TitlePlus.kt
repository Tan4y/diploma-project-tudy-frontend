package org.tues.tudy.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.tues.tudy.R
import androidx.navigation.NavController
import org.tues.tudy.ui.navigation.Routes
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens

@Composable
fun TitlePlus(
    value: String,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Row(
        modifier = Modifier.padding(Dimens.Space125).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = value,
            style = AppTypography.Heading5,
            color = BaseColor100,
        )

        Icon(
            painter = painterResource(R.drawable.plus_outlined),
            contentDescription = "Plus",
            tint = BaseColor80,
            modifier = Modifier.size(Dimens.Space175).clickable { navController.navigate(Routes.ADD) }
        )
    }
}