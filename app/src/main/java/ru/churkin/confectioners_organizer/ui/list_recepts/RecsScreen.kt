package ru.churkin.confectioners_organizer.listRecepts

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.Screen
import ru.churkin.confectioners_organizer.local.db.entity.Recept
import ru.churkin.confectioners_organizer.view_models.list_recepts.ReceptsState
import ru.churkin.confectioners_organizer.view_models.list_recepts.RecsViewModel

@ExperimentalMaterialApi
@Composable
fun RecsScreen(navController: NavController, vm: RecsViewModel = viewModel()) {

    val state by vm.state.collectAsState()
    val listRecepts = remember { mutableStateListOf<Recept>() }

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
                IconButton(onClick = { }) {
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

                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_search_24),
                        tint = MaterialTheme.colors.onPrimary,
                        contentDescription = "Найти"
                    )
                }
            }
            when (val listState = state.receptsState) {

                is ReceptsState.Empty -> {}
                is ReceptsState.Loading -> {}

                is ReceptsState.Value -> {
                    LazyColumn {
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

                is ReceptsState.ValueWithMessage -> {}
            }
            Spacer(modifier = Modifier.height(56.dp))
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
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.secondary
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_add_circle_24),
                modifier = Modifier
                    .size(64.dp),
                contentDescription = "Добавить",
                tint = MaterialTheme.colors.secondary
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


/*
@Preview
@Composable
fun previewRecsCard() {
    AppTheme {
        RecsCard()
    }
}

@Preview
@Composable
fun previewRecs() {
    AppTheme {
        RecsScreen()
    }
}*/
