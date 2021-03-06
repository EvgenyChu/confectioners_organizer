package ru.churkin.confectioners_organizer.product

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.InternalCoroutinesApi
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.RootActivity
import ru.churkin.confectioners_organizer.items.*
import ru.churkin.confectioners_organizer.local.db.entity.ProductIngredientItem
import ru.churkin.confectioners_organizer.local.db.entity.ProductReceptItem
import ru.churkin.confectioners_organizer.ui.recept.CreateIngredientsDialog
import ru.churkin.confectioners_organizer.ui.theme.Green
import ru.churkin.confectioners_organizer.ui.theme.Red
import ru.churkin.confectioners_organizer.view_models.product.data.CreateProductViewModel
import ru.churkin.confectioners_organizer.view_models.product.data.ReceptItem

@InternalCoroutinesApi
@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreateProductScreen(
    navController: NavController,
    id: Long?,
    orderId: Long?,
    vm: CreateProductViewModel = viewModel(
        LocalContext.current as RootActivity,
        key = "create_product"
    )
) {

    val state by vm.state.collectAsState()

    var openDialogUnits by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        vm.initState(id, orderId)
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
            EditTextItem(
                value = state.title,
                onValueChange = { vm.updateTitle(it) },
                label = "???????????????? ??????????????"
            )

            ActionItem(
                if (state.weight == 0) "" else "${state.weight}",
                "???????????????????? ??????????????",
                onValueChange = { vm.updateWeight(it) },
                inputType = KeyboardType.Number,
                tailIcon = R.drawable.ic_baseline_keyboard_arrow_down_24,
                onTailClick = { openDialogUnits = true },
                optionsItem = state.units
            )

            AdditableItem(
                onTailClick = { vm.showCreateReceptDialog() },
                text = "???????????????? ????????????, ??????-????"
            )

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

            AdditableItem(
                onTailClick = { vm.showCreateIngredientDialog() },
                text = "???????????????? ????????????????????, ??????-????"
            )

            if (openDialogUnits) {
                AlertDialog(
                    onDismissRequest = {
                        openDialogUnits = false
                    },
                    title = {
                        Text(
                            style = MaterialTheme.typography.h6,
                            text = "???????????????? ?????????????? ??????????????????",
                        )
                    },
                    buttons = {
                        Column(modifier = Modifier.padding(all = 16.dp)) {
                            Text(
                                style = MaterialTheme.typography.subtitle1,
                                text = "??????????", modifier = Modifier
                                    .height(44.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        vm.updateUnits("??.")
                                        openDialogUnits = false
                                    }
                            )
                            Text(
                                style = MaterialTheme.typography.subtitle1,
                                text = "??????????????????",
                                modifier = Modifier
                                    .height(44.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        vm.updateUnits("????")
                                        openDialogUnits = false
                                    }
                            )
                            Text(
                                style = MaterialTheme.typography.subtitle1,
                                text = "??????????",
                                modifier = Modifier
                                    .height(44.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        vm.updateUnits("????")
                                        openDialogUnits = false
                                    }
                            )
                        }
                    }
                )
            }

            if (state.ingredients.isNotEmpty()) {
                Box(modifier = Modifier.heightIn(0.dp, 3000.dp)) {
                    LazyColumn() {
                        items(state.ingredients, { it.id }) { item ->

                            SwipeItem(
                                onDismiss = { vm.removeProductIngredient(item.id) },
                            ){
                                ProductIngredientItem(productIngredientItem = item)
                            }
                        }
                    }
                }
            }

            Row(
                Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable {
                        vm.updateCostPrice()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "??????????????????????????: ${state.costPrice} ??????.",
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(Modifier.weight(1f, true))
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_calculate_24),
                    tint = MaterialTheme.colors.secondary,
                    contentDescription = "??????????????????????"
                )
            }

            EditTextItem(
                value = "${if (state.price == 0) "" else state.price}",
                onValueChange = { vm.updatePrice(it) },
                label = "?????????????????? ??????????????, ??????.",
                inputType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(56.dp))
        }

        ParamsBottomBar(
            text = "?????????????? ???????????? ???? ????????????????????)"
        )

        MainButton(
            tailIcon = R.drawable.ic_baseline_done_24,
            modifier = Modifier.align(alignment = Alignment.BottomEnd)
        ) {
            vm.addProduct()
            when (navController.currentDestination?.route) {
                "orders/{order_id}/products/create" -> navController.navigate("orders/edit/${state.orderId}")
                "orders/{order_id}/products/{id}" -> navController.navigate("orders/edit/${state.orderId}")
                "create_orders/{order_id}/products/create" -> navController.navigate("orders/create/${state.orderId}")
                "create_orders/{order_id}/products/{id}" -> navController.navigate("orders/create/${state.orderId}")
                else -> ""
            }
        }
    }

    if (state.isCreateIngredientDialog) {
        CreateIngredientsDialog(
            onDismiss = { vm.hideCreateIngredientDialog() },
            onCreate = { title, count, availability, unitsAvailable, costPrice ->
                vm.createProductIngredient(title, count, availability, unitsAvailable, costPrice)
            },
            listIngredients = state.availableIngredients
        )
    }

    if (state.isCreateReceptDialog) {
        CreateReceptsDialog(
            onDismiss = { vm.hideCreateReceptDialog() },
            onCreate = { title, count, availability, missingReceptIngredients, costPrice ->
                vm.createProductRecept(
                    title = title,
                    count = count,
                    availability = availability,
                    missingReceptIngredients = missingReceptIngredients,
                    costPrice = costPrice
                )
            },
            listRecepts = state.availableRecepts
        )
    }
}

