package ru.churkin.confectioners_organizer.recept

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.ingredient.IngScreen
import ru.churkin.confectioners_organizer.ui.theme.AppTheme

@Composable
fun RecScreen() {
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
                        "Новый рецепт",
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
                            "Название рецепта",
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
                    modifier = Modifier
                        .height(56.dp),
                    textStyle = MaterialTheme.typography.body1,
                    placeholder = {
                        Text(
                            "Выход, грамм",
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
                    modifier = Modifier
                        .height(56.dp),
                    textStyle = MaterialTheme.typography.body1,
                    placeholder = {
                        Text(
                            "Время приготовления, мин.",
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
                            "Добавьте ингредиент, кол-во",
                            style = MaterialTheme.typography.body2,
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { }){
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
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
                TextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier
                        .height(56.dp),
                    textStyle = MaterialTheme.typography.body1,
                    placeholder = {
                        Text(
                            "Примечание",
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
                        "Рецептов много не бывает)",
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
fun previewRec() {
    AppTheme {
        RecScreen()
    }
}