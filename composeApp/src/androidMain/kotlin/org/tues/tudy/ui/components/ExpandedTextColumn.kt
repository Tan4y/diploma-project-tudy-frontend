package org.tues.tudy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.tues.tudy.data.model.TypeSubject
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius150
import org.tues.tudy.ui.theme.Dimens.BorderRadius200
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.ui.theme.shadow1

@Composable
fun ExpandedTextColumn(
    options: List<TypeSubject>,
    selectedOption: TypeSubject?,
    onOptionSelected: (TypeSubject) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .padding(top = Dimens.Space75)
            .border(
                width = 1.dp,
                color = PrimaryColor1,
                shape = RoundedCornerShape(BorderRadius200)
            )
            .clip(RoundedCornerShape(BorderRadius200))
            .heightIn(max = 300.dp)
            .padding(Dimens.Space125),
        verticalArrangement = Arrangement.spacedBy(Dimens.Space125)
    ) {
        items(options) { option ->

            val isSelected = option.name == selectedOption?.name

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (isSelected) Modifier.shadow1() else Modifier
                    )
                    .clip(RoundedCornerShape(BorderRadius150))
                    .background(if (isSelected) PrimaryColor1 else BaseColor0)
                    .padding(horizontal = Dimens.Space75, vertical = Dimens.Space50)
                    .clickable(
                        indication = null,
                        interactionSource = MutableInteractionSource()
                    ) {
                        onOptionSelected(option)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.Space75)
            ) {
                Text(
                    text = option.name,
                    style = AppTypography.Paragraph1,
                    color = if (isSelected) BaseColor0 else BaseColor80
                )
                Icon(
                    painter = painterResource(option.iconRes),
                    contentDescription = option.name,
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) BaseColor0 else BaseColor80
                )
            }
        }
    }
}
