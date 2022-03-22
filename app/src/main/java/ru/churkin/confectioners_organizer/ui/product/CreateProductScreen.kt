package ru.churkin.confectioners_organizer.product

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.listOrders.OrderItem
import ru.churkin.confectioners_organizer.local.db.entity.ProductIngredientItem
import ru.churkin.confectioners_organizer.local.db.entity.ProductReceptItem
import ru.churkin.confectioners_organizer.local.db.entity.ReceptIngredientItem
import ru.churkin.confectioners_organizer.ui.recept.CreateIngredientsDialog
import ru.churkin.confectioners_organizer.ui.theme.AppTheme
import ru.churkin.confectioners_organizer.ui.theme.Green
import ru.churkin.confectioners_organizer.ui.theme.Red
import ru.churkin.confectioners_organizer.view_models.product.data.CreateProductViewModel
import ru.churkin.confectioners_organizer.view_models.product.data.ReceptItem
import ru.churkin.confectioners_organizer.view_models.recept.IngredientItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateProductScreen(navController: NavController, vm: CreateProductViewModel = viewModel()) {

    val state by vm.state.collectAsState()
    val isCreate: Boolean by remember {
        mutableStateOf(navController.currentDestination?.route == "orders/{order_id}/products/{id}")
    }
    val title by remember {
        mutableStateOf(
            if (!isCreate) "Новое изделие" else "Редактирование"
        )
    }

    var openDialogUnits by remember { mutableStateOf(false) }

    val colors = TextFieldDefaults.textFieldColors(
        textColor = colors.onPrimary,
        backgroundColor = colors.background,
        disabledTextColor = colors.background,
        placeholderColor = colors.background,
        disabledPlaceholderColor = colors.background,
        focusedIndicatorColor = colors.secondary,
        cursorColor = colors.onPrimary
    )

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
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
                IconButton(onClick = {
                    navController.popBackStack()
                    if (!isCreate) vm.removeProduct(state.id)
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

            TextField(
                value = state.title,
                onValueChange = { vm.updateTitle(it) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.subtitle1,
                placeholder = {
                    Text(
                        "Название изделия",
                        style = MaterialTheme.typography.subtitle2,
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_search_24),
                            tint = MaterialTheme.colors.secondary,
                            contentDescription = "Поиск изделий"
                        )
                    }
                },
                colors = colors
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                TextField(
                    value = "${if (state.weight == 0) "" else state.weight}",
                    onValueChange = {
                        try {
                            vm.updateWeight(if (it.isEmpty()) 0 else it.toInt())
                        } catch (e: Exception) {
                            ""
                        }
                    },
                    modifier = Modifier
                        .height(56.dp)
                        .weight(3f),
                    textStyle = MaterialTheme.typography.subtitle1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = {
                        Text(
                            "Количество изделия",
                            style = MaterialTheme.typography.subtitle2
                        )
                    },
                    colors = colors
                )

                Box(
                    Modifier
                        .height(56.dp)
                        .weight(1f)
                ) {
                    Text(
                        state.units,
                        modifier = Modifier.padding(top = 16.dp),
                        style = if (state.units == "ед. изм.") MaterialTheme.typography.subtitle2
                        else MaterialTheme.typography.subtitle1
                    )
                }
                IconButton(onClick = { openDialogUnits = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_down_24),
                        tint = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .padding(top = 14.dp)
                            .weight(1f),
                        contentDescription = "Выбор ед.изм."
                    )
                }
                if (openDialogUnits) {
                    AlertDialog(
                        onDismissRequest = {
                            openDialogUnits = false
                        },
                        title = {
                            Text(
                                style = MaterialTheme.typography.h6,
                                text = "Выберите единицу измерения",
                            )
                        },
                        buttons = {
                            Column(modifier = Modifier.padding(all = 16.dp)) {
                                Text(
                                    style = MaterialTheme.typography.subtitle1,
                                    text = "Грамм", modifier = Modifier
                                        .height(44.dp)
                                        .fillMaxWidth()
                                        .clickable {
                                            vm.updateUnits("г.")
                                            openDialogUnits = false
                                        }
                                )
                                Text(
                                    style = MaterialTheme.typography.subtitle1,
                                    text = "Миллилитр",
                                    modifier = Modifier
                                        .height(44.dp)
                                        .fillMaxWidth()
                                        .clickable {
                                            vm.updateUnits("мл")
                                            openDialogUnits = false
                                        }
                                )
                                Text(
                                    style = MaterialTheme.typography.subtitle1,
                                    text = "Штука",
                                    modifier = Modifier
                                        .height(44.dp)
                                        .fillMaxWidth()
                                        .clickable {
                                            vm.updateUnits("шт")
                                            openDialogUnits = false
                                        }
                                )
                            }
                        }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clickable { vm.showCreateReceptDialog() }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Добавьте рецепт, кол-во",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.subtitle2
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                    tint = MaterialTheme.colors.secondary,
                    contentDescription = "Наличие"
                )
            }

            if (state.recepts.isNotEmpty()) {
                Box(modifier = Modifier.heightIn(0.dp, 3000.dp)) {
                    LazyColumn() {
                        items(state.recepts, { it.id }) { item ->

                            val dismissState = rememberDismissState()

                            if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                                vm.removeProductRecept(item.id)
                            }
                            SwipeToDismiss(
                                state = dismissState,
                                directions = setOf(
                                    DismissDirection.StartToEnd
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
                                    ProductReceptItem(productReceptItem = item)
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clickable { vm.showCreateIngredientDialog() }
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


            if (state.ingredients.isNotEmpty()) {
                Box(modifier = Modifier.heightIn(0.dp, 3000.dp)) {
                    LazyColumn() {
                        items(state.ingredients, { it.id }) { item ->

                            val dismissState = rememberDismissState()

                            if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                                vm.removeProductIngredient(item.id)
                            }
                            SwipeToDismiss(
                                state = dismissState,
                                directions = setOf(
                                    DismissDirection.StartToEnd
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
                                    ProductIngredientItem(productIngredientItem = item)
                                }
                            )
                        }
                    }
                }
            }

            TextField(
                value = "",
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.subtitle1,
                placeholder = {
                    Text(
                        "Себестоимость, руб.",
                        style = MaterialTheme.typography.subtitle2,
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_calculate_24),
                            tint = MaterialTheme.colors.secondary,
                            contentDescription = "Калькулятор"
                        )
                    }
                },
                colors = colors
            )

            TextField(
                value = "${if (state.price == 0) "" else state.price}",
                onValueChange = {
                    try {
                        vm.updatePrice(if (it.isEmpty()) 0 else it.toInt())
                    } catch (e: Exception) {
                        ""
                    }
                },
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.subtitle1,
                placeholder = {
                    Text(
                        "Стоимость изделия, руб.",
                        style = MaterialTheme.typography.subtitle2
                    )
                },
                colors = colors
            )
        }


        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {

            BottomAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.height(56.dp)
            ) {

                Text(
                    "Главное ничего не перепутать)",
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.body1
                )

            }
        }
        FloatingActionButton(
            onClick = {
                vm.addProduct()
                navController.popBackStack()
            },
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(bottom = 28.dp, end = 16.dp),
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_done_24),
                contentDescription = "Добавить",
            )
        }
    }

    if (state.isCreateIngredientDialog) {
        CreateIngredientsDialog(
            onDismiss = { vm.hideCreateIngredientDialog() },
            onCreate = { title, count, availability, unitsAvailable ->
                vm.createProductIngredient(title, count, availability, unitsAvailable)
            },
            listIngredients = state.availableIngredients
        )
    }

    if (state.isCreateReceptDialog) {
        CreateReceptsDialog(
            onDismiss = { vm.hideCreateReceptDialog() },
            onCreate = { title, count, availability ->
                vm.createProductRecept(title, count, availability)
            },
            listRecepts = state.availableRecepts
        )
    }
}

