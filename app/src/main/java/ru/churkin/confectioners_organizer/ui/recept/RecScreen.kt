package ru.churkin.confectioners_organizer.recept

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.ui.theme.AppTheme
import ru.churkin.confectioners_organizer.view_models.recept.ReceptViewModel

@Composable
fun RecScreen(vm: ReceptViewModel = viewModel()) {

    val state by vm.state.collectAsState()

    AppTheme() {
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
                TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Назад"
                        )
                    }
                    Text(
                        "Новый рецепт",
                        style = MaterialTheme.typography.h6,
                    )
                    Spacer(Modifier.weight(1f, true))

                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "Очистить"
                        )
                    }
                }

                TextField(
                    value = state.title,
                    onValueChange = { vm.updateTitle(it) },
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth(),
                    textStyle = MaterialTheme.typography.subtitle1,
                    placeholder = {
                        Text(
                            "Название рецепта",
                            style = MaterialTheme.typography.subtitle2
                        )
                    },
                    colors = colors
                    )


                TextField(
                    value = "${if (state.weight == 0) "" else state.weight}",
                    onValueChange = { vm.updateWeight(if (it.isEmpty()) 0 else it.toInt()) },
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth(),
                    textStyle = MaterialTheme.typography.subtitle1,
                    placeholder = {
                        Text(
                            "Выход, грамм",
                            style = MaterialTheme.typography.subtitle2
                        )
                    },
                    colors = colors
                    )

                TextField(
                    value = "${if (state.time == 0) "" else state.time}",
                    onValueChange = {vm.updateTime(if (it.isEmpty()) 0 else it.toInt()) },
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth(),
                    textStyle = MaterialTheme.typography.subtitle1,
                    placeholder = {
                        Text(
                            "Время приготовления, мин.",
                            style = MaterialTheme.typography.subtitle2
                        )
                    },
                    colors = colors
                )


                TextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.subtitle1,
                    placeholder = {
                        Text(
                            "Добавьте ингредиент, кол-во",
                            style = MaterialTheme.typography.subtitle2,
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                                tint = MaterialTheme.colors.secondary,
                                contentDescription = "Наличие"
                            )
                        }
                    },
                    colors = colors
                )

                TextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth(),
                    textStyle = MaterialTheme.typography.subtitle1,
                    placeholder = {
                        Text(
                            "Примечание",
                            style = MaterialTheme.typography.subtitle2
                        )
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
                        "Рецептов много не бывает)",
                        modifier = Modifier.padding(start = 12.dp),
                        style = MaterialTheme.typography.body1
                    )

                }
            }
            FloatingActionButton(
                onClick = { },
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .padding(bottom = 28.dp, end = 16.dp),
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.secondary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_check_circle_outline_24),
                    modifier = Modifier
                        .size(64.dp),
                    contentDescription = "Добавить",
                    tint = MaterialTheme.colors.secondary
                )
            }
        }
    }
}

@Preview
@Composable
fun previewRec() {
    AppTheme {
        RecScreen()
    }
}