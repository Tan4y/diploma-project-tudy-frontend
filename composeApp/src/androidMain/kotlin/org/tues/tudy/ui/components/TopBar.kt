package org.tues.tudy.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import org.tues.tudy.R
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius150
import org.tues.tudy.ui.theme.PrimaryColor1

@Composable
fun TopBar(
    navController: NavController? = null,
    mode: TopBarMode = TopBarMode.MENU,
    heading: String,
    onClose: (() -> Unit)? = null,
    isMenuOpen: Boolean,
    onMenuClick: () -> Unit,
    onBack: (() -> Unit)? = null
) {

    val leftIcon = when (mode) {
        TopBarMode.MENU -> R.drawable.menu
        TopBarMode.BACK -> R.drawable.arrow_left
        TopBarMode.CLOSE -> null
    }

    val rightIcon = when (mode) {
        TopBarMode.CLOSE -> R.drawable.cancel
        else -> null
    }

    val rotation by animateFloatAsState(
        targetValue = if (mode == TopBarMode.MENU && isMenuOpen) 90f else 0f,
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
        Box(contentAlignment = Alignment.CenterStart) {
            leftIcon?.let {
                Icon(
                    painter = painterResource(it),
                    contentDescription = "Left action",
                    tint = PrimaryColor1,
                    modifier = Modifier
                        .graphicsLayer {
                            if (mode == TopBarMode.MENU) {
                                rotationZ = rotation
                            }
                        }
                        .clip(RoundedCornerShape(BorderRadius150))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            when (mode) {
                                TopBarMode.MENU -> onMenuClick()
                                TopBarMode.BACK -> if (onBack != null) onBack() else navController?.popBackStack()
                                else -> {}
                            }

                        }
                        .padding(Dimens.Space50)
                )
            }
        }

        Box(contentAlignment = Alignment.Center) {
            Text(
                text = heading,
                color = PrimaryColor1,
                style = AppTypography.Heading4,
                textAlign = TextAlign.Center
            )
        }

        Box(contentAlignment = Alignment.CenterEnd) {
            if (rightIcon == null) {
                Spacer(modifier = Modifier.size(Dimens.Space200))
            }
            rightIcon?.let {
                Icon(
                    painter = painterResource(it),
                    contentDescription = "Close",
                    tint = PrimaryColor1,
                    modifier = Modifier
                        .clip(RoundedCornerShape(BorderRadius150))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) { onClose?.invoke() }
                        .padding(Dimens.Space50)
                )
            }
        }
    }
}
