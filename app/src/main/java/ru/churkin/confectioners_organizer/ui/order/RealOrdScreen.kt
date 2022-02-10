package ru.churkin.confectioners_organizer.listOrders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.date.format
import ru.churkin.confectioners_organizer.ingredient.IngsCard
import ru.churkin.confectioners_organizer.listRecepts.RecsCard
import ru.churkin.confectioners_organizer.view_models.order.data.Order
import ru.churkin.confectioners_organizer.ui.theme.AppTheme
import ru.churkin.confectioners_organizer.view_models.ingredient.data.Ingredient
import java.util.*

@Composable
fun RealOrdScreen(order: Order) {

    val textOrder = remember { mutableStateOf("Заказ не выполнен")}
    val checkedState = remember { mutableStateOf(false) }
    order.implementation = checkedState.value

    AppTheme() {
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
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Назад"
                        )
                    }

                    Text(
                        text = textOrder.value,
                        style = MaterialTheme.typography.h6,
                    )
                    Spacer(Modifier.weight(1f, true))

                    Switch(
                        checked = checkedState.value,
                        onCheckedChange = { checkedState.value = it
                                          if (checkedState.value) textOrder.value = "Заказ выполнен"
                                          else  textOrder.value = "Заказ не выполнен"},
                        colors = SwitchDefaults.colors(
                            uncheckedThumbColor = Color(0xFFE61610),
                            uncheckedTrackColor = Color(0xFF840705),
                            checkedThumbColor = Color(0xFF72BB53),
                            checkedTrackColor = Color(0xFF4C7A34)
                        )
                    )

                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Очистить"
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = order.customer,
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = order.phone ?: "телефон не указан",
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "День сдачи заказа: ${order.deadline.format("dd.mm.yyyy")}",
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_check_circle),
                        tint = if (order.delivery) MaterialTheme.colors.secondary
                        else MaterialTheme.colors.secondaryVariant,
                        contentDescription = "Доставка"
                    )
                    Text(
                        text = order.addresss ?: "Доставка не требуется",
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Бисквит",
                        modifier = Modifier
                            .padding(start = 16.dp),
                        style = MaterialTheme.typography.subtitle1,
                    )
                }

                RecsCard()

                IngsCard(ingredient = Ingredient())

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Стоимость заказа: ${order.price} руб.",
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = order.note ?: "Примечание",
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
            }


            Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {

                BottomAppBar(
                    backgroundColor = MaterialTheme.colors.primary,
                    modifier = Modifier.height(56.dp)
                ) {

                    Text(
                        "Это самый лучший заказ)",
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
                    painter = painterResource(id = R.drawable.ic_baseline_check_circle_outline_24),
                    modifier = Modifier
                        .size(64.dp),
                    contentDescription = "Добавить",
                    tint = MaterialTheme.colors.secondary
                )
            }
        }
    }
}

@Preview
@Composable
fun previewRealOrder() {
    AppTheme {
        RealOrdScreen(
            order = Order.makeOrder(
                customer = "Лукьянов Владимир Александрович",
                phone = "+79272636468",
                deadline = Date(),
                delivery = false,
                addresss = "г. Тольятти, ул. Комсомольская 79, кв. 37",
                product = listOf(),
                price = 3000,
                paid = true,
                note = "Нужна сдача",
                implementation = false
            )
        )
    }
}