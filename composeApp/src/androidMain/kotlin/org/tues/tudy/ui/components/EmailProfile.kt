package org.tues.tudy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import org.tues.tudy.R
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.Dimens

@Composable
fun EmailProfile(
    owner: Boolean? = false
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimens.Space75)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.Space50),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.email),
                tint = BaseColor100,
                contentDescription = "Email",
            )
            Text(
                text = "tudy.app.support@gmail.com",
                style = AppTypography.Paragraph1,
                color = BaseColor100
            )
        }

        if (owner == true) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.Space50),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.profile_outlined),
                    tint = BaseColor100,
                    contentDescription = "Owner",
                )
                Text(
                    text = "Tanya Koleva – Developer and Owner of Tudy",
                    style = AppTypography.Paragraph1,
                    color = BaseColor100
                )
            }
        }
    }
}