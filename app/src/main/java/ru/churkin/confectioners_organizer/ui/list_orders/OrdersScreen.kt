package ru.churkin.confectioners_organizer.listOrders

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.date.format
import ru.churkin.confectioners_organizer.listRecepts.ReceptItem
import ru.churkin.confectioners_organizer.local.db.entity.Order
import ru.churkin.confectioners_organizer.local.db.entity.Recept
import ru.churkin.confectioners_organizer.ui.theme.AppTheme
import ru.churkin.confectioners_organizer.view_models.list_orders.OrdersState
import ru.churkin.confectioners_organizer.view_models.list_orders.OrdersViewModel
import ru.churkin.confectioners_organizer.view_models.list_recepts.ReceptsState
import java.lang.String.format

@ExperimentalMaterialApi
@Composable
fun OrdersScreen(navController: NavController, vm: OrdersViewModel = viewModel()) {

    val state by vm.state.collectAsState()

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
                    IconButton(onClick = { }) {
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

                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Календарь"
                        )
                    }

                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_search_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Найти"
                        )
                    }
                }
                when (val listState = state.ordersState) {

                    is OrdersState.Empty -> {}
                    is OrdersState.Loading -> {}

                    is OrdersState.Value -> {
                        LazyColumn(contentPadding = PaddingValues(bottom = 56.dp)) {
                            items(listState.orders, { it.id }) { item ->

                                val dismissState = rememberDismissState()
                                if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                                    vm.removeOrder(item.id)
                                }
                                SwipeToDismiss(
                                    state = dismissState,
                                    directions = setOf(
                                        DismissDirection.StartToEnd,
                                    ),
                                    background = {

                                        val color by animateColorAsState(
                                            when (dismissState.targetValue) {
                                                DismissValue.Default -> MaterialTheme.colors.surface
                                                else -> MaterialTheme.colors.secondary
                                            }
                                        )

                                        val icon = Icons.Default.Delete

                                        val scale by animateFloatAsState(targetValue = if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.2f)

                                        val alignment = Alignment.CenterStart


                                        Box(
                                            Modifier
                                                .fillMaxSize()
                                                .background(color)
                                                .padding(start = 16.dp, end = 16.dp),
                                            contentAlignment = alignment
                                        ) {
                                            Icon(
                                                icon,
                                                contentDescription = "icon",
                                                modifier = Modifier.scale(scale)
                                            )
                                        }
                                    },
                                    dismissContent = {
                                        OrderItem(order = item, onClick = { id ->
                                            navController.navigate("orders/$id")
                                        })
                                    }
                                )
                            }
                        }
                    }

                    is OrdersState.ValueWithMessage -> {}
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
                onClick = { },
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .padding(bottom = 28.dp, end = 16.dp),
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.secondary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_add_circle_24),
                    modifier = Modifier
                        .size(64.dp),
                    contentDescription = "Добавить",
                    tint = MaterialTheme.colors.secondary
                )
            }
        }
}

@Composable
fun OrderItem(order: Order, onClick: (Long) -> Unit) {
        Column(modifier = Modifier
            .background(color = MaterialTheme.colors.background)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.Center) {
                    Icon(
                        modifier = Modifier.padding(16.dp),
                        painter = painterResource(id = R.drawable.ic_baseline_cake_24),
                        tint = MaterialTheme.colors.secondary,
                        contentDescription = "Лейбл"
                    )
                }
                Column() {
                    Spacer(modifier = Modifier.padding(top = 8.dp))
                    Row(modifier = Modifier.padding(end = 16.dp)) {
                        Text(
                            text = order.customer,
                            style = MaterialTheme.typography.caption,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            modifier = Modifier.padding(end = 16.dp),
                            painter = painterResource(id = R.drawable.ic_baseline_delivery_dining_24),
                            tint = colorResource(id = R.color.green),
                            contentDescription = "Знак доставки"
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_circle_small_24),
                            tint = colorResource(id = R.color.green),
                            contentDescription = "Наличие"
                        )
                    }
                    Spacer(modifier = Modifier.padding(top = 8.dp))
                    Row(modifier = Modifier.padding(end = 16.dp)) {
                        Text(
                            text = "${order.phone}",
                            style = MaterialTheme.typography.caption,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = " ${order.price} руб. ",
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colors.secondary,
                                    RoundedCornerShape(11.dp)
                                ),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.padding(top = 16.dp))
                    Row(modifier = Modifier.padding(end = 16.dp)) {
                        Text(
                            text = "${order.product ?: ""}",
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
