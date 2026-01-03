package org.tues.tudy.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import org.tues.tudy.R
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius150
import org.tues.tudy.ui.theme.PrimaryColor1

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    primary: Boolean = true,
    heading: String,
) {
    var isOpen by remember { mutableStateOf(false) }
    val showIcon = if (primary) R.drawable.menu else R.drawable.arrow_left

    val rotation by animateFloatAsState(
        targetValue = if (primary && isOpen) 90f else 0f,
        label = "menuRotation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BaseColor0)
            .padding(vertical = Dimens.Space75, horizontal = Dimens.Space25),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            Icon(
                painter = painterResource(showIcon),
                contentDescription = "Menu",
                tint = PrimaryColor1,
                modifier = Modifier
                    .graphicsLayer {
                        rotationZ = rotation
                    }
                    .clip(RoundedCornerShape(BorderRadius150))
                    .clickable() {
                        if (primary) {
                            isOpen = !isOpen
                        } else {
                            navController?.popBackStack()
                        }
                    }
                    .padding(Dimens.Space50)
            )
        }

        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Text(
                text = heading,
                color = PrimaryColor1,
                style = AppTypography.Heading4,
            )
        }

        Box(Modifier.weight(1f))
    }

    if (isOpen) {
//         menu screen
//         (
//            onClose = { isOpen = false }
//        )
    }
}
