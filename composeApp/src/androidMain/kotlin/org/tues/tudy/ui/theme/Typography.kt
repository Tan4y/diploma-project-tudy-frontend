package org.tues.tudy.ui.theme
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

object AppTypography {

    val Heading1 = TextStyle(
        fontFamily = PrimaryFont,
        fontSize = 40.sp,
        lineHeight = 44.sp,
        fontWeight = FontWeight.Bold
    )

    val Heading2 = TextStyle(
        fontFamily = PrimaryFont,
        fontSize = 36.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.Bold
    )

    val Heading3 = TextStyle(
        fontFamily = PrimaryFont,
        fontSize = 32.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Medium
    )

    val Heading4 = TextStyle(
        fontFamily = PrimaryFont,
        fontSize = 28.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Medium
    )

    val Heading5 = TextStyle(
        fontFamily = PrimaryFont,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Medium
    )

    val Heading6 = TextStyle(
        fontFamily = PrimaryFont,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium
    )

    val Heading7 = TextStyle(
        fontFamily = PrimaryFont,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium
    )

    val UnderlinedHeading6 = TextStyle(
        fontFamily = PrimaryFont,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium,
        textDecoration = TextDecoration.Underline
    )

    val UnderlinedHeading7 = TextStyle(
        fontFamily = PrimaryFont,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
        textDecoration = TextDecoration.Underline
    )

    val Body1 = TextStyle(
        fontFamily = SecondaryFont,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.ExtraBold,
    )

    val Body2 = TextStyle(
        fontFamily = SecondaryFont,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold,
    )

    val Paragraph1 = TextStyle(
        fontFamily = SecondaryFont,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold,
    )

    val Caption1 = TextStyle(
        fontFamily = SecondaryFont,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
    )

    val Caption2 = TextStyle(
        fontFamily = SecondaryFont,
        fontSize = 10.sp,
        lineHeight = 12.sp,
        fontWeight = FontWeight.Medium,
    )

    val UnderlinedParagraph1 = TextStyle(
        fontFamily = SecondaryFont,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline
    )

    val UnderlinedCaption1 = TextStyle(
        fontFamily = SecondaryFont,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
        textDecoration = TextDecoration.Underline
    )
}
