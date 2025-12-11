package org.tues.tudy.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor100
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.Dimens.BorderRadius150
import org.tues.tudy.ui.theme.Dimens.BorderRadius200
import org.tues.tudy.ui.theme.ErrorColor
import org.tues.tudy.ui.theme.PrimaryColor1


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    forgotPassword: Boolean = false,
    onForgotPassword: () -> Unit = { "forgotPassword" },
    trailingIcon: (@Composable (() -> Unit))? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val isFocused = remember { mutableStateOf(false) }

    val stateColor = when {
        !error.isNullOrEmpty() -> ErrorColor
        isFocused.value -> PrimaryColor1
        value.isNotEmpty() -> BaseColor100
        else -> BaseColor80
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = AppTypography.Caption1,
            color = if (stateColor == BaseColor80) Color.Transparent else stateColor,
            modifier = Modifier
                .padding(Dimens.Space25)
                .animateContentSize()
        )
        Box(
            modifier = modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = stateColor,
                    shape = RoundedCornerShape(BorderRadius200)
                )
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = AppTypography.Paragraph1.copy(color = stateColor),
                placeholder = {
                    Text(
                        text = label,
                        style = AppTypography.Caption1.copy(color = BaseColor80)
                    )
                },
                trailingIcon = trailingIcon,
                visualTransformation = visualTransformation,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isFocused.value = it.isFocused },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = MaterialTheme.colorScheme.surface,
                ),
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Space25))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimens.Space75, end = Dimens.Space25),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (!error.isNullOrEmpty()) {
                Text(
                    text = error,
                    color = ErrorColor,
                    style = AppTypography.Caption2
                )
            } else {
                Spacer(modifier = Modifier.height(Dimens.Space100))
            }

            if (forgotPassword) {
                Text(
                    text = "Forgot Password",
                    color = PrimaryColor1,
                    style = AppTypography.UnderlinedCaption1,
                    modifier = Modifier
                        .clip(RoundedCornerShape(BorderRadius150))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onForgotPassword() }
                )
            }
        }

    }
}

