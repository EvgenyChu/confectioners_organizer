package ru.churkin.confectioners_organizer.ui.ingredient

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.InternalCoroutinesApi
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.RootActivity
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.date.format
import ru.churkin.confectioners_organizer.product.ParamsItem
import ru.churkin.confectioners_organizer.ui.date_picker.DatePicker
import ru.churkin.confectioners_organizer.view_models.ingredient.CreateIngredientViewModel

@InternalCoroutinesApi
@ExperimentalMaterialApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateIngredientScreen(
    navController: NavController,
    id: Long?,
    vm: CreateIngredientViewModel = viewModel(LocalContext.current as RootActivity,key = "create_ingredient")
) {

    val state by vm.state.collectAsState()

    var openDialogUnits by remember { mutableStateOf(false) }
    var openDialogUnitsPrice by remember { mutableStateOf(false) }
    var isShowDatePicker by remember { mutableStateOf(false) }


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
                .verticalScroll(rememberScrollState())
        ) {

            TextField(
                value = if (state.title == "") "" else state.title,
                onValueChange = { vm.updateTitle(it) },
                textStyle = MaterialTheme.typography.subtitle1,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        "Наименование",
                        style = MaterialTheme.typography.subtitle2
                    )
                },
                colors = colors
            )

            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (state.availability) "В наличии" else "Отсутствует",
                    style = MaterialTheme.typography.subtitle1
                )

                Spacer(Modifier.weight(1f, true))

                Switch(
                    checked = state.availability,
                    onCheckedChange = {
                        vm.updateAvailability(it)
                    },
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color(0xFFE61610),
                        uncheckedTrackColor = Color(0xFF840705),
                        checkedThumbColor = Color(0xFF72BB53),
                        checkedTrackColor = Color(0xFF4C7A34)
                    )
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = MaterialTheme.colors.surface
            )

            ParamsItem(
                if (state.available == 0) "" else "${state.available}",
                "Количество",
                onValueChange = { vm.updateAvailable(it) },
                inputType = KeyboardType.Number,
                tailIcon = R.drawable.ic_baseline_keyboard_arrow_down_24,
                onTailClick = { openDialogUnits = true },
                optionsItem = state.unitsAvailable
            )

            ParamsItem(
                state._costPrice,
                state.errors["costPrice"] ?: "Цена",
                onValueChange = { vm.updatecostPrice(it) },
                inputType = KeyboardType.Number,
                tailIcon = R.drawable.ic_baseline_keyboard_arrow_down_24,
                onTailClick = { openDialogUnitsPrice = true },
                optionsItem = state.unitsPrice,
                isError = state.errors["costPrice"]?.isNotEmpty() ?: false
            )

            TextField(
                value = state.sellBy?.format() ?: "",
                onValueChange = { vm.updateSellBy(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isShowDatePicker = true },
                textStyle = MaterialTheme.typography.subtitle1,
                enabled = false,
                placeholder = {
                    Text(
                        "Годен до: дд.мм.гггг",
                        style = MaterialTheme.typography.subtitle2
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { isShowDatePicker = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                            tint = MaterialTheme.colors.secondary,
                            contentDescription = "Наличие"
                        )
                    }
                    if (isShowDatePicker) DatePicker(
                        onSelect = {
                            vm.updateSellBy(it)
                            isShowDatePicker = false
                        },
                        onDismiss = { isShowDatePicker = false })
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
                    "Жаль, что продукты кончаются)",
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.body1
                )

            }
        }
        FloatingActionButton(
            onClick = {
                vm.addIngredient()
                navController.navigate(Screen.Ingredients.route)
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
                                vm.updateUnitsAvailable("г.")
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
                                vm.updateUnitsAvailable("мл")
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
                                vm.updateUnitsAvailable("шт")
                                openDialogUnits = false
                            }
                    )
                }
            }
        )
    }

    if (openDialogUnitsPrice) {
        AlertDialog(
            onDismissRequest = {
                openDialogUnitsPrice = false
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
                        text = "руб./г.",
                        modifier = Modifier
                            .height(44.dp)
                            .fillMaxWidth()
                            .clickable {
                                vm.updateUnitsPrice("руб./г.")
                                openDialogUnitsPrice = false
                            }
                    )
                    Text(
                        style = MaterialTheme.typography.subtitle1,
                        text = "руб./мл",
                        modifier = Modifier
                            .height(44.dp)
                            .fillMaxWidth()
                            .clickable {
                                vm.updateUnitsPrice("руб./мл")
                                openDialogUnitsPrice = false
                            }
                    )
                    Text(
                        style = MaterialTheme.typography.subtitle1,
                        text = "руб./шт.",
                        modifier = Modifier
                            .height(44.dp)
                            .fillMaxWidth()
                            .clickable {
                                vm.updateUnitsPrice("руб./шт.")
                                openDialogUnitsPrice = false
                            }
                    )
                }
            }
        )
    }
}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateIngredientToolBar(
    navController: NavController,
    vm: CreateIngredientViewModel = viewModel(LocalContext.current as RootActivity,key = "create_ingredient")
){
    val state by vm.state.collectAsState()
    val isCreate: Boolean by remember {
        mutableStateOf(navController.currentDestination?.route == "ingredients/create")
    }
    val title by remember {
        mutableStateOf(
            if (isCreate) "Новый ингредиент" else "Редактирование"
        )
    }

    TopAppBar() {
        IconButton(onClick = {
            navController.popBackStack()
            if (isCreate) vm.removeIngredient(state.id)
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

        IconButton(onClick = {
            vm.emptyState()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "Очистить"
            )
        }
    }
}

/*@Preview
@Composable
fun previewIng() {
    val navController = rememberNavController()
    AppTheme {
        IngScreen(navController = navController)
    }
}*/
