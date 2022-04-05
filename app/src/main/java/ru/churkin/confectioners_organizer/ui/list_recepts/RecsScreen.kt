package ru.churkin.confectioners_organizer.ui.list_recepts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.InternalCoroutinesApi
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.RootActivity
import ru.churkin.confectioners_organizer.items.MainButton
import ru.churkin.confectioners_organizer.items.SearchToolBar
import ru.churkin.confectioners_organizer.items.SwipeItem
import ru.churkin.confectioners_organizer.items.ToolBarAction
import ru.churkin.confectioners_organizer.local.db.entity.Recept
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
        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {
            BottomAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.height(56.dp)
            ) {

                Text(
                    "Пора что-то менять)",
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.body1
                )

            }
        }

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { onClick(recept.id) }
                .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = R.drawable.ic_baseline_circle_24),
                tint = if (recept.availabilityIngredients) colorResource(id = R.color.green)
                else colorResource(id = R.color.red),
                contentDescription = "Наличие"
            )
            Text(
                text = recept.title,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "${recept.weight} г.",
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

@ExperimentalComposeUiApi
@Composable
fun SearchBar(
    searchText: String,
    onSearch: (String) -> Unit,
    onSubmit: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {
            onSearch("")
            onDismiss()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "Назад"
            )
        }
        TextField(
            value = searchText,
            onValueChange = { onSearch(it) },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.onPrimary,
                backgroundColor = MaterialTheme.colors.primary
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSubmit(searchText)
                keyboardController?.hide()
            }),
            textStyle = MaterialTheme.typography.h5,
            placeholder = {
                Text(
                    "Поиск",
                    style = MaterialTheme.typography.overline,
                    color = androidx.compose.ui.graphics.Color.Gray
                )
            }
        )
        IconButton(onClick = {
            if (searchText.isEmpty()) onDismiss() else onSearch("")
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_close_24),
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "Назад"
            )
        }
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


/*@Preview
@Composable
fun PreviwSearch() {
    AppTheme {
        SearchBar(searchText = "", onSearch = {}, onSubmit = {}) {

        }
    }
}*/

/*
@Preview
@Composable
fun previewRecs() {
    AppTheme {
        RecsScreen()
    }
}*/
