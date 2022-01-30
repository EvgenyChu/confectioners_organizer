package ru.churkin.confectioners_organizer.ingredient

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
import ru.churkin.confectioners_organizer.ui.theme.AppTheme

@Composable
fun IngsScreen() {
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
                        "Список ингредиентов",
                        style = MaterialTheme.typography.body1,
                    )
                    Spacer(Modifier.weight(1f, true))

                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_circle_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Сортировка"
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

                    Text(
                        "Похоже чего-то не хватает)",
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
fun IngsCard() {
    AppTheme() {
        Column() {
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
                    tint = MaterialTheme.colors.onSecondary,
                    contentDescription = "Наличие"
                )
                Text(
                    text = "Мука",
                    style = MaterialTheme.typography.body1
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "300 г.",
                    style = MaterialTheme.typography.body1
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
}


@Preview
@Composable
fun previewIngsCard() {
    AppTheme {
        IngsCard()
    }
}

@Preview
@Composable
fun previewIngs() {
    AppTheme {
        IngsScreen()
    }
}