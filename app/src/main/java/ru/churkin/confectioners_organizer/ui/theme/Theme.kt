package ru.churkin.confectioners_organizer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import ru.churkin.confectioners_organizer.R

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        darkColors(
            secondary = BlueTheme,
            secondaryVariant = SecondBlueTheme,
            primary = HamburgerMenu,
            primaryVariant = BlackTheme,
            onPrimary = TextOn,
            background = Background,
            surface = HamburgerMenu,
            error = Red,
            onSecondary = TextOn,
            onBackground = TextOn,
            onSurface = TextOn,
            onError = TextOn
        )

    } else {
        lightColors(

/*        secondary = BlueTheme,
        secondaryVariant = SecondBlueTheme,
        primary = HamburgerMenu,
        primaryVariant = BlackTheme,
        onPrimary = TextOn,
        background = Background,
        surface = HamburgerMenu,
        error = Red,
        onSecondary = TextOn,
        onBackground = TextOn,
        onSurface = TextOn,
        onError = TextOn */

                 secondary = LightSecondary,
                 secondaryVariant = LightSecondaryVariant,
                 primary = LightPrimary,
                 primaryVariant = LightPrimaryVariant,
                 onPrimary = BlackTheme,
                 background = LightBackground,
                 surface = LightSurface,
                 error = Red,
                 onSecondary = TextOn,
                 onBackground = LightPrimaryVariant,
                 onSurface = LightPrimaryVariant,
                 onError = TextOn
    )
}
MaterialTheme(
    colors = colors,
    typography = MyTypography(
        colors.onPrimary, colors.onBackground, colors.surface),
    shapes = Shapes,
    content = content
)
}