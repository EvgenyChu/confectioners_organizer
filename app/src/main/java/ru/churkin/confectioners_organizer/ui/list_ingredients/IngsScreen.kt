package ru.churkin.confectioners_organizer.ui.list_ingredients

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import ru.churkin.confectioners_organizer.items.*
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient
import ru.churkin.confectioners_organizer.ui.theme.Green
import ru.churkin.confectioners_organizer.ui.theme.Red
import ru.churkin.confectioners_organizer.view_models.list_ingredients.IngredientsState
import ru.churkin.confectioners_organizer.view_models.list_ingredients.IngsViewModel

@InternalCoroutinesApi
@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalMaterialApi
@Composable
fun IngsScreen(
    navController: NavController,
    vm: IngsViewModel = viewModel(LocalContext.current as RootActivity, key = "ingredients")
) {

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

            when (val listState = state) {
                is IngredientsState.Empty -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize(1f)
                    ) {
                        Text("Не найдено")
                    }
                }
                is IngredientsState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize(1f)
                            .background(color = MaterialTheme.colors.background)
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colors.secondary)
                    }
                }
                is IngredientsState.Value -> {
                    LazyColumn(contentPadding = PaddingValues(bottom = 56.dp)) {
                        items(listState.ingredients, { it.id }) { item ->

                            SwipeItem(
                                onDismiss = { vm.removeIngredient(item.id) },
                            ) {
                                IngredientItem(ingredient = item, onClick = { id ->
                                    navController.navigate("ingredients/$id")
                                })
                            }
                        }
                    }
                }
                is IngredientsState.ValueWithMessage -> {}
            }
        }

        ParamsBottomBar(
            text = "Похоже чего-то не хватает)"
        )

        MainButton(
            tailIcon = R.drawable.ic_baseline_add_24,
            modifier = Modifier.align(alignment = Alignment.BottomEnd)
        ) {
            navController.navigate("ingredients/create")
        }
    }
}

@Composable
fun IngredientItem(ingredient: Ingredient, onClick: (Long) -> Unit) {

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
    ) {

        RowChoiceItem(
            onClick = {onClick(ingredient.id)},
        tint = if (ingredient.availability) Green else Red,
        textFirst = ingredient.title,
        textSecond = "${ingredient.available}" +
                if (ingredient.unitsAvailable == "ед. изм.") "" else " ${ingredient.unitsAvailable}"
        )

        Divider(color = MaterialTheme.colors.secondary)
    }
}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun IngsToolBar(
    vm: IngsViewModel = viewModel(LocalContext.current as RootActivity, key = "ingredients"),
    onMenuClick: () -> Unit
) {
    val searchText by vm.searchText.collectAsState()
    var counter by remember { mutableStateOf(0) }

    SearchToolBar(
        searchText = searchText,
        text = "Ингредиенты",
        onNavigate = { onMenuClick() },
        actions = listOf(
            ToolBarAction(
                icon = R.drawable.ic_baseline_circle_24,
                tint = when (counter) {
                    1 -> R.color.green
                    2 -> R.color.red
                    else -> null
                },
                action = {
                    counter++
                    if (counter > 2) counter = 0
                    vm.filterIngredients(counter)
                })
        ),
        onSearch = { vm.searchIngredients(it) },
        onSubmit = { vm.searchIngredients(it) },
        onSearchDismiss = {}
    )
}
