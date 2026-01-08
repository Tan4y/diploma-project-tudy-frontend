package org.tues.tudy.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.tues.tudy.ui.theme.AppTypography
import org.tues.tudy.ui.theme.BaseColor0
import org.tues.tudy.ui.theme.BaseColor80
import org.tues.tudy.ui.theme.Dimens
import org.tues.tudy.ui.theme.ErrorColor

@Composable
fun BasePopUp(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    description: String
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Space200)
        ) {

            Surface(
                shape = RoundedCornerShape(Dimens.BorderRadius250),
                color = BaseColor0,
                tonalElevation = 6.dp,
            ) {
                Column(
                    modifier = Modifier
                        .padding(Dimens.Space200),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Space200),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CrossTitlePopUp(
                        onClick = onDismiss,
                        title = title,
                    )
                    Text(
                        text = description,
                        style = AppTypography.Paragraph1,
                        color = BaseColor80,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    TwoMidButtons(
                        text1 = "Go Back",
                        text2 = "Delete",
                        color1 = BaseColor80,
                        color2 = ErrorColor,
                        onClick1 = onDismiss,
                        onClick2 = onConfirm
                    )
                }
            }
        }
    }
}
