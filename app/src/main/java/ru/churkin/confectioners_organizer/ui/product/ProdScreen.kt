package ru.churkin.confectioners_organizer.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

@Composable
fun ProductScreen() {
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
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Назад"
                        )
                    }
                    Text(
                        "Изделие для заказа",
                        style = MaterialTheme.typography.h6,
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

                TextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.subtitle1,
                    placeholder = {
                        Text(
                            "Название изделия",
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ){
                TextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier
                        .height(56.dp)
                        .weight(3f),
                    textStyle = MaterialTheme.typography.subtitle1,
                    placeholder = {
                        Text(
                            "Количество изделия",
                            style = MaterialTheme.typography.subtitle2
                        )
                    },
                    colors = colors
                )

                Box(
                    Modifier
                        .height(56.dp)
                        .weight(1f)
                ) {
                    Text(
                        "ед. изм.",
                        modifier = Modifier.padding(top = 16.dp),
                        style = MaterialTheme.typography.subtitle2
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

                TextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.subtitle1,
                    placeholder = {
                        Text(
                            "Добавить рецепт",
                            style = MaterialTheme.typography.subtitle2,
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { }){
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                                tint = MaterialTheme.colors.secondary,
                                contentDescription = "Добавить"
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
                            "Добавить ингредиент",
                            style = MaterialTheme.typography.subtitle2,
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { }){
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                                tint = MaterialTheme.colors.secondary,
                                contentDescription = "Добавить"
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
                            "Себестоимость, руб.",
                            style = MaterialTheme.typography.subtitle2,
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { }){
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_calculate_24),
                                tint = MaterialTheme.colors.secondary,
                                contentDescription = "Калькулятор"
                            )
                        }
                    },
                    colors = colors
                )

                TextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth(),
                    textStyle = MaterialTheme.typography.subtitle1,
                    placeholder = {
                        Text(
                            "Стоимость изделия, руб.",
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
                        "Главное ничего не перепутать)",
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

@Composable
fun ProdRecCard() {
        Column(modifier = Modifier
            .background(color = MaterialTheme.colors.background)) {
            Row(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = MaterialTheme.colors.background),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(16.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_circle_24),
                    tint = colorResource(id = R.color.green),
                    contentDescription = "Наличие"
                )
                Text(
                    text = "Бисквит",
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "300 г.",
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = MaterialTheme.colors.secondary
            )
        }
    }

@Composable
fun ProdIngCard() {
        Column(modifier = Modifier
            .background(color = MaterialTheme.colors.background)) {
            Row(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(16.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_circle_24),
                    tint = colorResource(id = R.color.green),
                    contentDescription = "Наличие"
                )
                Text(
                    text = "Мука",
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "300 г.",
                    style = MaterialTheme.typography.subtitle1
                )
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
fun previewProduct() {
    AppTheme {
        ProductScreen()
    }
}

@Preview
@Composable
fun previewProductReceptCard() {
    AppTheme {
        ProdRecCard()
    }
}

@Preview
@Composable
fun previewProductIngredientCard() {
    AppTheme {
        ProdIngCard()
    }
}*/
