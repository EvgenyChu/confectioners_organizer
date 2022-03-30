package ru.churkin.confectioners_organizer.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import ru.churkin.confectioners_organizer.R

val montserrataIternatesBold = FontFamily(
    Font(R.font.montserratalternates_bold)
)

val montserrataIternatesMedium = FontFamily(
    Font(R.font.montserratalternates_medium)
)

fun MyTypography(onPrimary: Color, onBackground: Color, surface: Color) = Typography(
    h1 = TextStyle(
        fontFamily = montserrataIternatesBold,

        fontSize = 96.sp
    ),
    h2 = TextStyle(
        fontFamily = montserrataIternatesBold,
        fontSize = 60.sp
    ),
    h3 = TextStyle(
        fontFamily = montserrataIternatesBold,
        fontSize = 48.sp
    ),
    h4 = TextStyle(
        fontFamily = montserrataIternatesBold,
        fontSize = 21.sp,
        color = onBackground
    ),
    h5 = TextStyle(
        fontFamily = montserrataIternatesBold,
        fontSize = 17.sp,
        color = onPrimary
    ),
    h6 = TextStyle(
        fontFamily = montserrataIternatesBold,
        fontSize = 21.sp,
        color = onPrimary
    ),
    subtitle1 = TextStyle(
        fontFamily = montserrataIternatesMedium,
        fontSize = 17.sp,
        color = onBackground
    ),
    subtitle2 = TextStyle(
        fontFamily = montserrataIternatesMedium,
        fontSize = 17.sp,
        color = surface
    ),
    body1 = TextStyle(
        fontFamily = montserrataIternatesBold,
        fontSize = 14.sp,
        color = onPrimary
    ),
    body2 = TextStyle(
        fontFamily = montserrataIternatesBold,
        fontSize = 14.sp,
        color = onBackground
    ),
    button = TextStyle(
        fontFamily = montserrataIternatesBold,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = montserrataIternatesBold,
        fontSize = 14.sp,
        color = onBackground
    ),
    overline = TextStyle(
        fontFamily = montserrataIternatesMedium,
        fontSize = 17.sp
    )
)