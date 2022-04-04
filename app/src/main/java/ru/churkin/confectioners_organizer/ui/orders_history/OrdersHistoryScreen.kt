package ru.churkin.confectioners_organizer.history

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import ru.churkin.confectioners_organizer.items.ParamsActionItem
import ru.churkin.confectioners_organizer.items.ParamsSwipeItem
import ru.churkin.confectioners_organizer.ui.date_picker.DatePicker
import ru.churkin.confectioners_organizer.ui.list_orders.OrderItem
import ru.churkin.confectioners_organizer.ui.list_recepts.SearchBar
import ru.churkin.confectioners_organizer.view_models.list_orders.OrdersState
import ru.churkin.confectioners_organizer.view_models.orders_history.OrdersHistoryViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@InternalCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun OrdersHistoryScreen(
    navController: NavController,
    vm: OrdersHistoryViewModel = viewModel(LocalContext.current as RootActivity, key = "order_history"),
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
                        items(listState.orders.sortedBy { it.deadline }, { it.id }) { item ->

                            val dismissState = rememberDismissState()
                            if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                                vm.removeOrder(item.id)
                            }

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
                Text(
                    "Отличная история получилась)",
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.body1
                )

            }
        }

        ParamsActionItem(
            tailIcon = if (vm.isShowDate.value) R.drawable.ic_baseline_arrow_back_24
            else R.drawable.ic_baseline_add_24,
            modifier = Modifier.align(alignment = Alignment.BottomEnd)
        ) {
            if (!vm.isShowDate.value) navController.navigate("orders")
            else {
                vm.currentOrdersState()
                vm.isShowDate.value = false
            }
        }
    }
}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OrdersHistoryToolBar(
    vm: OrdersHistoryViewModel = viewModel(LocalContext.current as RootActivity, key = "order_history"),
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
                "История заказов",
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
fun previewHistory() {
    AppTheme {
       HistoryScreen()
    }
}*/
