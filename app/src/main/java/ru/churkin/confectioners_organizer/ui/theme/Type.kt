package ru.churkin.confectioners_organizer.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.churkin.confectioners_organizer.R

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        color = Color(0xFFF7F6F6),
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        background = Color(0xFF232323)
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        color = Color(0xFF929292),
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        background = Color(0xFF232323)
    ),
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),    */
    caption = TextStyle(
        fontFamily = FontFamily.SansSerif,
        color = Color(0xFFF7F6F6),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)