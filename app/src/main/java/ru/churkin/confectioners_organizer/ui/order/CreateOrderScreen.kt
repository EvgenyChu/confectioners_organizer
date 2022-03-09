package ru.churkin.confectioners_organizer.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.date.format
import ru.churkin.confectioners_organizer.ui.date_picker.DatePicker
import ru.churkin.confectioners_organizer.view_models.order.data.CreateOrderViewModel

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

    var needDelivery by remember { mutableStateOf("Отсутствует") }
    val paidOrder = remember { mutableStateOf("Заказ не оплачен") }
    val checkedState = remember { mutableStateOf(false) }

    val colors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.onPrimary,
        backgroundColor = MaterialTheme.colors.background,
        disabledTextColor = MaterialTheme.colors.background,
        placeholderColor = MaterialTheme.colors.background,
        disabledPlaceholderColor = MaterialTheme.colors.background,
        focusedIndicatorColor = MaterialTheme.colors.secondary,
        cursorColor = MaterialTheme.colors.onPrimary
    )
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
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.subtitle1,
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
                    text = needDelivery,
                    style = MaterialTheme.typography.subtitle1
                )

                Spacer(Modifier.weight(1f, true))

                Switch(
                    checked = state.needDelivery,
                    onCheckedChange = {
                        vm.updateNeedDelivery(it)
                        if (state.needDelivery) needDelivery = "Без доставки"
                        else needDelivery = "Доставка"
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
                enabled = !state.needDelivery,
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

            TextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.subtitle1,
                placeholder = {
                    Text(
                        "Добавьте изделие",
                        style = MaterialTheme.typography.subtitle2,
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                            tint = MaterialTheme.colors.secondary,
                            contentDescription = "Добавить изделие"
                        )
                    }
                },
                colors = colors
            )


            TextField(
                value = "${if (state.price == 0) "" else state.price}",
                onValueChange = { vm.updatePrice(it.toInt()) },
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
                    text = paidOrder.value,
                    style = MaterialTheme.typography.subtitle1,
                )

                Spacer(Modifier.weight(1f, true))

                Switch(
                    checked = state.isPaid,
                    onCheckedChange = {
                        vm.updateIsPaid(it)
                        if (checkedState.value) paidOrder.value = "Заказ оплачен"
                        else paidOrder.value = "Заказ не оплачен"
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
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.secondary
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_done_24),
                contentDescription = "Добавить"
            )
        }
    }
}