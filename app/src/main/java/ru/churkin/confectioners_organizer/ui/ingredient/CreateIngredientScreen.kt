package ru.churkin.confectioners_organizer.ui.ingredient

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
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
                label = "????????????????????????"
            )

            ToggleItem(
                text = if (state.availability) "?? ??????????????" else "??????????????????????",
                value = state.availability,
                onValueChange = { vm.updateAvailability(it) }
            )

            Divider(color = MaterialTheme.colors.surface)

            ActionItem(
                if (state.available == 0) "" else "${state.available}",
                "????????????????????",
                onValueChange = { vm.updateAvailable(it) },
                inputType = KeyboardType.Number,
                tailIcon = R.drawable.ic_baseline_keyboard_arrow_down_24,
                onTailClick = { openDialogUnits = true },
                optionsItem = state.unitsAvailable
            )

            ActionItem(
                state._costPrice,
                state.errors["costPrice"] ?: "????????",
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
                label = "?????????? ????: ????.????.????????",
                actions = listOf(
                    IconButton(
                        icon = R.drawable.ic_baseline_calendar_month_24,
                    action = { isShowDatePicker = true },
                    tint = MaterialTheme.colors.secondary))
            )
        }

        ParamsBottomBar(
            text = "????????, ?????? ???????????????? ??????????????????)"
        )

        MainButton(
            tailIcon = R.drawable.ic_baseline_done_24,
            modifier = Modifier.align(alignment = Alignment.BottomEnd)
        ){
            vm.addIngredient()
        navController.navigate(Screen.Ingredients.route)
        }

    }
    if (openDialogUnits) {
        DialogItem(
            onDismiss = { openDialogUnits = false },
        actions = listOf(
            DialogAction(
                text = "??????????",
                action = {
                    vm.updateUnitsAvailable("??.")
                    openDialogUnits = false
                }),
            DialogAction(
                text = "??????????????????",
                action = {
                    vm.updateUnitsAvailable("????")
                    openDialogUnits = false
                }),
            DialogAction(
                text = "??????????",
                action = {
                    vm.updateUnitsAvailable("????")
                    openDialogUnits = false
                })
        ))
    }

    if (openDialogUnitsPrice) {

        DialogItem(
            onDismiss = { openDialogUnitsPrice = false },
            actions = listOf(
                DialogAction(
                    text = "??????./??.",
                    action = {
                        vm.updateUnitsPrice("??????./??.")
                        openDialogUnitsPrice = false
                    }),
                DialogAction(
                    text = "??????./????",
                    action = {
                        vm.updateUnitsPrice("??????./????")
                        openDialogUnitsPrice = false
                    }),
                DialogAction(
                    text = "??????./????.",
                    action = {
                        vm.updateUnitsPrice("??????./????.")
                        openDialogUnitsPrice = false
                    })
            ))
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
            if (isCreate) "?????????? ????????????????????" else "????????????????????????????"
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
