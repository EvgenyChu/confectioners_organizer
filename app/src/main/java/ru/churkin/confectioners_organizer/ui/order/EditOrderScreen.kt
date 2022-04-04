package ru.churkin.confectioners_organizer.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.InternalCoroutinesApi
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.RootActivity
import ru.churkin.confectioners_organizer.date.format
import ru.churkin.confectioners_organizer.items.*
import ru.churkin.confectioners_organizer.ui.date_picker.DatePicker
import ru.churkin.confectioners_organizer.view_models.order.data.EditOrderViewModel

@InternalCoroutinesApi
@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditOrderScreen(
    navController: NavController,
    id: Long?,
    vm: EditOrderViewModel = viewModel(LocalContext.current as RootActivity, key = "edit_order")
) {

    val state by vm.state.collectAsState()

    var isShowDatePicker by remember { mutableStateOf(false) }

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

            TextField(
                value = state.customer,
                onValueChange = { vm.updateCustomer(it) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.subtitle1,
                label = {
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

            ParamsTextFieldItem(
                value = state.phone ?: "",
                onValueChange = { vm.updatePhone(it) },
                label = "Телефон заказчика",
                inputType = KeyboardType.Phone
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
                        "дата выполнения: дд/мм/гггг",
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
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onPrimary,
                    backgroundColor = MaterialTheme.colors.background
                )
            )

            ParamsSwitchItem(
                text = if (state.needDelivery) "Доставка" else "Без доставки",
                value = state.needDelivery
            ) {
                vm.updateNeedDelivery(state.needDelivery)
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

            ParamsAddItem(
                onTailClick = {
                    navController.navigate("orders/${state.id}/products/create")
                    vm.addOrder()
                },
                text = "Добавьте изделие, кол-во"
            )

            if (state.products?.isNotEmpty() == true) {
                Box(modifier = Modifier.heightIn(0.dp, 3000.dp)){
                    LazyColumn() {
                        items(state.products!!.sortedBy { it.title }, { it.id }) { item ->

                            ParamsSwipeItem(
                                onDismiss = { vm.removeOrderProduct(item.id) },
                            ){
                                OrderProductItem(product = item) {
                                    navController.navigate("orders/${item.orderId}/products/${item.id}")
                                    vm.addOrder()
                                }
                            }
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
                        vm.updateCostPrice()
                    },
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

            ParamsTextFieldItem(
                value = "${if (state.price == 0) "" else state.price}",
                onValueChange = { vm.updatePrice(it) },
                label = "Стоимость заказа, руб.",
                inputType = KeyboardType.Number
            )

            ParamsSwitchItem(
                text = if (state.isPaid) "Заказ оплачен" else "Заказ не оплачен",
                value = state.isPaid
            ) {
                vm.updateIsPaid(state.isPaid)
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = MaterialTheme.colors.surface
            )

            ParamsTextFieldItem(
                value = state.note ?: "",
                onValueChange = { vm.updateNote(it) },
                label = "Примечание"
            )

            Spacer(modifier = Modifier.height(56.dp))
        }

        if (isShowDatePicker) DatePicker(
            onSelect = {
                vm.updateDeadLine(it)
                isShowDatePicker = false
            },
            onDismiss = { isShowDatePicker = false })

        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {

            BottomAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.height(56.dp)
            ) {

                Text(
                    "Тут что-то не так?)",
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.body1
                )

            }
        }

        ParamsActionItem(
            tailIcon = R.drawable.ic_baseline_done_24,
            modifier = Modifier.align(alignment = Alignment.BottomEnd)
        ) {
            vm.addOrder()
            navController.navigate("orders/$id")
        }
    }
}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun EditOrderToolBar(
    navController: NavController,
    vm: EditOrderViewModel = viewModel(LocalContext.current as RootActivity, key = "edit_order")
){
    ParamsToolBar(
        text = "Редактирование",
        editIcon = R.drawable.ic_baseline_delete_24,
        onBackClick = {
            navController.popBackStack()
        },
        onEditClick = { vm.emptyState() }
    )
}