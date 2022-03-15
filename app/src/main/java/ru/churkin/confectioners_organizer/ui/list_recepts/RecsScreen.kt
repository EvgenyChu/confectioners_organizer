package ru.churkin.confectioners_organizer.ui.list_recepts

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.local.db.entity.Recept
import ru.churkin.confectioners_organizer.ui.theme.AppTheme
import ru.churkin.confectioners_organizer.view_models.list_recepts.ReceptsState
import ru.churkin.confectioners_organizer.view_models.list_recepts.RecsViewModel

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun RecsScreen(navController: NavController, vm: RecsViewModel = viewModel()) {

    val state by vm.state.collectAsState()
    val searchText by vm.searchText.collectAsState()
    var isShowSearch by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {

        Column(
            Modifier
                .fillMaxSize()
        ) {
            TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
                if (isShowSearch) {
                    SearchBar(
                        searchText = searchText,
                        onSearch = { vm.searchRecepts(it) },
                        onSubmit = {
                            vm.searchRecepts(it)
                            isShowSearch = false
                        },
                        onDismiss = { isShowSearch = false })
                } else {

                    IconButton(onClick = { navController.navigate("orders") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_dehaze_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Меню навигации"
                        )
                    }
                    Text(
                        "Список рецептов",
                        style = MaterialTheme.typography.h6,
                    )
                    Spacer(Modifier.weight(1f, true))

                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_circle_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Сортировка"
                        )
                    }

                    IconButton(onClick = {
                        isShowSearch = true
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_search_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Найти"
                        )
                    }
                }
            }
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
                            color = MaterialTheme.colors.secondary)
                    }
                }

                is ReceptsState.Value -> {
                    LazyColumn(contentPadding = PaddingValues(bottom = 56.dp)) {
                        items(listState.recepts, { it.id }) { item ->

                            val dismissState = rememberDismissState()
                            if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                                vm.removeRecept(item.id)
                            }
                            SwipeToDismiss(
                                state = dismissState,
                                directions = setOf(
                                    DismissDirection.StartToEnd,
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
                                    ReceptItem(recept = item, onClick = { id ->
                                        navController.navigate("recepts/$id")
                                    })
                                }
                            )
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
        FloatingActionButton(
            onClick = { navController.navigate("recepts/create") },
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(bottom = 28.dp, end = 16.dp),
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                contentDescription = "Добавить"
            )
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
                tint = colorResource(id = R.color.green),
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
