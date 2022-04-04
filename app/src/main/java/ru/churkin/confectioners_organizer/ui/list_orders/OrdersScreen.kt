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
import ru.churkin.confectioners_organizer.items.ParamsSwipeItem
import ru.churkin.confectioners_organizer.local.db.entity.Order
import ru.churkin.confectioners_organizer.ui.date_picker.DatePicker
import ru.churkin.confectioners_organizer.ui.list_recepts.SearchBar
import ru.churkin.confectioners_organizer.view_models.list_orders.OrdersState
import ru.churkin.confectioners_organizer.view_models.list_orders.OrdersViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(InternalCoroutinesApi::class)
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun OrdersScreen(
    navController: NavController,
    vm: OrdersViewModel = viewModel(LocalContext.current as RootActivity,key = "orders"),
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

                            ParamsSwipeItem(
                                onDismiss = { vm.removeOrder(item.id) },
                            ){
                                OrderItem(order = item, onClick = { id ->
                                    navController.navigate("orders/$id")
                                })
                            }
                        }
                    }
                }
            }
        }
        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {

            BottomAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.height(56.dp)
            ) {
                Icon(
                    modifier = Modifier.padding(start = 16.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_alarm_on_24),
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = "Будильник"
                )
                Text(
                    "Есть время! Нет заказов)",
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.body1
                )

            }
        }
        FloatingActionButton(
            onClick = {
                if (!vm.isShowDate.value) navController.navigate("orders/create")
                else {
                    vm.currentOrdersState()
                    vm.isShowDate.value = false
                }
            },
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(bottom = 28.dp, end = 16.dp),
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary
        ) {
            Icon(
                painter = if (vm.isShowDate.value) painterResource(id = R.drawable.ic_baseline_arrow_back_24)
                else painterResource(id = R.drawable.ic_baseline_add_24),
                contentDescription = "Добавить"
            )
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
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = MaterialTheme.colors.secondary
        )
    }
}


@ExperimentalMaterialApi
@InternalCoroutinesApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OrdersToolBar(
    vm: OrdersViewModel= viewModel(LocalContext.current as RootActivity, key = "orders"),
            onMenuClick: ()-> Unit
){

    val searchText by vm.searchText.collectAsState()
    var isShowSearch by remember { mutableStateOf(false) }
    var isShowDatePicker by remember { mutableStateOf(false) }

    TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
        if (isShowSearch) {
            SearchBar(
                searchText = searchText,
                onSearch = { vm.searchOrders(it) },
                onSubmit = {
                    vm.searchOrders(it)
                    isShowSearch = false
                    vm.isShowDate.value = false
                },
                onDismiss = {
                    isShowSearch = false
                    vm.isShowDate.value = false
                })
        } else {
            IconButton(onClick = {
                    onMenuClick()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_dehaze_24),
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = "Меню навигации"
                )
            }
            Text(
                "Список заказов",
                style = MaterialTheme.typography.h6,
            )
            Spacer(Modifier.weight(1f, true))

            IconButton(onClick = { isShowDatePicker = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = "Календарь"
                )
                if (isShowDatePicker) DatePicker(
                    onSelect = {
                        vm.searchDeadLine(it)
                        isShowDatePicker = false
                        vm.isShowDate.value = true
                    },
                    onDismiss = {
                        isShowDatePicker = false
                    })
            }

            IconButton(onClick = { isShowSearch = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_search_24),
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = "Найти"
                )
            }
        }
    }
}


/*
@Preview
@Composable
fun previewOrdersCard() {
    AppTheme {
        OrdersCard()
    }
}

@Preview
@Composable
fun previewOrders() {
    AppTheme {
        OrdersScreen()
    }
}*/
