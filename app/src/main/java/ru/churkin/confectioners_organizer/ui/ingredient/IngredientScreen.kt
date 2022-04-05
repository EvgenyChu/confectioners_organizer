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
import ru.churkin.confectioners_organizer.date.format
import ru.churkin.confectioners_organizer.items.MainButton
import ru.churkin.confectioners_organizer.items.ParamsChoiceItem
import ru.churkin.confectioners_organizer.items.ParamsToolBar
import ru.churkin.confectioners_organizer.items.TextItem
import ru.churkin.confectioners_organizer.view_models.ingredient.data.IngredientViewModel

@InternalCoroutinesApi
@ExperimentalMaterialApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun IngredientScreen(
    navController: NavController,
    id: Long,
    vm: IngredientViewModel = viewModel(LocalContext.current as RootActivity, key = "ingredient")
) {

    val state by vm.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        vm.initState(id)
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

            TextItem(state.title)

            Divider(color = MaterialTheme.colors.secondary)

            ParamsChoiceItem(
                tintIcon = state.availability,
                text = if (state.availability) "В наличии" else "Отсутствует",
                content = "Наличие ингредиента"
            )

            Divider(color = MaterialTheme.colors.secondary)

            TextItem("В наличии: ${state.available} ${state.unitsAvailable}")

            Divider(color = MaterialTheme.colors.secondary)

            TextItem("Цена: ${state.costPrice} ${state.unitsPrice}")

            Divider(color = MaterialTheme.colors.secondary)

            TextItem("Годен до: ${state.sellBy?.format("dd.MM.yyyy")}")

            Divider(color = MaterialTheme.colors.secondary)
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

        MainButton(
            tailIcon = R.drawable.ic_baseline_done_24,
            modifier = Modifier.align(alignment = Alignment.BottomEnd)
        ){
            navController.navigate(Screen.Ingredients.route)
        }
    }
}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun IngredientToolBar(
    navController: NavController,
    vm: IngredientViewModel = viewModel(LocalContext.current as RootActivity, key = "ingredient")
) {
    val state by vm.state.collectAsState()

    ParamsToolBar(
        text = "Ингредиент",
        onBackClick = { navController.popBackStack() },
        onEditClick = { navController.navigate("ingredients/edit/${state.id}") }
    )
}
