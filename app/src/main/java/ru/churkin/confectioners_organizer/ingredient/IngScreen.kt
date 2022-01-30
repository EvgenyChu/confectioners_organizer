package ru.churkin.confectioners_organizer.ingredient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.ui.theme.AppTheme
import java.util.*

@Composable
fun IngScreen() {
    AppTheme() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
        ) {

            Column(
                Modifier
                    .fillMaxSize()
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
                        "Новый ингредиент",
                        style = MaterialTheme.typography.body1,
                    )
                    Spacer(Modifier.weight(1f, true))

                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Очистить"
                        )
                    }
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colors.secondary
                )
                TextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier
                        .height(56.dp),
                    textStyle = MaterialTheme.typography.body1,
                    placeholder = {
                        Text(
                            "Наименование",
                            style = MaterialTheme.typography.body2
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onPrimary,
                        backgroundColor = MaterialTheme.colors.background
                    )
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colors.secondary
                )

                TextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.body1,
                    placeholder = {
                        Text(
                            "Наличие ингредиента",
                            style = MaterialTheme.typography.body2,
                        )
                    },
                    leadingIcon = {
                        IconButton(onClick = { }){
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_check_circle),
                                tint = MaterialTheme.colors.secondaryVariant,
                                contentDescription = "Наличие"
                            )
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onPrimary,
                        backgroundColor = MaterialTheme.colors.background
                    )
                )
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
                ) {
                    TextField(
                        value = "",
                        onValueChange = { },
                        Modifier.weight(3f),
                        textStyle = MaterialTheme.typography.body1,
                        placeholder = {
                            Text(
                                "Количество,",
                                style = MaterialTheme.typography.body2
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colors.onPrimary,
                            backgroundColor = MaterialTheme.colors.background
                        )
                    )
                    //   Spacer(modifier = Modifier.fillMaxWidth())
                    Box(
                        Modifier
                            .height(56.dp)
                            .weight(1f)
                    ) {
                        Text(
                            "ед. изм.",
                            modifier = Modifier.padding(top = 16.dp),
                            style = MaterialTheme.typography.body2
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_down_24),
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier
                                .padding(top = 14.dp)
                                .weight(1f),
                            contentDescription = "Выбор ед.изм."
                        )
                    }
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
                ) {
                    TextField(
                        modifier = Modifier.weight(3f),
                        value = "",
                        onValueChange = { },
                        textStyle = MaterialTheme.typography.body1,
                        placeholder = {
                            Text(
                                "Цена,",
                                style = MaterialTheme.typography.body2
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colors.onPrimary,
                            backgroundColor = MaterialTheme.colors.background
                        )
                    )

                    Box(
                        Modifier
                            .height(56.dp)
                            .weight(2f)
                    ) {
                        Text(
                            "рубль за ______",
                            modifier = Modifier.padding(top = 16.dp),
                            style = MaterialTheme.typography.body2
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_down_24),
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier
                                .padding(top = 14.dp)
                                .weight(1f),
                            contentDescription = "Выбор ед.изм."
                        )
                    }
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = MaterialTheme.colors.secondary
                )
                TextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.body1,
                    placeholder = {
                        Text(
                            "Годен до: дд/мм/гггг",
                            style = MaterialTheme.typography.body2
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { }){
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                                tint = MaterialTheme.colors.secondary,
                                contentDescription = "Наличие"
                            )
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onPrimary,
                        backgroundColor = MaterialTheme.colors.background
                    )
                )
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

                    Text(
                        "Жаль, что продукты кончаются)",
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
fun previewIng() {
    AppTheme {
        IngScreen()
    }
}