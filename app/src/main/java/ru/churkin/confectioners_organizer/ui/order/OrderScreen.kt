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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.InternalCoroutinesApi
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.RootActivity
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.date.format
import ru.churkin.confectioners_organizer.local.db.entity.Product
import ru.churkin.confectioners_organizer.view_models.order.data.OrderViewModel

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun OrderScreen(
    navController: NavController,
    id:Long,
    vm: OrderViewModel = viewModel(LocalContext.current as RootActivity, key = "order")
) {

    val state by vm.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        vm.initState(id)
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

            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (!state.isCooked) "Заказ в работе" else "Заказ выполнен",
                    style = MaterialTheme.typography.subtitle1
                )

                Spacer(Modifier.weight(1f, true))

                Switch(
                    checked = state.isCooked,
                    onCheckedChange = {
                        vm.updateIsCooked(if (state.isCooked == false) true else false)
                    },
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color(0xFFE61610),
                        uncheckedTrackColor = Color(0xFF840705),
                        checkedThumbColor = Color(0xFF72BB53),
                        checkedTrackColor = Color(0xFF4C7A34)
                    )
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
                    if (state.phone == null) "тел. +7хххххххххх" else "тел. ${state.phone}",
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
                    text = if (state.deadLine == null) "Дата выполнения: _._._ г." else "Дата выполнения: ${
                        state.deadLine?.format(
                            "dd.MM.yyyy"
                        )
                    }",
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
                    if (state.address == null) "Адрес доставки не указан" else "Адрес: ${state.address}",
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

            if (state.products?.isNotEmpty() == true) state.products?.forEach { product ->
                    OrderProduct(product = product)
                }

            Row(
                Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Себестоимость: ${state.costPrice} руб.",
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
                    "Отсутствуют продукты: \n${state.missingIngredients}",
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

@Composable
fun OrderProduct(product: Product) {
    Column(
        modifier = Modifier

            .background(color = MaterialTheme.colors.background)
    ) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.title,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(Modifier.weight(1f))
            Column() {
                Text(
                    text = "${product.weight} ${product.units}",
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${product.price} руб.",
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
        Divider(color = MaterialTheme.colors.secondary)
    }
}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun OrderToolBar(
    navController: NavController,
    vm: OrderViewModel = viewModel(LocalContext.current as RootActivity, key = "order")
){
    val state by vm.state.collectAsState()

    TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
        IconButton(onClick = { navController.navigate(Screen.Orders.route) }) {
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
}


/*
@Preview
@Composable
fun previewOrder() {
    AppTheme {
        OrderScreen()
    }
}*/
