package ru.churkin.confectioners_organizer.ingredient

import android.graphics.drawable.shapes.Shape
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.ui.theme.AppTheme
import java.util.*

@Composable
fun IngScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primary)
    ) {

        Column(
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.primary)
        ) {
            TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        tint = MaterialTheme.colors.onPrimary,
                        contentDescription = "Меню навигации"
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
                    .height(1.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            TextField(
                value = "",
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.primary),
                textStyle = MaterialTheme.typography.body1,
                placeholder = {
                    Text(
                        "Наименование",
                        style = MaterialTheme.typography.body2,
                    )
                })
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )

            TextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.body1,
                placeholder = {
                    Text(
                        "Наличие ингредиента",
                        style = MaterialTheme.typography.body1,
                        color = colorResource(
                            R.color.second_text
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_check_circle),
                        tint = MaterialTheme.colors.secondaryVariant,
                        contentDescription = "Наличие"
                    )
                }
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                TextField(
                    value = "",
                    onValueChange = { },
                    textStyle = MaterialTheme.typography.body1,
                    placeholder = {
                        Text(
                            "Количество,",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.background
                        )
                    })
                //   Spacer(modifier = Modifier.fillMaxWidth())
                Box(Modifier.height(56.dp)) {
                    Text(
                        "ед. изм.",
                        modifier = Modifier.padding(top = 16.dp),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.background
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_down_24),
                        tint = MaterialTheme.colors.secondary,
                        modifier = Modifier.padding(top = 14.dp),
                        contentDescription = "Выбор ед.изм."
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                TextField(
                    modifier = Modifier.width(215.dp),
                    value = "",
                    onValueChange = { },
                    textStyle = MaterialTheme.typography.body1,
                    placeholder = {
                        Text(
                            "Цена,",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.background
                        )
                    })

                Box(Modifier.height(56.dp)) {
                    Text(
                        "рубль за _______",
                        modifier = Modifier.padding(top = 16.dp),
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.background
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_down_24),
                        tint = MaterialTheme.colors.secondary,
                        modifier = Modifier.padding(top = 14.dp),
                        contentDescription = "Выбор ед.изм."
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            TextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.body1,
                placeholder = {
                    Text(
                        "Годен до: дд/мм/гггг",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.background
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                        tint = MaterialTheme.colors.secondary,
                        contentDescription = "Наличие"
                    )
                }
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
        }

        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            BottomAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.height(56.dp)
            ) {

                Text(
                    "Жаль, что продукты кончаются)",
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.body1,
                    color = colorResource(R.color.text)
                )

            }
        }
        FloatingActionButton(
            onClick = { },
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(bottom = 28.dp, end = 16.dp)
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

@Preview
@Composable
fun preview() {
    AppTheme {
        IngScreen()
    }
}