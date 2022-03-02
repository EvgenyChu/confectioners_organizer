package ru.churkin.confectioners_organizer.ui.list_ingredients

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.ui.theme.Green
import ru.churkin.confectioners_organizer.ui.theme.Red
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient
import ru.churkin.confectioners_organizer.view_models.list_ingredients.IngredientsState
import ru.churkin.confectioners_organizer.view_models.list_ingredients.IngsViewModel

@ExperimentalMaterialApi
@Composable
fun IngsScreen(navController: NavController, vm: IngsViewModel = viewModel()) {

    val state by vm.state.collectAsState()


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background)
        ) {

            Column(
                Modifier
                    .fillMaxSize()
            ) {
                TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
                    IconButton(onClick = {navController.navigate("recepts") }) {
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
                        LazyColumn {
                            items(listState.ingredients, { it.id }) { item ->

                                val dismissState = rememberDismissState()
                                if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                                    vm.removeIngredient(item.id)
                                }
                                SwipeToDismiss(
                                    state = dismissState,
                                    directions = setOf(
                                        DismissDirection.StartToEnd,
                                    ),
                                    background = {

                                        val color by animateColorAsState(
                                            when (dismissState.targetValue) {
                                                DismissValue.Default -> MaterialTheme.colors.surface
                                                else -> MaterialTheme.colors.secondary
                                            }
                                        )

                                        val icon = Icons.Default.Delete

                                        val scale by animateFloatAsState(targetValue = if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.2f)

                                        val alignment = Alignment.CenterStart


                                        Box(
                                            Modifier
                                                .fillMaxSize()
                                                .background(color)
                                                .padding(start = 16.dp, end = 16.dp),
                                            contentAlignment = alignment
                                        ) {
                                            Icon(
                                                icon,
                                                contentDescription = "icon",
                                                modifier = Modifier.scale(scale)
                                            )
                                        }
                                    },
                                    dismissContent = {
                                        IngredientItem(ingredient = item, onClick = { id ->
                                            navController.navigate("ingredients/$id")
                                        })
                                    }
                                )
                            }
                        }
                    }
                    is IngredientsState.ValueWithMessage -> {}
                    }
            }
            Spacer(modifier = Modifier.height(56.dp))

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
