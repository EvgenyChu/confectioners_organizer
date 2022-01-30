package ru.churkin.confectioners_organizer.listOrders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.ui.theme.AppTheme

@Composable
fun OrdersScreen() {
    AppTheme() {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column(
                Modifier
                    .fillMaxSize()
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
                        style = MaterialTheme.typography.body1,
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
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colors.secondary
                )
            }
            Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colors.secondary
                )
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
                        style = MaterialTheme.typography.caption
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
}

@Composable
fun OrdersCard() {
    AppTheme() {
        Column() {
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
                            text = "Лукьянов Владимир Александрович",
                            style = MaterialTheme.typography.caption,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            modifier = Modifier.padding(end = 16.dp),
                            painter = painterResource(id = R.drawable.ic_baseline_delivery_dining_24),
                            tint = MaterialTheme.colors.onSecondary,
                            contentDescription = "Знак доставки"
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_circle_small_24),
                            tint = MaterialTheme.colors.onSecondary,
                            contentDescription = "Наличие"
                        )
                    }
                    Spacer(modifier = Modifier.padding(top = 8.dp))
                    Row(modifier = Modifier.padding(end = 16.dp)) {
                        Text(
                            text = "+79287456351",
                            style = MaterialTheme.typography.caption,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = " 6000 руб. ",
                            style = MaterialTheme.typography.h1,
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
                            text = "Торт, эклеры",
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.caption,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "31.01.2022",
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
}


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
}