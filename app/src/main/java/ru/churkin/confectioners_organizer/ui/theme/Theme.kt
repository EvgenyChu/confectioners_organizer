package ru.churkin.confectioners_organizer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.colorResource
import ru.churkin.confectioners_organizer.R

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Purple700
)

@Stable
/*private fun LightColorPalette(){
    return lightColors(
        primary = Purple500,
        primaryVariant = Purple700,
        secondary = colorResource(id = R.color.blue_theme)

        /* Other default colors to override
        background = Color.White,
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onBackground = Color.Black,
        onSurface = Color.Black,
        */
    )
}*/

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        lightColors(
            secondary = colorResource(id = R.color.blue_theme),
            secondaryVariant = colorResource(id = R.color.second_blue_theme),
            primary = colorResource(id = R.color.hamburger_menu),
            primaryVariant = colorResource(id = R.color.black_theme),
            onPrimary = colorResource(id = R.color.text),
            background = colorResource(id = R.color.background),
            surface = colorResource(id = R.color.second_red),
            error = colorResource(id = R.color.red),
            onSecondary = colorResource(id = R.color.green),
            onBackground = colorResource(id = R.color.second_green),
            onSurface = colorResource(id = R.color.yellow),
            onError = colorResource(id = R.color.second_text)
        )
    }

    MaterialTheme(
        colors = colors,
        typography = MyTypography(
            colorResource(id = R.color.text),
            colorResource(id = R.color.second_text),
            colorResource(id = R.color.background)
        ),
        shapes = Shapes,
        content = content
    )
}