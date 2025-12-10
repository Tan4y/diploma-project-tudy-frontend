package org.tues.tudy.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.tues.tudy.R
import org.tues.tudy.ui.components.CustomButton
import org.tues.tudy.ui.components.LogoPlusTitle
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.ErrorColor
import org.tues.tudy.ui.theme.SuccessColor

@Composable
fun SuccessErrorScreen(
    title: String,
    subtitle: String,
    description: String,
    buttonText: String = "Continue",
    arrow: Boolean = false,
    success: Boolean,
    onButtonClick: () -> Unit,
    onArrowClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Space100),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        if (arrow) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Dimens.Space125,
                        bottom = Dimens.Space125,
                        top = Dimens.Space150
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.arrow_left),
                    contentDescription = "Arrow Left",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable { onArrowClick() }
                )
            }
        } else {
            Spacer(modifier = Modifier.height(Dimens.Space150 + Dimens.Space125 + Dimens.Space150))
        }

        // GROUP 1
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LogoPlusTitle(title)
        }

        // GROUP 2
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (success) {
                Image(
                    painter = painterResource(id = R.drawable.success_tick),
                    contentDescription = "Tick"
                )
                Spacer(Modifier.height(Dimens.Space75))

                Text(subtitle, style = AppTypography.Heading4, color = SuccessColor)
            } else {
                Image(
                    painter = painterResource(id = R.drawable.error_cross),
                    contentDescription = "Cross"
                )
                Spacer(Modifier.height(Dimens.Space75))

                Text(subtitle, style = AppTypography.Heading4, color = ErrorColor)
            }

            Spacer(Modifier.height(Dimens.Space125))

            Text(
                description,
                style = AppTypography.Paragraph1,
                color = BaseColor80,
                textAlign = TextAlign.Center
            )
        }

        // GROUP 3
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.Space75)
        ) {
            if (success) {
                Image(
                    painter = painterResource(id = R.drawable.giraffe_happy),
                    contentDescription = "Happy Giraffe",
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.End),
                    contentScale = ContentScale.Fit
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.giraffe_sad),
                    contentDescription = "Sad Giraffe",
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.End),
                    contentScale = ContentScale.Fit
                )
            }

            CustomButton(value = buttonText, enabled = true, onClick = onButtonClick)
            Spacer(Modifier.height(Dimens.Space175))
        }
    }
}
