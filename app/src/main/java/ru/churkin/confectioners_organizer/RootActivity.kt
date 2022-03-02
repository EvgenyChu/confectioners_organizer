package ru.churkin.confectioners_organizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.InternalCoroutinesApi
import ru.churkin.confectioners_organizer.ingredient.IngredientScreen
import ru.churkin.confectioners_organizer.listRecepts.RecsScreen
import ru.churkin.confectioners_organizer.ui.recept.CreateReceptScreen
import ru.churkin.confectioners_organizer.ui.ingredient.CreateIngredientScreen
import ru.churkin.confectioners_organizer.ui.list_ingredients.IngsScreen
import ru.churkin.confectioners_organizer.ui.recept.ReceptScreen
import ru.churkin.confectioners_organizer.ui.theme.AppTheme

@ExperimentalMaterialApi
@InternalCoroutinesApi
class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppTheme() {
                NavHost(navController = navController, startDestination = Screen.Ingredients.route) {
                    composable(Screen.Ingredients.route) { IngsScreen(navController = navController) }
                    composable(Screen.Ingredient.route) { CreateIngredientScreen(navController = navController) }
                    composable(Screen.Recepts.route) { RecsScreen(navController = navController) }
                    composable("recepts/create") { CreateReceptScreen(navController = navController) }
                    composable("recepts/edit/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) { CreateReceptScreen(navController = navController) }
                    composable(
                        "recepts/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) { ReceptScreen(navController = navController) }
                    composable("ingredients/create") { CreateIngredientScreen(navController = navController) }
                    composable("ingredients/edit/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) { CreateIngredientScreen(navController = navController) }
                    composable(
                        "ingredients/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) { IngredientScreen(navController = navController) }
                }
            }

        }
    }
}

sealed class Screen(val route: String, val title: String) {
    object Ingredient : Screen("ingredient", "ingredient")
    object Ingredients : Screen("ingredients", "ingredients")
    object Recept : Screen("recept", "recept")
    object Recepts : Screen("recepts", "recepts")
}


