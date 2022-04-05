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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.InternalCoroutinesApi
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.RootActivity
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.date.format
import ru.churkin.confectioners_organizer.items.*
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

            ToggleItem(
                text = if (!state.isCooked) "Заказ в работе" else "Заказ выполнен",
                value = state.isCooked,
                onValueChange = { vm.updateIsCooked(it) }
            )

            Divider(color = MaterialTheme.colors.secondary)

            TextItem("Заказчик: ${state.customer}")

            Divider(color = MaterialTheme.colors.secondary)

            TextItem(
                if (state.phone == null) "тел. +7хххххххххх" else "тел. ${state.phone}"
            )

            Divider(color = MaterialTheme.colors.secondary)

            TextItem(
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

            ParamsChoiceItem(
            tintIcon = state.needDelivery,
            text = if (state.needDelivery) "Доставка" else "Без доставки"
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = MaterialTheme.colors.secondary
            )

            TextItem(
                if (state.address == null) "Адрес доставки не указан" else "Адрес: ${state.address}"
            )

            Divider(color = MaterialTheme.colors.secondary)

            TextItem(
                "Список изделий для заказа:"
            )

            if (state.products?.isNotEmpty() == true) state.products?.forEach { product ->
                    OrderProduct(product = product)
                }

            TextItem(
                "Себестоимость: ${state.costPrice} руб."
            )

            Divider(color = MaterialTheme.colors.secondary)

            TextItem(
                "Цена заказа: ${state.price} руб."
            )

            Divider(color = MaterialTheme.colors.secondary)

            ParamsChoiceItem(
                tintIcon = state.isPaid,
                text = if (state.isPaid) "Заказ оплачен" else "Заказ не оплачен",
                content = "Оплата"
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = MaterialTheme.colors.secondary
            )

            TextItem(
                "Отсутствуют продукты: \n${state.missingIngredients}"
            )

            Divider(color = MaterialTheme.colors.secondary)

            TextItem(
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

        MainButton(
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
