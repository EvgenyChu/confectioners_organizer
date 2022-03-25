package ru.churkin.confectioners_organizer.ui.recept

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.view_models.recept.ReceptViewModel

@Composable
fun ReceptScreen(navController: NavController, vm: ReceptViewModel = viewModel()) {

    val state by vm.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        vm.initState()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {

        Column(
            Modifier
                .fillMaxSize()
        ) {

            Column(Modifier.verticalScroll(rememberScrollState())) {

                Row(
                    Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        state.title,
                        style = MaterialTheme.typography.subtitle1
                    )
                }


                Divider(color = MaterialTheme.colors.secondary)

                Row(
                    Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Выход: ${state.weight} грамм",
                        style = MaterialTheme.typography.subtitle1
                    )
                }

                Divider(color = MaterialTheme.colors.secondary)

                Row(
                    Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Время приготовления: ${state.time} мин.",
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                Divider(color = MaterialTheme.colors.secondary)

                Row(
                    Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Список ингредиентов для рецепта:",
                        style = MaterialTheme.typography.subtitle1
                    )
                }

                if (state.ingredients.isNotEmpty()) state.ingredients.sortedBy { it.title }.forEach {
                    ReceptIngItem(receptIngredientItem = it)
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Примечание: \n${state.note}",
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                Divider(color = MaterialTheme.colors.secondary)

                Spacer(modifier = Modifier.height(56.dp))
            }
        }


        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {

            BottomAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.height(56.dp)
            ) {

                Text(
                    "Это можно приготовить)",
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.body1
                )

            }
        }
        FloatingActionButton(
            onClick = {
                navController.navigate(Screen.Recepts.route)
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

@Composable
fun ReceptToolBar(
    navController: NavController,
    vm: ReceptViewModel = viewModel()
){
    val state by vm.state.collectAsState()

    TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "Назад"
            )
        }
        Text(
            "Pецепт",
            style = MaterialTheme.typography.h6,
        )

        Spacer(Modifier.weight(1f, true))

        IconButton(onClick = { navController.navigate("recepts/edit/${state.id}") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "Очистить"
            )
        }
    }
}