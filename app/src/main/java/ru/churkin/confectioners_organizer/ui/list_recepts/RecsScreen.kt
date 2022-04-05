package ru.churkin.confectioners_organizer.ui.list_recepts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import ru.churkin.confectioners_organizer.items.*
import ru.churkin.confectioners_organizer.local.db.entity.Recept
import ru.churkin.confectioners_organizer.ui.theme.Green
import ru.churkin.confectioners_organizer.ui.theme.Red
import ru.churkin.confectioners_organizer.view_models.list_recepts.ReceptsState
import ru.churkin.confectioners_organizer.view_models.list_recepts.RecsViewModel

@OptIn(InternalCoroutinesApi::class)
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun RecsScreen(
    navController: NavController,
    vm: RecsViewModel = viewModel(LocalContext.current as RootActivity,key = "recepts")
) {
    val state by vm.state.collectAsState()

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
        ) {

            when (val listState = state) {

                is ReceptsState.Empty -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize(1f)
                    ) {
                        Text("Нет рецептов")
                    }
                }
                is ReceptsState.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize(1f)
                            .background(color = MaterialTheme.colors.background)
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colors.secondary
                        )
                    }
                }

                is ReceptsState.Value -> {
                    LazyColumn(contentPadding = PaddingValues(bottom = 56.dp)) {
                        items(listState.recepts, { it.id }) { item ->

                            SwipeItem(
                                onDismiss = { vm.removeRecept(item.id) },
                            ){
                                ReceptItem(recept = item, onClick = { id ->
                                    navController.navigate("recepts/$id")
                                })
                            }
                        }
                    }
                }
            }
        }

        ParamsBottomBar(
            text = "Пора что-то менять)"
        )

        MainButton(
            tailIcon = R.drawable.ic_baseline_add_24,
            modifier = Modifier.align(alignment = Alignment.BottomEnd)
        ) {
            navController.navigate("recepts/create")
        }
    }
}

@Composable
fun ReceptItem(recept: Recept, onClick: (Long) -> Unit) {

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
    ) {

        RowChoiceItem(
            onClick = {onClick(recept.id)},
            tint = if (recept.availabilityIngredients) Green else Red,
            textFirst = recept.title,
            textSecond = "${recept.weight} г."
        )

        Divider(color = MaterialTheme.colors.secondary)
    }
}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecsToolBar(
    vm: RecsViewModel = viewModel(LocalContext.current as RootActivity,key = "recepts"),
    onMenuClick: ()-> Unit
){
    val searchText by vm.searchText.collectAsState()
    var counter by remember { mutableStateOf(0) }

    SearchToolBar(
        searchText = searchText,
        text = "Рецепты",
        onNavigate = { onMenuClick() },
        actions = listOf(
            ToolBarAction(
                icon = R.drawable.ic_baseline_circle_24,
                tint = when (counter) {
                    1 -> R.color.green
                    2 -> R.color.red
                    else -> null
                },
                action = {
                    counter++
                    if (counter > 2) counter = 0
                    vm.filterRecepts(counter)
                })
        ),
        onSearch = { vm.searchRecepts(it) },
        onSubmit = { vm.searchRecepts(it) },
        onSearchDismiss = {}
    )
}

