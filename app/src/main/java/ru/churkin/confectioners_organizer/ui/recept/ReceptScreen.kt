package ru.churkin.confectioners_organizer.ui.recept

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.InternalCoroutinesApi
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.RootActivity
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.items.MainButton
import ru.churkin.confectioners_organizer.items.ParamsBottomBar
import ru.churkin.confectioners_organizer.items.ParamsToolBar
import ru.churkin.confectioners_organizer.items.TextItem
import ru.churkin.confectioners_organizer.view_models.recept.ReceptViewModel

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ReceptScreen(
    navController: NavController,
    id: Long,
    vm: ReceptViewModel = viewModel(LocalContext.current as RootActivity, key = "recept")
) {

    val state by vm.state.collectAsState()

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

                TextItem(state.title)

                Divider(color = MaterialTheme.colors.secondary)

                TextItem("??????????: ${state.weight} ??????????")

                Divider(color = MaterialTheme.colors.secondary)

                TextItem("?????????? ??????????????????????????: ${state.time} ??????.")

                Divider(color = MaterialTheme.colors.secondary)

                TextItem("???????????? ???????????????????????? ?????? ??????????????:")

                if (state.ingredients.isNotEmpty()) state.ingredients.forEach {
                    ReceptIngItem(receptIngredientItem = it)
                }

                TextItem("????????????????????: \n${state.note}")

                Divider(color = MaterialTheme.colors.secondary)

                Spacer(modifier = Modifier.height(56.dp))
            }
        }

        ParamsBottomBar(
            text = "?????? ?????????? ??????????????????????)"
        )

        MainButton(
            tailIcon = R.drawable.ic_baseline_done_24,
            modifier = Modifier.align(alignment = Alignment.BottomEnd)
        ){
            navController.navigate(Screen.Recepts.route)
        }
    }
}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ReceptToolBar(
    navController: NavController,
    vm: ReceptViewModel = viewModel(LocalContext.current as RootActivity, key = "recept")
) {
    val state by vm.state.collectAsState()

    ParamsToolBar(
        text = "P??????????",
        onBackClick = { navController.popBackStack() },
        onEditClick = { navController.navigate("recepts/edit/${state.id}") }
    )
}