package ru.churkin.confectioners_organizer.ui.recept

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.InternalCoroutinesApi
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.RootActivity
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.items.*
import ru.churkin.confectioners_organizer.local.db.entity.ReceptIngredientItem
import ru.churkin.confectioners_organizer.ui.theme.Green
import ru.churkin.confectioners_organizer.ui.theme.Red
import ru.churkin.confectioners_organizer.view_models.recept.CreateReceptViewModel
import ru.churkin.confectioners_organizer.view_models.recept.IngredientItem

@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterialApi::class)
@InternalCoroutinesApi
@Composable
fun CreateReceptScreen(
    navController: NavController,
    id: Long?,
    vm: CreateReceptViewModel = viewModel(
        LocalContext.current as RootActivity,
        key = "create_recept"
    )
) {

    val state by vm.state.collectAsState()

    val colors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.onPrimary,
        backgroundColor = MaterialTheme.colors.background,
        disabledTextColor = MaterialTheme.colors.background,
        placeholderColor = MaterialTheme.colors.background,
        disabledPlaceholderColor = MaterialTheme.colors.background,
        focusedIndicatorColor = MaterialTheme.colors.secondary,
        cursorColor = MaterialTheme.colors.onPrimary
    )

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

                EditTextItem(
                    value = state.title,
                    onValueChange = { vm.updateTitle(it) },
                    label = "Название рецепта"
                )

                EditTextItem(
                    value = "${if (state.weight == 0) "" else state.weight}",
                    onValueChange = { vm.updateWeight(it) },
                    label = "Выход, грамм",
                    inputType = KeyboardType.Number
                )

                EditTextItem(
                    value = "${if (state.time == 0) "" else state.time}",
                    onValueChange = { vm.updateTime(it) },
                    label = "Время приготовления, мин.",
                    inputType = KeyboardType.Number
                )

                AdditableItem(
                    onTailClick = { vm.showCreateDialog() },
                    text = "Добавьте ингредиент, кол-во"
                )

                if (state.ingredients.isNotEmpty()) {
                    Box(modifier = Modifier.heightIn(0.dp, 3000.dp)) {
                        LazyColumn() {
                            items(state.ingredients, { it.id }) { item ->

                                val dismissState = rememberDismissState()

                                if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                                    vm.removeReceptIngredient(item.id)
                                }

                                SwipeItem(
                                    onDismiss = { vm.removeReceptIngredient(item.id) },
                                ){
                                    ReceptIngItem(receptIngredientItem = item)
                                }
                            }
                        }
                    }
                }

                EditTextItem(
                    value = state.note,
                    onValueChange = { vm.updateNote(it) },
                    label = "Примечание"
                )

                Spacer(modifier = Modifier.height(56.dp))
            }

        }

        ParamsBottomBar(
            text = "Рецептов много не бывает)"
        )

        MainButton(
            tailIcon = R.drawable.ic_baseline_done_24,
            modifier = Modifier.align(alignment = Alignment.BottomEnd)
        ) {
            vm.addRecept()
            navController.navigate(Screen.Recepts.route)
        }
    }

    if (state.isCreateDialog) {
        CreateIngredientsDialog(
            onDismiss = { vm.hideCreateDialog() },
            onCreate = { title, count, availability, unitsAvailable, costPrice ->
                vm.createReceptIngredient(title, count, availability, unitsAvailable, costPrice)
            },
            listIngredients = state.availableIngredients
        )
    }


}


@Composable
fun CreateIngredientsDialog(
    listIngredients: List<IngredientItem>,
    onDismiss: () -> Unit,
    onCreate: (title: String, count: Int, availibility: Boolean, unitsAvailable: String, costPrise: Float) -> Unit
) {
    Log.e("dialog", listIngredients.toString())
    var selectionItem: String? by remember { mutableStateOf(null) }

    var selectionAvailability: Boolean? by remember { mutableStateOf(null) }

    var selectionUnitsAvailable: String? by remember { mutableStateOf(null) }

    var ingredientCount: Int by remember { mutableStateOf(0) }

    var selectionCostPrice: Float by remember { mutableStateOf(0f) }

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

                Box(
                    modifier = Modifier
                        .heightIn(0.dp, 300.dp)
                ) {
                    LazyColumn() {
                        listIngredients.forEach {
                            val backgroundColor =
                                if (selectionItem == it.title) Color.Green
                                else Color.Transparent

                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(color = backgroundColor)
                                        .clickable(onClick = {
                                            selectionItem = it.title
                                            selectionAvailability = it.availability
                                            selectionUnitsAvailable = it.unitsAvailable
                                            selectionCostPrice = it.costPrice
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

                                    Spacer(Modifier.weight(1f, true))

                                    Text(
                                        text = it.unitsAvailable,
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                            }
                        }
                    }
                }

                EditTextItem(
                    value = "${if (ingredientCount == 0) "" else ingredientCount}",
                    onValueChange = {
                        try {
                            ingredientCount = if (it.isBlank()) 0 else it.toInt()
                        } catch (e: NumberFormatException) {
                            ingredientCount = 0
                        }
                    },
                    label = "Кол-во для рецепта",
                    inputType = KeyboardType.Number
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
                            selectionItem?.let {
                                onCreate(
                                    it,
                                    ingredientCount,
                                    selectionAvailability ?: true,
                                    selectionUnitsAvailable ?: "г.",
                                    selectionCostPrice
                                )
                            }
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
            Spacer(Modifier.width(8.dp))
            Text(
                text = receptIngredientItem.unitsAvailable,
                style = MaterialTheme.typography.subtitle1
            )
        }
        Divider(color = MaterialTheme.colors.secondary)
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@OptIn(InternalCoroutinesApi::class)
@Composable
fun CreateReceptToolBar(
    navController: NavController,
    vm: CreateReceptViewModel = viewModel(
        LocalContext.current as RootActivity,
        key = "create_recept"
    )
) {
    val state by vm.state.collectAsState()
    val isCreate: Boolean by remember {
        mutableStateOf(navController.currentDestination?.route == "recepts/create")
    }
    val title by remember {
        mutableStateOf(
            if (isCreate) "Новый рецепт" else "Редактирование"
        )
    }

    ParamsToolBar(
        text = title,
        editIcon = R.drawable.ic_baseline_delete_24,
        onBackClick = {
            navController.popBackStack()
            if (isCreate) vm.removeRecept(state.id)
        },
        onEditClick = { vm.emptyState() }
    )

}

/*
@Preview
@Composable
fun previewRec() {
    AppTheme {
        RecScreen()
    }
}*/
