package ru.churkin.confectioners_organizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.churkin.confectioners_organizer.ingredient.IngScreen
import ru.churkin.confectioners_organizer.ingredient.IngsScreen
import ru.churkin.confectioners_organizer.ui.theme.AppTheme

val screens = listOf(
    Screen.Ingredient,
    Screen.ListIngs
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                // A surface container using the 'background' color from the theme
                IngsScreen()
        }
    }
}

sealed class Screen(val route: String, val title: String) {
    object Ingredient : Screen("ingredient", "ingredient")
    object ListIngs : Screen("listIngs", "listIngs")
}