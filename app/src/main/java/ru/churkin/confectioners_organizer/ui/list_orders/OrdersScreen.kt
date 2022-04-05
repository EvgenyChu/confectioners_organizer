package ru.churkin.confectioners_organizer.ui.list_orders

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.InternalCoroutinesApi
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.RootActivity
import ru.churkin.confectioners_organizer.date.format
import ru.churkin.confectioners_organizer.items.*
import ru.churkin.confectioners_organizer.local.db.entity.Order
import ru.churkin.confectioners_organizer.ui.date_picker.DatePicker
import ru.churkin.confectioners_organizer.view_models.list_orders.OrdersState
import ru.churkin.confectioners_organizer.view_models.list_orders.OrdersViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(InternalCoroutinesApi::class)
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun OrdersScreen(
    navController: NavController,
    vm: OrdersViewModel = viewModel(LocalContext.current as RootActivity, key = "orders"),
) {

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
        ) {

            when (val listState = state) {

                is OrdersState.Empty -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize(1f)
                    ) {
                        Text("Не найдено")
                    }
                }
                is OrdersState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize(1f)
                            .background(color = MaterialTheme.colors.background)
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colors.secondary)
                    }
                }

                is OrdersState.Value -> {
                    LazyColumn(contentPadding = PaddingValues(bottom = 56.dp)) {
                        items(listState.orders, { it.id }) { item ->

                            SwipeItem(
                                onDismiss = { vm.removeOrder(item.id) },
                            ) {
                                OrderItem(order = item, onClick = { id ->
                                    navController.navigate("orders/$id")
                                })
                            }
                        }
                    }
                }
            }
        }

        ParamsBottomBar(
            text = "Есть время! Нет заказов)",
            icons = listOf(
                Icon(
                    icon = R.drawable.ic_baseline_alarm_on_24,
                    tint = MaterialTheme.colors.onPrimary
                )
            )
        )

        MainButton(
            tailIcon = if (vm.isShowDate.value) R.drawable.ic_baseline_arrow_back_24
            else R.drawable.ic_baseline_add_24,
            modifier = Modifier.align(alignment = Alignment.BottomEnd)
        ) {
            if (!vm.isShowDate.value) navController.navigate("orders/create")
            else {
                vm.currentOrdersState()
                vm.isShowDate.value = false
            }
        }
    }
}

@Composable
fun OrderItem(order: Order, onClick: (Long) -> Unit) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(order.id) }
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Icon(
                    modifier = Modifier.padding(16.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_cake_24),
                    tint = if (order.availabilityProduct) colorResource(id = R.color.green)
                    else colorResource(id = R.color.red),
                    contentDescription = "Лейбл"
                )
            }
            Column() {
                Spacer(modifier = Modifier.padding(top = 8.dp))
                Row(modifier = Modifier.padding(end = 16.dp)) {
                    Text(
                        text = if (order.customer == "") "ФИО" else order.customer,
                        style = MaterialTheme.typography.caption,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        modifier = Modifier.padding(end = 16.dp),
                        painter = painterResource(id = R.drawable.ic_baseline_delivery_dining_24),
                        tint = if (order.needDelivery) colorResource(id = R.color.green)
                        else colorResource(id = R.color.red),
                        contentDescription = "Знак доставки"
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_monetization_on_24),
                        tint = if (order.isPaid) colorResource(id = R.color.green)
                        else colorResource(id = R.color.red),
                        contentDescription = "Выполнение заказа"
                    )
                }
                Spacer(modifier = Modifier.padding(top = 8.dp))
                Row(modifier = Modifier.padding(end = 16.dp)) {
                    Text(
                        text = if (order.phone == null) "+7xxxxxxxxxx" else "${order.phone}",
                        style = MaterialTheme.typography.caption,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = " ${order.price} руб. ",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .background(
                                color = Color.Yellow,
                                RoundedCornerShape(11.dp)
                            ),
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.padding(top = 16.dp))
                Row(modifier = Modifier.padding(end = 16.dp)) {
                    Text(
                        text = order.availableProducts,
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.caption,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = order.deadline?.format() ?: "",
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.caption
                    )
                }
                Spacer(modifier = Modifier.padding(top = 8.dp))
            }
        }
        Divider(color = MaterialTheme.colors.secondary)
    }
}


@ExperimentalMaterialApi
@InternalCoroutinesApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OrdersToolBar(
    vm: OrdersViewModel = viewModel(LocalContext.current as RootActivity, key = "orders"),
    onMenuClick: () -> Unit
) {

    val searchText by vm.searchText.collectAsState()
    var isShowDatePicker by remember { mutableStateOf(false) }

    SearchToolBar(
        searchText = searchText,
        actions = listOf(
            ToolBarAction(
                icon = R.drawable.ic_baseline_calendar_month_24,
                action = { isShowDatePicker = true }
            )),
        onNavigate = { onMenuClick() },
        onSearch = { vm.searchOrders(it) },
        onSubmit = {
            vm.searchOrders(it)
            vm.isShowDate.value = false
        },
        onSearchDismiss = { vm.isShowDate.value = false },
    )
    if (isShowDatePicker) DatePicker(
        onSelect = {
            vm.searchDeadLine(it)
            vm.isShowDate.value = true
            isShowDatePicker = false
        },
        onDismiss = {
            isShowDatePicker = false
        })
}

