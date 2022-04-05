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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.InternalCoroutinesApi
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.RootActivity
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.date.format
import ru.churkin.confectioners_organizer.items.*
import ru.churkin.confectioners_organizer.ui.date_picker.DatePicker
import ru.churkin.confectioners_organizer.view_models.ingredient.CreateIngredientViewModel

@InternalCoroutinesApi
@ExperimentalMaterialApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateIngredientScreen(
    navController: NavController,
    id: Long?,
    vm: CreateIngredientViewModel = viewModel(
        LocalContext.current as RootActivity,
        key = "create_ingredient"
    )
) {

    val state by vm.state.collectAsState()

    var openDialogUnits by remember { mutableStateOf(false) }
    var openDialogUnitsPrice by remember { mutableStateOf(false) }
    var isShowDatePicker by remember { mutableStateOf(false) }

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
            EditTextItem(
                value = if (state.title == "") "" else state.title,
                onValueChange = { vm.updateTitle(it) },
                label = "Наименование"
            )

            ToggleItem(
                text = if (state.availability) "В наличии" else "Отсутствует",
                value = state.availability,
                onValueChange = { vm.updateAvailability(it) }
            )

            Divider(color = MaterialTheme.colors.surface)

            ActionItem(
                if (state.available == 0) "" else "${state.available}",
                "Количество",
                onValueChange = { vm.updateAvailable(it) },
                inputType = KeyboardType.Number,
                tailIcon = R.drawable.ic_baseline_keyboard_arrow_down_24,
                onTailClick = { openDialogUnits = true },
                optionsItem = state.unitsAvailable
            )

            ActionItem(
                state._costPrice,
                state.errors["costPrice"] ?: "Цена",
                onValueChange = { vm.updatecostPrice(it) },
                inputType = KeyboardType.Number,
                tailIcon = R.drawable.ic_baseline_keyboard_arrow_down_24,
                onTailClick = { openDialogUnitsPrice = true },
                optionsItem = state.unitsPrice,
                isError = state.errors["costPrice"]?.isNotEmpty() ?: false
            )

            EditTextItem(
                value = state.sellBy?.format() ?: "",
                modifier = Modifier
                    .clickable { isShowDatePicker = true },
                enabled = false,
                onValueChange = { vm.updateSellBy(it) },
                label = "Годен до: дд.мм.гггг",
                actions = listOf(
                    IconButton(
                        icon = R.drawable.ic_baseline_calendar_month_24,
                    action = { isShowDatePicker = true },
                    tint = MaterialTheme.colors.secondary))
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

        MainButton(
            tailIcon = R.drawable.ic_baseline_done_24,
            modifier = Modifier.align(alignment = Alignment.BottomEnd)
        ){
            vm.addIngredient()
        navController.navigate(Screen.Ingredients.route)
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

    if (isShowDatePicker) DatePicker(
        onSelect = {
            vm.updateSellBy(it)
            isShowDatePicker = false
        },
        onDismiss = { isShowDatePicker = false })
}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateIngredientToolBar(
    navController: NavController,
    vm: CreateIngredientViewModel = viewModel(
        LocalContext.current as RootActivity,
        key = "create_ingredient"
    )
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

    ParamsToolBar(
        text = title,
        editIcon = R.drawable.ic_baseline_delete_24,
        onBackClick = {
            navController.popBackStack()
            if (isCreate) vm.removeIngredient(state.id)
                      },
        onEditClick = { vm.emptyState() }
    )
}
