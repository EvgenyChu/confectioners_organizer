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
import ru.churkin.confectioners_organizer.items.ParamsActionItem
import ru.churkin.confectioners_organizer.items.ParamsSwitchItem
import ru.churkin.confectioners_organizer.items.ParamsTextItem
import ru.churkin.confectioners_organizer.items.ParamsToolBar
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

            ParamsSwitchItem(
                text = if (!state.isCooked) "Заказ в работе" else "Заказ выполнен",
                value = state.isCooked
            ){
                vm.updateIsCooked(if (it == false) true else false)
            }

            Divider(color = MaterialTheme.colors.secondary)

            ParamsTextItem("Заказчик: ${state.customer}")

            Divider(color = MaterialTheme.colors.secondary)

            ParamsTextItem(
                if (state.phone == null) "тел. +7хххххххххх" else "тел. ${state.phone}"
            )

            Divider(color = MaterialTheme.colors.secondary)

            ParamsTextItem(
                if (state.deadLine == null) "Дата выполнения: _._._ г." else "Дата выполнения: ${
                    state.deadLine?.format(
                        "dd.MM.yyyy"
                    )
                }"
            )

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

            ParamsTextItem(
                if (state.address == null) "Адрес доставки не указан" else "Адрес: ${state.address}"
            )

            Divider(color = MaterialTheme.colors.secondary)

            ParamsTextItem(
                "Список изделий для заказа:"
            )

            if (state.products?.isNotEmpty() == true) state.products?.forEach { product ->
                    OrderProduct(product = product)
                }

            ParamsTextItem(
                "Себестоимость: ${state.costPrice} руб."
            )

            Divider(color = MaterialTheme.colors.secondary)

            ParamsTextItem(
                "Цена заказа: ${state.price} руб."
            )

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

            ParamsTextItem(
                "Отсутствуют продукты: \n${state.missingIngredients}"
            )

            Divider(color = MaterialTheme.colors.secondary)

            ParamsTextItem(
                "Примечание: \n${state.note}"
            )

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

        ParamsActionItem(
            tailIcon = R.drawable.ic_baseline_done_24,
            modifier = Modifier.align(alignment = Alignment.BottomEnd)
        ) {
            navController.navigate(Screen.Orders.route)
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

    ParamsToolBar(
        text = "Заказ",
        onBackClick = { navController.navigate(Screen.Orders.route) },
        onEditClick = { navController.navigate("orders/edit/${state.id}") }
    )
}


/*
@Preview
@Composable
fun previewOrder() {
    AppTheme {
        OrderScreen()
    }
}*/
