package ru.churkin.confectioners_organizer.ingredient

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.date.format
import ru.churkin.confectioners_organizer.view_models.ingredient.data.IngredientViewModel

@Composable
fun IngredientScreen(navController: NavController, vm: IngredientViewModel = viewModel()) {

    val state by vm.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        vm.initState()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
                IconButton(onClick = {navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        tint = MaterialTheme.colors.onPrimary,
                        contentDescription = "Назад"
                    )
                }
                Text(
                    text = "Ингредиент",
                    style = MaterialTheme.typography.h6,
                )
                Spacer(Modifier.weight(1f, true))

                IconButton(onClick = { navController.navigate("ingredients/edit/${state.id}")}) {
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
                    text = state.title,
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
                Icon(
                    modifier = Modifier.padding(start = 16.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_check_circle),
                    tint = if (state.availability) MaterialTheme.colors.secondary else MaterialTheme.colors.surface,
                    contentDescription = "Наличие"
                )
                Text(
                    text = if (state.availability) "В наличии" else "Отсутствует",
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
                    text = "В наличии: ${state.available} ${state.unitsAvailable}",
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
                    text = "Цена: ${state.costPrice} ${state.unitsPrice}",
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
                    text = "Годен до: ${state.sellBy?.format("dd.MM.yyyy")}",
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
            ) {

                Text(
                    "Что бы с этим сделать?)",
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.body1
                )

            }
        }
        FloatingActionButton(
            onClick = {
                navController.navigate(Screen.Ingredients.route)
            },
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(bottom = 28.dp, end = 16.dp),
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_done_24),
                contentDescription = "Добавить"
            )
        }
    }
}

/*
@Preview
@Composable
fun previewRealIng() {
    AppTheme {
        RealIngScreen(
            ingredient = IngredientState.makeIngredient(
                title = "Огонь",
                _costPrice = "0.0",
                availability = true,
                available = 10,
                unitsPrice = "руб./г.",
                sellBy = Date(),
                unitsAvailable = "г."
            )
        )
    }
}*/
