package ru.churkin.confectioners_organizer.ui.list_ingredients

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.InternalCoroutinesApi
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.RootActivity
import ru.churkin.confectioners_organizer.items.ParamsActionItem
import ru.churkin.confectioners_organizer.items.ParamsSwipeItem
import ru.churkin.confectioners_organizer.items.ParamsToolSearchBar
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

                            ParamsSwipeItem(
                                onDismiss = { vm.removeIngredient(item.id) },
                            ){
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

        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxHeight()
        ) {
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

        ParamsActionItem(
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { onClick(ingredient.id) }
                .padding(end = 16.dp),
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
                text = "${ingredient.available}" +
                        if (ingredient.unitsAvailable == "ед. изм.") "" else " ${ingredient.unitsAvailable}",
                style = MaterialTheme.typography.subtitle1
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            color = MaterialTheme.colors.secondary
        )
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

    ParamsToolSearchBar(
        searchText = searchText,
        onTailClick = {onMenuClick()},
        onFilterClick ={vm.filterIngredients(it)},
        onSearch = { vm.searchIngredients(it) },
        onSubmit = {vm.searchIngredients(it)}
    )
}

/*
@Preview
@Composable
fun previewIngsCard() {
    AppTheme {
        IngsCard(
            ingredient = Ingredient()
        )
    }
}
*/

/*
@Preview
@Composable
fun previewIngs() {
    AppTheme {
        IngsScreen()
    }
}*/
