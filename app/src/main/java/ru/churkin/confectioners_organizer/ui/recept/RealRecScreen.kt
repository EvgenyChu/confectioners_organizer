package ru.churkin.confectioners_organizer.recept

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.ui.list_ingredients.IngsCard
import ru.churkin.confectioners_organizer.ui.theme.AppTheme
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient
import ru.churkin.confectioners_organizer.view_models.recept.data.Recept

@Composable
fun RealRecScreen(recept: Recept) {
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
                        "Рецепт",
                        style = MaterialTheme.typography.h6,
                    )
                    Spacer(Modifier.weight(1f, true))

                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Редактировать"
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
                        text = recept.title,
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
                        text = "Вес изделия: ${recept.weight} грамм",
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
                        text = "Время приготовления: ${recept.time} мин.",
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
                        text = "Список ингредиентов:",
                        modifier = Modifier
                            .padding(start = 16.dp),
                        style = MaterialTheme.typography.subtitle1,
                    )
                }

                    IngsCard(ingredient = Ingredient())

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (recept.note.isBlank() || recept.note.isEmpty()) "Примечание"
                        else recept.note,
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
                        "Отличный рецепт)",
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

/*
@Preview
@Composable
fun previewRealRec() {
    AppTheme {
        RealRecScreen(
            recept = Recept.makeRecept(
                title = "Бисквит",
                weight = 1000,
                time = 120,
                listIngredients = listOf(),
                note = ""
            )
        )
    }
}*/
