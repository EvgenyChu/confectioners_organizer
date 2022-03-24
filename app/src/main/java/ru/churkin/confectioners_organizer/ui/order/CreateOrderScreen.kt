package ru.churkin.confectioners_organizer.ui.order

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.date.format
import ru.churkin.confectioners_organizer.local.db.entity.Product
import ru.churkin.confectioners_organizer.ui.date_picker.DatePicker
import ru.churkin.confectioners_organizer.ui.recept.ReceptIngItem
import ru.churkin.confectioners_organizer.view_models.order.data.CreateOrderViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateOrderScreen(navController: NavController, vm: CreateOrderViewModel = viewModel()) {

    val state by vm.state.collectAsState()

    val isCreate: Boolean by remember {
        mutableStateOf(navController.currentDestination?.route == "orders/create")
    }

    val title by remember {
        mutableStateOf(
            if (isCreate) "Новый заказ" else "Редактирование"
        )
    }

    var isShowDatePicker by remember { mutableStateOf(false) }

    var calculate by remember { mutableStateOf(false)}

    val colors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.onPrimary,
        backgroundColor = MaterialTheme.colors.background,
        disabledTextColor = MaterialTheme.colors.background,
        placeholderColor = MaterialTheme.colors.background,
        disabledPlaceholderColor = MaterialTheme.colors.background,
        focusedIndicatorColor = MaterialTheme.colors.secondary,
        cursorColor = MaterialTheme.colors.onPrimary
    )
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
                IconButton(onClick = {
                    navController.popBackStack()
                    if (isCreate) vm.removeOrder(state.id)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        tint = MaterialTheme.colors.onPrimary,
                        contentDescription = "Назад"
                    )
                }
                Text(
                    title,
                    style = MaterialTheme.typography.h6,
                )
                Spacer(Modifier.weight(1f, true))

                IconButton(onClick = { vm.emptyState() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                        tint = MaterialTheme.colors.onPrimary,
                        contentDescription = "Очистить"
                    )
                }
            }

            TextField(
                value = state.customer,
                onValueChange = { vm.updateCustomer(it) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.subtitle1,
                placeholder = {
                    Text(
                        "ФИО заказчика",
                        style = MaterialTheme.typography.subtitle2,
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_search_24),
                            tint = MaterialTheme.colors.secondary,
                            contentDescription = "Поиск контактов"
                        )
                    }
                },
                colors = colors
            )

            TextField(
                value = state.phone ?: "",
                onValueChange = { vm.updatePhone(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.subtitle1,
                placeholder = {
                    Text(
                        "Телефон заказчика",
                        style = MaterialTheme.typography.subtitle2
                    )
                },
                colors = colors
            )

            TextField(
                value = state.deadLine?.format() ?: "",
                onValueChange = { vm.updateDeadLine(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isShowDatePicker = true },
                textStyle = MaterialTheme.typography.subtitle1,
                enabled = false,
                placeholder = {
                    Text(
                        "дд/мм/гггг",
                        style = MaterialTheme.typography.subtitle2,
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { isShowDatePicker = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                            tint = MaterialTheme.colors.secondary,
                            contentDescription = "Календарь"
                        )
                    }
                    if (isShowDatePicker) DatePicker(
                        onSelect = {
                            vm.updateDeadLine(it)
                            isShowDatePicker = false
                        },
                        onDismiss = { isShowDatePicker = false })
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onPrimary,
                    backgroundColor = MaterialTheme.colors.background
                )
            )

            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (state.needDelivery) "Доставка" else "Без доставки",
                    style = MaterialTheme.typography.subtitle1
                )

                Spacer(Modifier.weight(1f, true))

                Switch(
                    checked = state.needDelivery,
                    onCheckedChange = {
                        vm.updateNeedDelivery(it)
                    },
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color(0xFFE61610),
                        uncheckedTrackColor = Color(0xFF840705),
                        checkedThumbColor = Color(0xFF72BB53),
                        checkedTrackColor = Color(0xFF4C7A34)
                    )
                )
            }

            Divider(
                color = MaterialTheme.colors.surface
            )

            TextField(
                value = state.address ?: "",
                onValueChange = { vm.updateAddress(it) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.subtitle1,
                enabled = state.needDelivery,
                placeholder = {
                    Text(
                        "Адрес доставки",
                        style = MaterialTheme.typography.subtitle2,
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_location_on_24),
                            tint = MaterialTheme.colors.secondary,
                            contentDescription = "Геолокация"
                        )
                    }
                },
                colors = colors
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clickable {
                        navController.navigate("orders/${state.id}/products/create")
                    }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Добавьте изделие, кол-во",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.subtitle2
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                    tint = MaterialTheme.colors.secondary,
                    contentDescription = "Наличие"
                )
            }

            if (state.products?.isNotEmpty() == true) {
                Box(modifier = Modifier.heightIn(0.dp, 3000.dp)){
                    LazyColumn() {
                        items(state.products!!.sortedBy { it.title }, { it.id }) { item ->

                            val dismissState = rememberDismissState()

                            if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                                vm.removeOrderProduct(item.id)
                            }
                            SwipeToDismiss(
                                state = dismissState,
                                directions = setOf(
                                    DismissDirection.StartToEnd
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
                                    OrderProductItem(product = item) {
                                        navController.navigate("orders/${item.orderId}/products/${item.id}")
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Row(
                Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable {
                        vm.updateCostPrice() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Себестоимость: ${state.costPrice} руб.",
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(Modifier.weight(1f, true))
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_calculate_24),
                    tint = MaterialTheme.colors.secondary,
                    contentDescription = "Калькулятор"
                )
            }

            TextField(
                value = "${if (state.price == 0) "" else state.price}",
                onValueChange = { vm.updatePrice(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.subtitle1,
                label = {
                    Text(
                        "Стоимость заказа, руб.",
                        style = MaterialTheme.typography.subtitle2
                    )
                },
                colors = colors
            )

            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (state.isPaid) "Заказ оплачен" else "Заказ не оплачен",
                    style = MaterialTheme.typography.subtitle1,
                )

                Spacer(Modifier.weight(1f, true))

                Switch(
                    checked = state.isPaid,
                    onCheckedChange = {
                        vm.updateIsPaid(it)
                    },
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color(0xFFE61610),
                        uncheckedTrackColor = Color(0xFF840705),
                        checkedThumbColor = Color(0xFF72BB53),
                        checkedTrackColor = Color(0xFF4C7A34)
                    )
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = MaterialTheme.colors.surface
            )

            TextField(
                value = state.note ?: "",
                onValueChange = { vm.updateNote(it) },
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.subtitle1,
                placeholder = {
                    Text(
                        "Примечание",
                        style = MaterialTheme.typography.subtitle2
                    )
                },
                colors = colors
            )
            Spacer(modifier = Modifier.height(56.dp))
        }


        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {

            BottomAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.height(56.dp)
            ) {

                Text(
                    "Новый заказ - новая история)",
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.body1
                )

            }
        }
        FloatingActionButton(
            onClick = {
                vm.addOrder()
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
fun OrderProductItem(product: Product, onClick: (id: Long) -> Unit) {
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
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { onClick(product.id) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                    tint = MaterialTheme.colors.secondary,
                    contentDescription = "Очистить"
                )
            }
        }
        Divider(color = MaterialTheme.colors.secondary)
    }
}