package org.tues.tudy.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.PrimaryColor1

@Composable
fun TitleDescription (
    title: String,
    description: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = title,
            style = AppTypography.Heading5,
            color = PrimaryColor1
        )

        Spacer(modifier = Modifier.height(Dimens.Space75))

        Text(
            text = description,
            style = AppTypography.Paragraph1,
            color = BaseColor100
        )
    }
}