package ru.churkin.confectioners_organizer.ui.recept

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.InternalCoroutinesApi
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.ui.theme.Green
import ru.churkin.confectioners_organizer.ui.theme.Red
import ru.churkin.confectioners_organizer.view_models.recept.IngredientItem
import ru.churkin.confectioners_organizer.local.db.entity.ReceptIngredientItem
import ru.churkin.confectioners_organizer.view_models.recept.CreateReceptViewModel

@InternalCoroutinesApi
@Composable
fun CreateReceptScreen(navController: NavController, vm: CreateReceptViewModel = viewModel()) {

    val state by vm.state.collectAsState()
    val isCreate: Boolean by remember {
        mutableStateOf(navController.currentDestination?.route == "recepts/create")
    }
    val title by remember {
        mutableStateOf(
            if (isCreate) "Новый рецепт" else "Редактирование"
        )
    }

    val colors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.onPrimary,
        backgroundColor = MaterialTheme.colors.background,
        disabledTextColor = MaterialTheme.colors.background,
        placeholderColor = MaterialTheme.colors.background,
        disabledPlaceholderColor = MaterialTheme.colors.background,
        focusedIndicatorColor = MaterialTheme.colors.secondary,
        cursorColor = MaterialTheme.colors.onPrimary
    )
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
                IconButton(onClick = {
                    navController.popBackStack()
                    if (isCreate) vm.removeRecept(state.id)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                        tint = MaterialTheme.colors.onPrimary,
                        contentDescription = "Назад"
                    )
                }
                Text(
                    title,
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.weight(1f, true))

                IconButton(onClick = { vm.emptyState() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                        tint = MaterialTheme.colors.onPrimary,
                        contentDescription = "Очистить"
                    )
                }
            }
            Column(Modifier.verticalScroll(rememberScrollState())) {
                TextField(
                    value = state.title,
                    onValueChange = { vm.updateTitle(it) },
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth(),
                    textStyle = MaterialTheme.typography.subtitle1,
                    label = {
                        Text(
                            "Название рецепта",
                            style = MaterialTheme.typography.subtitle2
                        )
                    },
                    colors = colors
                )


                TextField(
                    value = "${if (state.weight == 0) "" else state.weight}",
                    onValueChange = { vm.updateWeight(if (it.isEmpty()) 0 else it.toInt()) },
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth(),
                    textStyle = MaterialTheme.typography.subtitle1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text(
                            "Выход, грамм",
                            style = MaterialTheme.typography.subtitle2
                        )
                    },
                    colors = colors
                )

                TextField(
                    value = "${if (state.time == 0) "" else state.time}",
                    onValueChange = { vm.updateTime(if (it.isEmpty()) 0 else it.toInt()) },
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth(),
                    textStyle = MaterialTheme.typography.subtitle1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = {
                        Text(
                            "Время приготовления, мин.",
                            style = MaterialTheme.typography.subtitle2
                        )
                    },
                    colors = colors
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clickable { vm.showCreateDialog() }
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Добавьте ингредиент, кол-во",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.subtitle2
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                        tint = MaterialTheme.colors.secondary,
                        contentDescription = "Наличие"
                    )
                }



                if (state.ingredients.isNotEmpty()) state.ingredients.forEach {
                    ReceptIngItem(receptIngredientItem = it)
                }

                TextField(
                    value = state.note,
                    onValueChange = { vm.updateNote(it) },
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth(),
                    textStyle = MaterialTheme.typography.subtitle1,
                    label = {
                        Text(
                            "Примечание",
                            style = MaterialTheme.typography.subtitle2
                        )
                    },
                    colors = colors
                )
                Spacer(modifier = Modifier.height(56.dp))
            }

        }

        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {

            BottomAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.height(56.dp)
            ) {

                Text(
                    "Рецептов много не бывает)",
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.body1
                )

            }
        }
        FloatingActionButton(
            onClick = {
                vm.addRecept()
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

    //modals
    if (state.isCreateDialog) {
        CreateIngredientsDialog(
            onDismiss = { vm.hideCreateDialog() },
            onCreate = { title, count, availability ->
                vm.createReceptIngredient(title, count, availability)
            },
            listIngredients = state.availableIngredients
        )
    }


}


@Composable
fun CreateIngredientsDialog(
    listIngredients: List<IngredientItem>,
    onDismiss: () -> Unit,
    onCreate: (title: String, count: Int, availibility: Boolean) -> Unit
) {
    Log.e("dialog", listIngredients.toString())
    var selectionItem: String? by remember { mutableStateOf(null) }

    var ingredientCount: Int by remember { mutableStateOf(0) }

    val colors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.onPrimary,
        backgroundColor = MaterialTheme.colors.background,
        disabledTextColor = MaterialTheme.colors.background,
        placeholderColor = MaterialTheme.colors.background,
        disabledPlaceholderColor = MaterialTheme.colors.background,
        focusedIndicatorColor = MaterialTheme.colors.secondary,
        cursorColor = MaterialTheme.colors.onPrimary
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()

            ) {
                Text(
                    text = "Выберете ингредиент из списка",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier
                    .width(300.dp)
                    .height(300.dp)) {
                    LazyColumn() {
                        listIngredients.forEach {
                            val backgroundColor =
                                if (selectionItem == it.title) Color.Red
                                else Color.Transparent

                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(color = backgroundColor)
                                        .clickable(onClick = {
                                            selectionItem = it.title
                                        })
                                        .height(44.dp)
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth()
                                ) {
                                    Icon(
                                        modifier = Modifier.padding(16.dp),
                                        painter = painterResource(id = R.drawable.ic_baseline_circle_24),
                                        tint = if (it.availability) Green else Red,
                                        contentDescription = "Наличие"
                                    )

                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                            }
                        }
                    }
                }

                TextField(
                    value = "${if (ingredientCount == 0) "" else ingredientCount}",
                    onValueChange = {
                        try {
                            ingredientCount = if (it.isBlank()) 0 else it.toInt()
                        } catch (e: NumberFormatException) {
                            ingredientCount = 0
                        }
                    },
                    textStyle = MaterialTheme.typography.body2,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth(),
                    label = {
                        Text(
                            "Кол-во для рецепта",
                            style = MaterialTheme.typography.subtitle2
                        )
                    },
                    colors = colors
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    TextButton(onClick = onDismiss) {
                        Text("Отмена", color = MaterialTheme.colors.secondary)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = {
                            selectionItem?.let { onCreate(it, ingredientCount, true) }
                        },
                        enabled = selectionItem != null && ingredientCount > 0
                    )
                    {
                        Text("Добавить", color = MaterialTheme.colors.secondary)
                    }
                }
            }
        }
    }
}

@Composable
fun ReceptIngItem(receptIngredientItem: ReceptIngredientItem) {
    Column(
        modifier = Modifier

            .background(color = MaterialTheme.colors.background)
    ) {
        Row(
            modifier = Modifier
                .height(44.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_circle_24),
                tint = if (receptIngredientItem.availability) Green else Red,
                contentDescription = "Наличие"
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = receptIngredientItem.title,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "${receptIngredientItem.count}",
                style = MaterialTheme.typography.subtitle1
            )
        }
        Divider(color = MaterialTheme.colors.secondary)
    }
}

/*
@Preview
@Composable
fun previewRec() {
    AppTheme {
        RecScreen()
    }
}*/
