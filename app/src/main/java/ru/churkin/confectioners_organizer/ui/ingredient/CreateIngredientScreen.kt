package ru.churkin.confectioners_organizer.ui.ingredient

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.date.format
import ru.churkin.confectioners_organizer.ui.date_picker.DatePicker
import ru.churkin.confectioners_organizer.view_models.ingredient.CreateIngredientViewModel

@Composable
fun CreateIngredientScreen(
    navController: NavController,
    vm: CreateIngredientViewModel = viewModel()
) {

    val state by vm.state.collectAsState()
    val isCreate: Boolean by remember {
        mutableStateOf(navController.currentDestination?.route == "ingredients/create")
    }
    val title by remember {
        mutableStateOf(
            if (isCreate) "Новый ингредиент" else "Редактирование"
        )
    }

    var availabilityIngredient by remember { mutableStateOf("Отсутствует") }
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
                    availabilityIngredient = "Отсутствует"
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                        tint = MaterialTheme.colors.onPrimary,
                        contentDescription = "Очистить"
                    )
                }
            }
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
                    text = availabilityIngredient,
                    style = MaterialTheme.typography.subtitle1
                )

                Spacer(Modifier.weight(1f, true))

                Switch(
                    checked = state.availability,
                    onCheckedChange = {
                        vm.updateAvailability(it)
                        if (state.availability) availabilityIngredient = "Отсутствует"
                        else availabilityIngredient = "В наличии"
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                TextField(
                    value = "${if (state.available == 0) "" else state.available}",
                    onValueChange = { vm.updateAvailable(if (it.isEmpty()) 0 else it.toInt()) },
                    Modifier.weight(3f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.subtitle1,
                    placeholder = {
                        Text(
                            "Количество",
                            style = MaterialTheme.typography.subtitle2
                        )
                    },
                    colors = colors
                )

                Text(
                    state.unitsAvailable,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable { openDialogUnits = true },
                    style = if (state.unitsAvailable == "ед. изм.") MaterialTheme.typography.subtitle2
                    else MaterialTheme.typography.subtitle1
                )

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
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                TextField(
                    modifier = Modifier.weight(3f),
                    value = state._costPrice,
                    onValueChange = { vm.updatecostPrice(it) },
                    textStyle = MaterialTheme.typography.subtitle1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = state.errors["costPrice"]?.isNotEmpty() ?: false,
                    label = {
                        Text(
                            text = state.errors["costPrice"] ?: "Цена",
                            style = MaterialTheme.typography.subtitle2
                        )
                    },
                    colors = colors
                )

                Text(
                    state.unitsPrice,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable { openDialogUnitsPrice = true },
                    style = if (state.unitsPrice == "рубль за ______") MaterialTheme.typography.subtitle2
                    else MaterialTheme.typography.subtitle1
                )
                //}
                IconButton(onClick = { openDialogUnitsPrice = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_down_24),
                        tint = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .padding(top = 14.dp)
                            .weight(1f),
                        contentDescription = "Выбор ед.изм."
                    )
                }
                if (openDialogUnitsPrice)
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
                                    text = "Грамм",
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
                                    text = "Миллилитр",
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
                                    text = "Штука",
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
}


/*@Preview
@Composable
fun previewIng() {
    val navController = rememberNavController()
    AppTheme {
        IngScreen(navController = navController)
    }
}*/
