package ru.churkin.confectioners_organizer.ui.list_ingredients

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.ui.theme.AppTheme
import ru.churkin.confectioners_organizer.ui.theme.Green
import ru.churkin.confectioners_organizer.ui.theme.Red
import ru.churkin.confectioners_organizer.view_models.ingredient.data.Ingredient
import ru.churkin.confectioners_organizer.view_models.list_ingredients.IngredientsState
import ru.churkin.confectioners_organizer.view_models.list_ingredients.ListIngsViewModel

@Composable
fun IngsScreen(navController: NavController, vm: ListIngsViewModel = viewModel()) {

    val state by vm.screenState.collectAsState()

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
                    IconButton(onClick = {navController.navigate(Screen.Recept.route) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_dehaze_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Меню навигации"
                        )
                    }
                    Text(
                        "Список ингредиентов",
                        style = MaterialTheme.typography.h6,
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
                when (val listState = state.ingredientsState) {
                    is IngredientsState.Empty -> {}
                    is IngredientsState.Loading -> {}
                    is IngredientsState.Value -> {
                        listState.ingredients
                            .forEach {
                                IngsCard(ingredient = it)
                            }
                    }
                    is IngredientsState.ValueWithMessage -> {}
                    }
            }
            Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {
                BottomAppBar(
                    backgroundColor = MaterialTheme.colors.primary,
                    modifier = Modifier.height(56.dp)
                ) {

                    Text(
                        "Похоже чего-то не хватает)",
                        modifier = Modifier.padding(start = 12.dp),
                        style = MaterialTheme.typography.body1
                    )

                }
            }
            FloatingActionButton(
                onClick = {navController.navigate(Screen.Ingredient.route) },
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
fun IngsCard(vm: ListIngsViewModel = viewModel(), ingredient: Ingredient) {
    AppTheme() {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colors.background)
        ) {
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
                    tint = if (ingredient.availability) Green
                    else Red,
                    contentDescription = "Наличие"
                )
                Text(
                    text = ingredient.title,
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${ingredient.available} ${ingredient.unitsAvailable}",
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
}


@Preview
@Composable
fun previewIngsCard() {
    AppTheme {
        IngsCard(
            ingredient = Ingredient()
        )
    }
}

/*
@Preview
@Composable
fun previewIngs() {
    AppTheme {
        IngsScreen()
    }
}*/
