package ru.churkin.confectioners_organizer.ui.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.order.OrderScreen
import ru.churkin.confectioners_organizer.ui.theme.AppTheme

sealed class DrawerScreens(val title: String) {
    object OrdersScreen : DrawerScreens("Список заказов")
    object RecsScreen : DrawerScreens("Рецепты")
    object IngsScreen : DrawerScreens("Ингредиенты")
    object HistoryScreen : DrawerScreens("История заказов")
}

@Composable
fun DrawerScreen(navController: NavController) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primary)) {
            Row(
                modifier = Modifier
                    .padding(top = 24.dp, start = 16.dp, bottom = 16.dp)
                    .clickable { navController.navigate(Screen.Orders.route) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_assignment_24),
                    tint = MaterialTheme.colors.onBackground,
                    contentDescription = "Список заказов"
                )
                Text(
                    text = DrawerScreens.OrdersScreen.title,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colors.onBackground
            )
            Row(
                modifier = Modifier
                    .padding(top = 24.dp, start = 16.dp, bottom = 16.dp)
                    .clickable { navController.navigate(Screen.Recepts.route) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_cookie_24),
                    tint = MaterialTheme.colors.onBackground,
                    contentDescription = "Рецепты"
                )
                Text(
                    text = DrawerScreens.RecsScreen.title,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colors.onBackground
            )
            Row(
                modifier = Modifier
                    .padding(top = 24.dp, start = 16.dp, bottom = 16.dp)
                    .clickable { navController.navigate(Screen.Ingredients.route) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_shopping_cart_24),
                    tint = MaterialTheme.colors.onBackground,
                    contentDescription = "Ингредиенты"
                )
                Text(
                    text = DrawerScreens.IngsScreen.title,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colors.onBackground
            )
            Row(
                modifier = Modifier
                    .padding(top = 24.dp, start = 16.dp, bottom = 16.dp)
                    .clickable { navController.navigate(Screen.Orders.route) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_content_paste_24),
                    tint = MaterialTheme.colors.onBackground,
                    contentDescription = "История заказов"
                )
                Text(
                    text = DrawerScreens.HistoryScreen.title,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colors.onBackground
            )
        }
}

/*
@Preview
@Composable
fun previewDrawer() {
    AppTheme {
        DrawerScreen()
    }
}*/