@Composable
fun CreateReceptsDialog(
    listRecepts: List<ReceptItem>,
    onDismiss: () -> Unit,
    onCreate: (
        title: String,
        count: Int,
        availibility: Boolean,
        missingReceptIngredients: String,
        costPrice: Float
    ) -> Unit
) {

    var selectionItem: String? by remember { mutableStateOf(null) }

    var selectionAvailability: Boolean? by remember { mutableStateOf(null) }

    var selectionmissingIgredient: String? by remember { mutableStateOf(null) }

    var selectionCostPrice: Float by remember { mutableStateOf(0f) }

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
                    text = "???????????????? ???????????? ???? ????????????",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(horizontal = 16.dp)
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
                                            selectionmissingIgredient = it.missingReceptIngredients
                                            selectionCostPrice = it.costPrice
                                        })
                                        .height(44.dp)
                                        .padding(end = 16.dp)
                                ) {
                                    Icon(
                                        modifier = Modifier.padding(16.dp),
                                        painter = painterResource(id = R.drawable.ic_baseline_circle_24),
                                        tint = if (it.availability) Green else Red,
                                        contentDescription = "??????????????"
                                    )

                                    Text(
                                        text = it.title,
                                        style = MaterialTheme.typography.body2
                                    )
                                    Spacer(Modifier.weight(1f, true))

                                    Text(
                                        text = "??.",
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
                            "??????-???? ?????? ??????????????",
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
                        Text("????????????", color = MaterialTheme.colors.secondary)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = {
                            selectionItem?.let {
                                onCreate(
                                    it,
                                    receptCount,
                                    selectionAvailability ?: true,
                                    selectionmissingIgredient ?: "",
                                    selectionCostPrice
                                )
                            }
                        },
                        enabled = selectionItem != null && receptCount > 0
                    )
                    {
                        Text("????????????????", color = MaterialTheme.colors.secondary)
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
                .fillMaxWidth()
                .height(56.dp)
                .padding(end = 16.dp)
                .background(color = MaterialTheme.colors.background),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = R.drawable.ic_baseline_circle_24),
                tint = if (productReceptItem.availability) colorResource(id = R.color.green)
                else colorResource(id = R.color.red),
                contentDescription = "??????????????"
            )
            Text(
                text = productReceptItem.title,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "${productReceptItem.count} ??.",
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
                tint = if (productIngredientItem.availability) colorResource(id = R.color.green)
                else colorResource(id = R.color.red),
                contentDescription = "??????????????"
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
            color = MaterialTheme.colors.secondary
        )
    }
}

@InternalCoroutinesApi
@OptIn(ExperimentalMaterialApi::class)
@ExperimentalComposeUiApi
@Composable
fun CreateProductToolBar(
    navController: NavController,
    vm: CreateProductViewModel = viewModel(
        LocalContext.current as RootActivity,
        key = "create_product"
    )
) {
    val isCreate: Boolean by remember {
        mutableStateOf(
            navController.currentDestination?.route == "orders/{order_id}/products/{id}" ||
                    navController.currentDestination?.route == "create_orders/{order_id}/products/{id}"
        )
    }
    val state by vm.state.collectAsState()
    val title by remember {
        mutableStateOf(
            if (!isCreate) "?????????? ??????????????" else "????????????????????????????"
        )
    }

    ParamsToolBar(
        text = title,
        editIcon = R.drawable.ic_baseline_delete_24,
        onBackClick = {
            when (navController.currentDestination?.route) {
                "orders/{order_id}/products/create" -> navController.navigate("orders/edit/${state.orderId}")
                "orders/{order_id}/products/{id}" -> navController.navigate("orders/edit/${state.orderId}")
                "create_orders/{order_id}/products/create" -> navController.navigate("orders/create/${state.orderId}")
                "create_orders/{order_id}/products/{id}" -> navController.navigate("orders/create/${state.orderId}")
                else -> ""
            }
            if (!isCreate) vm.removeProduct(state.id)
        },
        onEditClick = { vm.emptyState() }
    )
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
