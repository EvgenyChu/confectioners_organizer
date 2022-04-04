package ru.churkin.confectioners_organizer.ui.recept

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.InternalCoroutinesApi
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.RootActivity
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.items.ParamsActionItem
import ru.churkin.confectioners_organizer.items.ParamsTextItem
import ru.churkin.confectioners_organizer.items.ParamsToolBar
import ru.churkin.confectioners_organizer.view_models.recept.ReceptViewModel

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ReceptScreen(
    navController: NavController,
    id: Long,
    vm: ReceptViewModel = viewModel(LocalContext.current as RootActivity, key = "recept")
) {

    val state by vm.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        vm.initState(id)
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

                ParamsTextItem(state.title)

                Divider(color = MaterialTheme.colors.secondary)

                ParamsTextItem("Выход: ${state.weight} грамм")

                Divider(color = MaterialTheme.colors.secondary)

                ParamsTextItem("Время приготовления: ${state.time} мин.")

                Divider(color = MaterialTheme.colors.secondary)

                ParamsTextItem("Список ингредиентов для рецепта:")

                if (state.ingredients.isNotEmpty()) state.ingredients.forEach {
                    ReceptIngItem(receptIngredientItem = it)
                }

                ParamsTextItem("Примечание: \n${state.note}")

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

        ParamsActionItem(
            tailIcon = R.drawable.ic_baseline_done_24,
            modifier = Modifier.align(alignment = Alignment.BottomEnd)
        ){
            navController.navigate(Screen.Recepts.route)
        }
    }
}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ReceptToolBar(
    navController: NavController,
    vm: ReceptViewModel = viewModel(LocalContext.current as RootActivity, key = "recept")
) {
    val state by vm.state.collectAsState()

    ParamsToolBar(
        text = "Pецепт",
        onBackClick = { navController.popBackStack() },
        onEditClick = { navController.navigate("recepts/edit/${state.id}") }
    )
}