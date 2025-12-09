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

@Composable
fun ErrorScreen(
    title: String,
    subtitle: String,
    description: String,
    buttonText: String = "Continue",
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.arrow_left),
                contentDescription = "Arrow Left",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { onArrowClick() }
                    .padding(Dimens.Space125)
            )
        }

        // GROUP 1
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LogoPlusTitle(title)
        }

        // GROUP 2
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.error_cross),
                contentDescription = "Cross"
            )
            Spacer(Modifier.height(Dimens.Space75))

            Text(subtitle, style = AppTypography.Heading4, color = ErrorColor)
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
            Image(
                painter = painterResource(id = R.drawable.giraffe_sad),
                contentDescription = "Sad Giraffe",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.End),
                contentScale = ContentScale.Fit
            )

            CustomButton(value = buttonText, enabled = true, onClick = onButtonClick)
            Spacer(Modifier.height(Dimens.Space175))
        }
    }
}
