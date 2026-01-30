package org.tues.tudy.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import org.tues.tudy.R
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.PrimaryColor2

@Composable
fun LogoPlusTitle (
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.Space50)
    ) {
        Image(
            painter = painterResource(id = R.drawable.tudylogo),
            contentDescription = "App Logo"
        )

        Text(
            text = value,
            style = AppTypography.Heading3,
            color = PrimaryColor2
        )
    }
}