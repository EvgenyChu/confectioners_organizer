package ru.churkin.confectioners_organizer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.history.OrdersHistoryScreen
import ru.churkin.confectioners_organizer.history.OrdersHistoryToolBar
import ru.churkin.confectioners_organizer.ingredient.IngredientScreen
import ru.churkin.confectioners_organizer.ingredient.IngredientToolBar
import ru.churkin.confectioners_organizer.ui.list_orders.OrdersScreen
import ru.churkin.confectioners_organizer.ui.list_orders.OrdersToolBar
import ru.churkin.confectioners_organizer.ui.list_recepts.RecsScreen
import ru.churkin.confectioners_organizer.order.OrderScreen
import ru.churkin.confectioners_organizer.order.OrderToolBar
import ru.churkin.confectioners_organizer.product.CreateProductScreen
import ru.churkin.confectioners_organizer.product.CreateProductToolBar
import ru.churkin.confectioners_organizer.ui.drawer.DrawerScreen
import ru.churkin.confectioners_organizer.ui.recept.CreateReceptScreen
import ru.churkin.confectioners_organizer.ui.ingredient.CreateIngredientScreen
import ru.churkin.confectioners_organizer.ui.ingredient.CreateIngredientToolBar
import ru.churkin.confectioners_organizer.ui.list_ingredients.IngsScreen
import ru.churkin.confectioners_organizer.ui.list_ingredients.IngsToolBar
import ru.churkin.confectioners_organizer.ui.list_recepts.RecsToolBar
import ru.churkin.confectioners_organizer.ui.order.CreateOrderScreen
import ru.churkin.confectioners_organizer.ui.order.CreateOrderToolBar
import ru.churkin.confectioners_organizer.ui.recept.CreateReceptToolBar
import ru.churkin.confectioners_organizer.ui.recept.ReceptScreen
import ru.churkin.confectioners_organizer.ui.recept.ReceptToolBar
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
            var isLockDrawer by remember { mutableStateOf(false) }
            LaunchedEffect(key1 = Unit) {
                navController.addOnDestinationChangedListener { controller, destination, arguments ->
                    isLockDrawer = when (destination.route) {
                        "ingredients" -> false
                        "recepts" -> false
                        "orders" -> false
                        "history" -> false
                        else -> true
                    }
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
                    drawerContent = { DrawerScreen(navController = navController) },
                    topBar = {
                        ToolBarHost(navController, onMenuClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        })
                    }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Start.route
                    ) {
                        composable("start") {
                            StartScreen(navController = navController)
                        }
                        composable(Screen.Ingredients.route) {
                            IngsScreen(navController = navController)
                        }
                        composable(Screen.Recepts.route) {
                            RecsScreen(
                                navController = navController
                            )
                        }
                        composable(Screen.Orders.route) {
                            OrdersScreen(
                                navController = navController,
                            )
                        }
                        composable(Screen.History.route) {
                            OrdersHistoryScreen(
                                navController = navController
                            )
                        }
                        composable("orders/create") {
                            CreateOrderScreen(navController = navController, id = null)
                        }
                        composable("products/create") {
                            CreateProductScreen(navController = navController, id = null, orderId = null)
                        }
                        composable(
                            "orders/{order_id}/products/create",
                            arguments = listOf(navArgument("order_id")
                            { type = NavType.LongType })
                        ) { CreateProductScreen(navController = navController, orderId = it.arguments?.getLong("order_id"), id=null) }
                        composable("recepts/create") { CreateReceptScreen(navController = navController, id = null) }
                        composable("ingredients/create") { CreateIngredientScreen(navController = navController, id=null) }
                        composable(
                            "orders/{order_id}/products/{id}",
                            arguments = listOf(
                                navArgument("id") { type = NavType.LongType },
                                navArgument("order_id") { type = NavType.LongType }
                            )
                        ) { CreateProductScreen(navController = navController, orderId = it.arguments?.getLong("order_id"), id = it.arguments?.getLong("id")) }
                        composable(
                            "orders/edit/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { CreateOrderScreen(navController = navController, id = it.arguments?.getLong("id")) }
                        composable(
                            "recepts/edit/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { CreateReceptScreen(navController = navController, id = it.arguments?.getLong("id")) }
                        composable(
                            "ingredients/edit/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { CreateIngredientScreen(navController = navController, id = it.arguments?.getLong("id")) }
                        composable(
                            "orders/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { OrderScreen(navController = navController, id = checkNotNull(it.arguments?.getLong("id"))) }
                        composable(
                            "recepts/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { ReceptScreen(navController = navController, id = checkNotNull(it.arguments?.getLong("id"))) }
                        composable(
                            "ingredients/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.LongType })
                        ) { IngredientScreen(navController = navController, id = checkNotNull(it.arguments?.getLong("id"))) }

                    }

                }

            }

        }
    }
}


@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@OptIn(InternalCoroutinesApi::class)
@Composable
private fun ToolBarHost(navController: NavController, onMenuClick: () -> Unit) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    when (currentRoute) {
        Screen.Ingredients.route -> IngsToolBar(onMenuClick = onMenuClick)
        Screen.Ingredient.route -> IngredientToolBar(navController = navController)
        "ingredients/{id}" -> IngredientToolBar(navController = navController)
        Screen.Recept.route -> ReceptToolBar(navController = navController)
        "recepts/{id}" -> ReceptToolBar(navController = navController)
        Screen.Recepts.route -> RecsToolBar(onMenuClick = onMenuClick)
        Screen.Order.route -> OrderToolBar(navController = navController)
        "orders/{id}" -> OrderToolBar(navController = navController)
        Screen.Orders.route -> OrdersToolBar(onMenuClick = onMenuClick)
        Screen.History.route -> OrdersHistoryToolBar(onMenuClick = onMenuClick)
        "products/create" -> CreateProductToolBar(navController = navController)
        "orders/{order_id}/products/create" -> CreateProductToolBar(navController = navController)
        "orders/{order_id}/products/{id}" -> CreateProductToolBar(navController = navController)
        "recepts/create" -> CreateReceptToolBar(navController = navController)
        "recepts/edit/{id}" -> CreateReceptToolBar(navController = navController)
        "ingredients/create" -> CreateIngredientToolBar(navController = navController)
        "ingredients/edit/{id}" -> CreateIngredientToolBar(navController = navController)
        "orders/create" -> CreateOrderToolBar(navController = navController)
        "orders/edit/{id}" -> CreateOrderToolBar(navController = navController)
        else -> ""
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
    object History: Screen("history", "history")
}


