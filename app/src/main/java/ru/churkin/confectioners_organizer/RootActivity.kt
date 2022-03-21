package ru.churkin.confectioners_organizer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.ingredient.IngredientScreen
import ru.churkin.confectioners_organizer.listOrders.OrdersScreen
import ru.churkin.confectioners_organizer.ui.list_recepts.RecsScreen
import ru.churkin.confectioners_organizer.order.OrderScreen
import ru.churkin.confectioners_organizer.product.CreateProductScreen
import ru.churkin.confectioners_organizer.ui.drawer.DrawerScreen
import ru.churkin.confectioners_organizer.ui.recept.CreateReceptScreen
import ru.churkin.confectioners_organizer.ui.ingredient.CreateIngredientScreen
import ru.churkin.confectioners_organizer.ui.list_ingredients.IngsScreen
import ru.churkin.confectioners_organizer.ui.order.CreateOrderScreen
import ru.churkin.confectioners_organizer.ui.recept.ReceptScreen
import ru.churkin.confectioners_organizer.ui.start.StartScreen
import ru.churkin.confectioners_organizer.ui.theme.AppTheme

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@InternalCoroutinesApi
class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            var isLockDrawer by remember { mutableStateOf(false)}
            LaunchedEffect(key1 = Unit){
                navController.addOnDestinationChangedListener { controller, destination, arguments ->
                   scope.launch {
                       Log.e("RootActivity", "${scaffoldState.drawerState}")
                       scaffoldState.drawerState.close()
                   }
                }
            }
            AppTheme() {
                Scaffold(
                    scaffoldState = scaffoldState,
                    drawerGesturesEnabled = !isLockDrawer,
                    drawerContent = { DrawerScreen(navController = navController) }) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Start.route
                    ) {
                        composable("start") {
                            StartScreen(navController = navController)
                        }
                        composable(Screen.Ingredients.route) {
                            isLockDrawer = false
                            IngsScreen(navController = navController, scaffoldState = scaffoldState, scope = scope) }
                        composable(Screen.Recepts.route) {
                            isLockDrawer = false
                            RecsScreen(navController = navController, scaffoldState = scaffoldState, scope = scope) }
                        composable(Screen.Orders.route) {
                            isLockDrawer = false
                            OrdersScreen(navController = navController, scaffoldState = scaffoldState, scope = scope) }
                        composable("orders/create") {
                            isLockDrawer = true
                            CreateOrderScreen(navController = navController) }
                        composable("products/create") {
                            isLockDrawer = true
                            CreateProductScreen(navController = navController) }
                        composable(
                            "orders/{order_id}/products/create",
                            arguments = listOf(navArgument("order_id")
                            { type = NavType.LongType })
                        ) { CreateProductScreen(navController = navController) }
                        composable("recepts/create") { CreateReceptScreen(navController = navController) }
                        composable("ingredients/create") { CreateIngredientScreen(navController = navController) }
                        composable(
                            "orders/{order_id}/products/{id}",
                            arguments = listOf(
                                navArgument("id") { type = NavType.LongType },
                                navArgument("order_id") { type = NavType.LongType }
                            )
                        ) { CreateProductScreen(navController = navController) }
                        composable(
                            "orders/edit/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { CreateOrderScreen(navController = navController) }
                        composable(
                            "recepts/edit/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { CreateReceptScreen(navController = navController) }
                        composable(
                            "ingredients/edit/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { CreateIngredientScreen(navController = navController) }
                        composable(
                            "orders/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { OrderScreen(navController = navController) }
                        composable(
                            "recepts/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { ReceptScreen(navController = navController) }
                        composable(
                            "ingredients/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { IngredientScreen(navController = navController) }

                    }

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
    object Order : Screen("order", "order")
    object Orders : Screen("orders", "orders")
    object Product : Screen("product", "product")
    object Start : Screen("start", "start")
}


