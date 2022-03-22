package ru.churkin.confectioners_organizer.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.date.format
import ru.churkin.confectioners_organizer.ui.order.OrderProductItem
import ru.churkin.confectioners_organizer.view_models.order.data.OrderViewModel

@Composable
fun OrderScreen(navController: NavController, vm: OrderViewModel = viewModel()) {

    val state by vm.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        vm.initState()
    }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
        ) {

            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
                    IconButton(onClick = {navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Назад"
                        )
                    }
                    Text(
                        "Заказ",
                        style = MaterialTheme.typography.h6,
                    )
                    Spacer(Modifier.weight(1f, true))

                    IconButton(onClick = { navController.navigate("orders/edit/${state.id}") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Очистить"
                        )
                    }
                }

                Row(
                    Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Заказчик: ${state.customer}",
                        style = MaterialTheme.typography.subtitle1
                    )
                }

                Divider(color = MaterialTheme.colors.secondary)

                Row(
                    Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        if (state.phone == null) "тел. +7хххххххххх" else "тел. ${state.phone}" ,
                        style = MaterialTheme.typography.subtitle1
                    )
                }

                Divider(color = MaterialTheme.colors.secondary)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (state.deadLine == null) "DeadLine: _._._ г." else "DeadLine: ${state.deadLine?.format("dd.MM.yyyy")}",
                        modifier = Modifier
                            .padding(start = 16.dp),
                        style = MaterialTheme.typography.subtitle1,
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colors.secondary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(start = 16.dp),
                        painter = painterResource(id = R.drawable.ic_baseline_check_circle),
                        tint = if (!state.needDelivery) MaterialTheme.colors.surface else MaterialTheme.colors.secondary,
                        contentDescription = "Доставка"
                    )
                    Text(
                        text = if (state.needDelivery) "Доставка" else "Без доставки",
                        modifier = Modifier
                            .padding(start = 16.dp),
                        style = MaterialTheme.typography.subtitle1,
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colors.secondary
                )

                Row(
                    Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        if (state.address==null) "Адрес доставки не указан" else "Адрес: ${state.address}" ,
                        style = MaterialTheme.typography.subtitle1
                    )
                }

                Divider(color = MaterialTheme.colors.secondary)

                Row(
                    Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Список изделий для заказа:",
                        style = MaterialTheme.typography.subtitle1
                    )
                }

                if (state.products?.isNotEmpty() == true) state.products?.forEach {product ->
                    OrderProductItem(product = product) {
                        navController.navigate("orders/${product.orderId}/products/${product.id}")
                    }
                }


                Row(
                    Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Цена заказа: ${state.price} руб.",
                        style = MaterialTheme.typography.subtitle1
                    )
                }

                Divider(color = MaterialTheme.colors.secondary)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(start = 16.dp),
                        painter = painterResource(id = R.drawable.ic_baseline_check_circle),
                        tint = if (state.isPaid) MaterialTheme.colors.secondary else MaterialTheme.colors.surface,
                        contentDescription = "Доставка"
                    )
                    Text(
                        text = if (state.isPaid) "Заказ оплачен" else "Заказ не оплачен",
                        modifier = Modifier
                            .padding(start = 16.dp),
                        style = MaterialTheme.typography.subtitle1,
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colors.secondary
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Отсутствуют продукты: \n${state.missingIngredients.lowercase().capitalize()}",
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                Divider(color = MaterialTheme.colors.secondary)

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Примечание: \n${state.note}",
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                Divider(color = MaterialTheme.colors.secondary)

                Spacer(modifier = Modifier.height(56.dp))
            }


            Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {

                BottomAppBar(
                    backgroundColor = MaterialTheme.colors.primary,
                    modifier = Modifier.height(56.dp)
                ) {

                    Text(
                        "Пора начать готовить)",
                        modifier = Modifier.padding(start = 12.dp),
                        style = MaterialTheme.typography.body1
                    )

                }
            }
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.Orders.route)
                },
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .padding(bottom = 28.dp, end = 16.dp),
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onSecondary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_done_24),
                    contentDescription = "Добавить"
                )
            }
        }
    }


/*
@Preview
@Composable
fun previewOrder() {
    AppTheme {
        OrderScreen()
    }
}*/
