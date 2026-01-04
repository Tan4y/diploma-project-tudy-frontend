package org.tues.tudy.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.tues.tudy.R
import org.tues.tudy.data.model.TypeSubject
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius200

@Composable
fun <T> DropdownField(
    selectedItem: T?,
    expanded: Boolean,
    onToggleExpand: () -> Unit,
    onItemSelected: (Any) -> Unit,
    activeColor: androidx.compose.ui.graphics.Color,
    placeholder: String = "Select",
    icons: List<Int> = emptyList(),
    items: List<TypeSubject> = emptyList()
) {
    val isIconDropdown = icons.isNotEmpty()

    val isTypeSubjectDropdown = items.isNotEmpty()
    val selectedTypeSubject = items.find { it.name == selectedItem }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = activeColor,
                shape = RoundedCornerShape(BorderRadius200)
            )
            .clip(RoundedCornerShape(BorderRadius200))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onToggleExpand() }
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        when {
            isIconDropdown && selectedItem is Int -> Icon(
                painter = painterResource(selectedItem),
                contentDescription = null,
                tint = BaseColor100,
                modifier = Modifier.size(28.dp)
            )

            isTypeSubjectDropdown && selectedTypeSubject != null -> Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = selectedTypeSubject.name,
                    style = AppTypography.Paragraph1,
                    color = BaseColor100,
                    modifier = Modifier.padding(end = Dimens.Space75)
                )
                Icon(
                    painter = painterResource(selectedTypeSubject.iconRes),
                    contentDescription = selectedTypeSubject.name,
                    tint = BaseColor100,
                    modifier = Modifier.size(24.dp)
                )
            }

            else -> Text(
                text = placeholder,
                style = AppTypography.Paragraph1,
                color = BaseColor80
            )
        }

        Icon(
            painter = painterResource(R.drawable.arrow_down),
            contentDescription = "Dropdown arrow",
            tint = activeColor,
            modifier = Modifier.rotate(if (expanded) 180f else 0f)
        )
    }
    if (expanded) {
        if (isIconDropdown) {
            ExpandedIconGrid(
                icons = icons,
                selectedIcon = selectedItem as? Int,
                onIconSelected = {
                    onItemSelected(it)
                    onToggleExpand()
                }
            )
        }

        if (isTypeSubjectDropdown) {
            ExpandedTextColumn(
                options = items,
                selectedOption = selectedTypeSubject,
                onOptionSelected = {
                    onItemSelected(it)
                }
            )
        }
    }
}
