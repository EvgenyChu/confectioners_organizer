package ru.churkin.confectioners_organizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.InternalCoroutinesApi
import ru.churkin.confectioners_organizer.listRecepts.RecsScreen
import ru.churkin.confectioners_organizer.ui.recept.CreateReceptScreen
import ru.churkin.confectioners_organizer.ui.ingredient.IngScreen
import ru.churkin.confectioners_organizer.ui.list_ingredients.IngsScreen
import ru.churkin.confectioners_organizer.ui.recept.ReceptScreen
import ru.churkin.confectioners_organizer.ui.theme.AppTheme

@InternalCoroutinesApi
class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppTheme() {
                NavHost(navController = navController, startDestination = Screen.ListIngs.route) {
                    composable(Screen.ListIngs.route) { IngsScreen(navController = navController) }
                    composable(Screen.Ingredient.route) { IngScreen(navController = navController) }
                    composable(Screen.Recept.route) { CreateReceptScreen(navController = navController) }
                    composable(Screen.Recepts.route) { RecsScreen(navController = navController) }
                    composable(
                        "recepts/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) { ReceptScreen(navController = navController) }
                }
            }

        }
    }
}

sealed class Screen(val route: String, val title: String) {
    object Ingredient : Screen("ingredient", "ingredient")
    object ListIngs : Screen("listIngredients", "listIngs")
    object Recept : Screen("recept", "recept")
    object Recepts : Screen("recepts", "recepts")
}