@Composable
fun CreateReceptsDialog(
    listRecepts: List<ReceptItem>,
    onDismiss: () -> Unit,
    onCreate: (title: String, count: Int, availibility: Boolean) -> Unit
) {

    var selectionItem: String? by remember { mutableStateOf(null) }

    var selectionAvailability: Boolean? by remember { mutableStateOf(null) }

    var receptCount: Int by remember { mutableStateOf(0) }

    val colors = TextFieldDefaults.textFieldColors(
        textColor = colors.onPrimary,
        backgroundColor = colors.background,
        disabledTextColor = colors.background,
        placeholderColor = colors.background,
        disabledPlaceholderColor = colors.background,
        focusedIndicatorColor = colors.secondary,
        cursorColor = colors.onPrimary
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
                    text = "Выберете рецепт из списка",
                    style = MaterialTheme.typography.subtitle1
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .heightIn(0.dp, 300.dp)
                ) {
                    LazyColumn() {
                        listRecepts.forEach {
                            val backgroundColor =
                                if (selectionItem == it.title) Color.Green
                                else Color.Transparent

                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color = backgroundColor)
                                        .clickable(onClick = {
                                            selectionItem = it.title
                                            selectionAvailability = it.availability
                                        })
                                        .height(44.dp)
                                        .padding(end = 16.dp)
                                ) {
                                    Icon(
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
                                        text = "г.",
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                            }
                        }
                    }
                }

                TextField(
                    value = "${if (receptCount == 0) "" else receptCount}",
                    onValueChange = {
                        try {
                            receptCount = if (it.isBlank()) 0 else it.toInt()
                        } catch (e: NumberFormatException) {
                            receptCount = 0
                        }
                    },
                    textStyle = MaterialTheme.typography.body2,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth(),
                    label = {
                        Text(
                            "Кол-во для изделия",
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
                            selectionItem?.let {
                                onCreate(
                                    it,
                                    receptCount,
                                    selectionAvailability ?: true
                                )
                            }
                        },
                        enabled = selectionItem != null && receptCount > 0
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
fun ProductReceptItem(productReceptItem: ProductReceptItem) {
    Column(
        modifier = Modifier
            .background(color = colors.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(end = 16.dp)
                .background(color = colors.background),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = R.drawable.ic_baseline_circle_24),
                tint = if (productReceptItem.availability) colorResource(id = R.color.green)
                else colorResource(id = R.color.red),
                contentDescription = "Наличие"
            )
            Text(
                text = productReceptItem.title,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "${productReceptItem.count} г.",
                style = MaterialTheme.typography.subtitle1
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = colors.secondary
        )
    }
}

@Composable
fun ProductIngredientItem(productIngredientItem: ProductIngredientItem) {
    Column(
        modifier = Modifier
            .background(color = colors.background)
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
                tint = if (productIngredientItem.availability) colorResource(id = R.color.green)
                else colorResource(id = R.color.red),
                contentDescription = "Наличие"
            )
            Text(
                text = productIngredientItem.title,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "${productIngredientItem.count}",
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = productIngredientItem.unitsAvailable,
                style = MaterialTheme.typography.subtitle1
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = colors.secondary
        )
    }
}

/*
@Preview
@Composable
fun previewProduct() {
    AppTheme {
        ProductScreen()
    }
}

@Preview
@Composable
fun previewProductReceptCard() {
    AppTheme {
        ProdRecCard()
    }
}

@Preview
@Composable
fun previewProductIngredientCard() {
    AppTheme {
        ProdIngCard()
    }
}*/
