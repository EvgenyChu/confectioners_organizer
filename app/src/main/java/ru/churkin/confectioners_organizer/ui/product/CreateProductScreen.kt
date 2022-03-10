package ru.churkin.confectioners_organizer.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun CreateProductScreen(navController: NavController, vm: CreateProductViewModel = viewModel()) {

    val state by vm.state.collectAsState()
    val isCreate: Boolean by remember {
        mutableStateOf(navController.currentDestination?.route == "products/create")
    }
    val title by remember {
        mutableStateOf(
            if (isCreate) "Новое изделие" else "Редактирование"
        )
    }

    var openDialogUnits by remember { mutableStateOf(false) }

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
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
                IconButton(onClick = {
                    navController.popBackStack()
                    if (isCreate) vm.removeProduct(state.id)
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
                    onValueChange = { vm.updateWeight(if (it.isEmpty()) 0 else it.toInt()) },
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
                        "ед. изм.",
                        modifier = Modifier.padding(top = 16.dp),
                        style = MaterialTheme.typography.subtitle2
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

            if (state.recepts.isNotEmpty()) state.recepts.forEach {
                ProductReceptItem(productReceptItem = it)
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

            if (state.ingredients.isNotEmpty()) state.ingredients.forEach {
                ProductIngredientItem(productIngredientItem = it)
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
                onValueChange = {vm.updatePrice(if (it.isEmpty()) 0 else it.toInt())},
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
            onCreate = { title, count, availability ->
                vm.createProductIngredient(title, count, availability)
            },
            listIngredients = state.availableIngredients
        )
    }

    if (state.isCreateReceptDialog) {
        CreateReceptsDialog(
            onDismiss = { vm.hideCreateReceptDialog() },
            onCreate = { title, count ->
                vm.createProductRecept(title, count)
            },
            listRecepts = state.availableRecepts
        )
    }
}

@Composable
fun CreateReceptsDialog(
    listRecepts: List<ReceptItem>,
    onDismiss: () -> Unit,
    onCreate: (title: String, count: Int) -> Unit
) {

    var selectionItem: String? by remember { mutableStateOf(null) }

    var receptCount: Int by remember { mutableStateOf(0) }

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
                    text = "Выберете рецепт из списка",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier
                    .width(300.dp)
                    .height(300.dp)) {
                    LazyColumn() {
                        listRecepts.forEach {
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
                    value = "$receptCount",
                    onValueChange = { receptCount = if (it.isBlank()) 0 else it.toInt() },
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
                            selectionItem?.let { onCreate(it, receptCount) }
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
            .background(color = MaterialTheme.colors.background)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .height(56.dp)
                .background(color = MaterialTheme.colors.background),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = productReceptItem.title,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "${productReceptItem.count}",
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

@Composable
fun ProductIngredientItem(productIngredientItem: ProductIngredientItem) {
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
                tint = colorResource(id = R.color.green),
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
