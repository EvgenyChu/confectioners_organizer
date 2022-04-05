package ru.churkin.confectioners_organizer.ui.order

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
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
import ru.churkin.confectioners_organizer.ui.date_picker.DatePicker
import ru.churkin.confectioners_organizer.view_models.order.data.CreateOrderViewModel

@InternalCoroutinesApi
@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateOrderScreen(
    navController: NavController,
    id: Long?,
    vm: CreateOrderViewModel = viewModel(LocalContext.current as RootActivity, key = "create_order")
) {

    val state by vm.state.collectAsState()

    var isShowDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        Log.e("CreateOrderScreen", "$id")
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

            EditTextItem(
                value = state.customer,
                onValueChange = { vm.updateCustomer(it) },
                label = "ФИО заказчика"
            )

            EditTextItem(
                value = state.phone ?: "",
                onValueChange = { vm.updatePhone(it) },
                label = "Телефон заказчика",
                inputType = KeyboardType.Phone
            )

            EditTextItem(
                value = state.deadLine?.format() ?: "",
                modifier = Modifier
                    .clickable { isShowDatePicker = true },
                enabled = false,
                onValueChange = { vm.updateDeadLine(it) },
                label = "дата выполнения: дд/мм/гггг",
                actions = listOf(
                    IconButton(
                        icon = R.drawable.ic_baseline_calendar_month_24,
                        action = { isShowDatePicker = true },
                        tint = MaterialTheme.colors.secondary
                    )
                )
            )

            if (isShowDatePicker) DatePicker(
                onSelect = {
                    vm.updateDeadLine(it)
                    isShowDatePicker = false
                },
                onDismiss = { isShowDatePicker = false })

            ToggleItem(
                text = if (state.needDelivery) "Доставка" else "Без доставки",
                value = state.needDelivery,
                onValueChange = { vm.updateNeedDelivery(it) }
            )

            Divider(
                color = MaterialTheme.colors.surface
            )

            EditTextItem(
                value = state.address ?: "",
                onValueChange = { vm.updateAddress(it) },
                label = "Адрес доставки"
            )

            AdditableItem(
                onTailClick = {
                    navController.navigate("create_orders/${state.id}/products/create")
                    vm.addOrder()
                },
                text = "Добавьте изделие, кол-во"
            )

            if (state.products?.isNotEmpty() == true) {
                Box(modifier = Modifier.heightIn(0.dp, 3000.dp)) {
                    LazyColumn() {
                        items(state.products!!.sortedBy { it.title }, { it.id }) { item ->

                            SwipeItem(
                                onDismiss = { vm.removeOrderProduct(item.id) },
                            ) {
                                OrderProductItem(product = item) {
                                    navController.navigate("create_orders/${item.orderId}/products/${item.id}")
                                    vm.addOrder()
                                }
                            }
                        }
                    }
                }
            }

            AdditableItem(
                modifier = Modifier.height(56.dp),
                onTailClick = { vm.updateCostPrice() },
                text = "Себестоимость: ${state.costPrice} руб.",
                style = MaterialTheme.typography.subtitle1,
                icon = R.drawable.ic_baseline_calculate_24
            )

            EditTextItem(
                value = "${if (state.price == 0) "" else state.price}",
                onValueChange = { vm.updatePrice(it) },
                label = "Стоимость заказа, руб.",
                inputType = KeyboardType.Number
            )

            ToggleItem(
                text = if (state.isPaid) "Заказ оплачен" else "Заказ не оплачен",
                value = state.isPaid
            ) {
                vm.updateIsPaid(it)
            }

            Divider(color = MaterialTheme.colors.surface)

            EditTextItem(
                value = state.note ?: "",
                onValueChange = { vm.updateNote(it) },
                label = "Примечание"
            )

            Spacer(modifier = Modifier.height(56.dp))
        }

        ParamsBottomBar(
            text = "Новый заказ - новая история)"
        )

        MainButton(
            tailIcon = R.drawable.ic_baseline_done_24,
            modifier = Modifier.align(alignment = Alignment.BottomEnd)
        ) {
            vm.addOrder()
            navController.navigate(Screen.Orders.route)
        }
    }
}

@Composable
fun OrderProductItem(product: Product, onClick: (id: Long) -> Unit) {
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
    ) {
        TextItem(
            text = "${product.title} ${product.weight} ${product.units} \n ${product.price} руб.",
            spacer = listOf(RowSpacer(modifier = Modifier.weight(1f, true))),
            icons = listOf(
                Icon(
                    icon = R.drawable.ic_baseline_edit_24,
                    modifier = Modifier.clickable { onClick(product.id) },
                    tint = MaterialTheme.colors.secondary
                )
            )
        )
        Divider(color = MaterialTheme.colors.secondary)
    }
}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun CreateOrderToolBar(
    navController: NavController,
    vm: CreateOrderViewModel = viewModel(LocalContext.current as RootActivity, key = "create_order")
) {
    val state by vm.state.collectAsState()

    ParamsToolBar(
        text = "Новый заказ",
        editIcon = R.drawable.ic_baseline_delete_24,
        onBackClick = {
            navController.navigate(Screen.Orders.route)
            vm.removeOrder(state.id)
        },
        onEditClick = { vm.emptyState() }
    )
}