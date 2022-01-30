package ru.churkin.confectioners_organizer.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.churkin.confectioners_organizer.R

// Set of Material typography styles to start with
fun MyTypography(onPrimary: Color, onError: Color, background: Color) = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        color = background,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        color = onPrimary,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        color = onError,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),    */
    caption = TextStyle(
        fontFamily = FontFamily.SansSerif,
        color = onPrimary,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)