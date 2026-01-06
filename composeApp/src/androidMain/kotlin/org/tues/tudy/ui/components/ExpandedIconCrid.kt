package org.tues.tudy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius150
import org.tues.tudy.ui.theme.Dimens.BorderRadius200
import org.tues.tudy.ui.theme.PrimaryColor1
import org.tues.tudy.ui.theme.shadow1

@Composable
fun ExpandedIconGrid(
    icons: List<Int>,
    selectedIcon: Int?,
    onIconSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .padding(top = Dimens.Space75)
            .border(
                width = 1.dp,
                color = PrimaryColor1,
                shape = RoundedCornerShape(BorderRadius200)
            )
            .clip(RoundedCornerShape(BorderRadius200))
            .heightIn(max = 300.dp)
    ) {
        items(icons) { icon ->

            val isSelected = icon == selectedIcon

            Box(
                modifier = Modifier
                    .padding(Dimens.Space75).then(
                        if (isSelected) Modifier.shadow1() else Modifier
                    )
                    .clip(RoundedCornerShape(BorderRadius150))
                    .background(if (isSelected) PrimaryColor1 else BaseColor0)
                    .padding(Dimens.Space25)
                    .clickable(
                        indication = null,
                        interactionSource = MutableInteractionSource()
                    ) {
                        onIconSelected(icon)
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = if (isSelected) BaseColor0 else BaseColor80,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}
