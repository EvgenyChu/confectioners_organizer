package ru.churkin.confectioners_organizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.churkin.confectioners_organizer.recept.RecScreen
import ru.churkin.confectioners_organizer.ui.ingredient.IngScreen
import ru.churkin.confectioners_organizer.ui.list_ingredients.IngsScreen
import ru.churkin.confectioners_organizer.ui.theme.AppTheme

class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppTheme() {
                NavHost(navController = navController, startDestination = Screen.ListIngs.route) {
                    composable(Screen.ListIngs.route) { IngsScreen(navController = navController) }
                    composable(Screen.Ingredient.route) { IngScreen(navController = navController) }
                    composable(Screen.Recept.route) { RecScreen(navController = navController) }
                }
            }

        }
    }
}

sealed class Screen(val route: String, val title: String) {
    object Ingredient : Screen("ingredient", "ingredient")
    object ListIngs : Screen("listIngredients", "listIngs")
    object Recept : Screen("recept", "recept")
}


