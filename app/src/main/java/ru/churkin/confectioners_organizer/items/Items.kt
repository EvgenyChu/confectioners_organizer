package ru.churkin.confectioners_organizer.items

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.ui.date_picker.DatePicker
import ru.churkin.confectioners_organizer.ui.list_recepts.SearchBar

@Composable
fun ParamsTextItem(
    text: String = ""
){
    Row(
        Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun ParamsTextFieldItem(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    inputType: KeyboardType = KeyboardType.Text
){
    val colors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.onPrimary,
        backgroundColor = MaterialTheme.colors.background,
        disabledTextColor = MaterialTheme.colors.background,
        placeholderColor = MaterialTheme.colors.background,
        disabledPlaceholderColor = MaterialTheme.colors.background,
        focusedIndicatorColor = MaterialTheme.colors.secondary,
        cursorColor = MaterialTheme.colors.onPrimary
    )

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.subtitle1,
        keyboardOptions = KeyboardOptions(keyboardType = inputType),
        label = {
            Text(
                label,
                style = MaterialTheme.typography.subtitle2,
            )
        },
        colors = colors
    )
}

@Composable
fun ParamsItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    inputType: KeyboardType = KeyboardType.Text,
    optionsItem: String = "ед. изм.",
    tailIcon: Int,
    onValueChange: (String) -> Unit,
    onTailClick: () -> Unit,
    isError: Boolean = false
) {

    val colors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.onPrimary,
        backgroundColor = MaterialTheme.colors.background,
        disabledTextColor = MaterialTheme.colors.background,
        placeholderColor = MaterialTheme.colors.background,
        disabledPlaceholderColor = MaterialTheme.colors.background,
        focusedIndicatorColor = MaterialTheme.colors.secondary,
        cursorColor = MaterialTheme.colors.onPrimary
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(3f),
            textStyle = MaterialTheme.typography.subtitle1,
            keyboardOptions = KeyboardOptions(keyboardType = inputType),
            label = {
                Text(
                    label,
                    style = MaterialTheme.typography.subtitle2
                )
            },
            colors = colors,
            isError = isError
        )
        Box(
            Modifier
                .height(56.dp)
                .weight(1f)
                .clickable { onTailClick() }
        ) {
            Text(
                optionsItem,
                modifier = Modifier
                    .padding(top = 16.dp),
                style = MaterialTheme.typography.subtitle1
            )
        }
        IconButton(onClick = { onTailClick() }) {
            Icon(
                painter = painterResource(id = tailIcon),
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .padding(top = 14.dp)
                    .weight(1f),
                contentDescription = "Выбор ед.изм."
            )
        }
    }
}

@Composable
fun ParamsAddItem (
    modifier: Modifier = Modifier,
    onTailClick: () -> Unit,
    text: String = ""
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clickable { onTailClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.subtitle2
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
            tint = MaterialTheme.colors.secondary,
            contentDescription = "Наличие"
        )
    }
}

//fix this fun
@ExperimentalMaterialApi
@Composable
fun ParamsSwipeItem(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
){
    val dismissState = rememberDismissState()
    if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
        onDismiss()
    }

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(
            DismissDirection.StartToEnd
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
            content()
        }
    )
}

@Composable
fun ParamsSwitchItem(
    text: String = "",
    value: Boolean,
    onValueChange: (Boolean) -> Unit,

){
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text,
            style = MaterialTheme.typography.subtitle1
        )

        Spacer(Modifier.weight(1f, true))

        Switch(
            checked = value,
            onCheckedChange = {
                onValueChange(it)
            },
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color(0xFFE61610),
                uncheckedTrackColor = Color(0xFF840705),
                checkedThumbColor = Color(0xFF72BB53),
                checkedTrackColor = Color(0xFF4C7A34)
            )
        )
    }
}

@Composable
fun ParamsActionItem(
    tailIcon: Int,
    modifier: Modifier = Modifier,
    onTailClick: () -> Unit
){
    FloatingActionButton(
        onClick =  {
            onTailClick()
        },
        modifier = modifier
            .padding(bottom = 28.dp, end = 16.dp),
        backgroundColor = MaterialTheme.colors.secondary,
        contentColor = MaterialTheme.colors.onSecondary
    ) {
        Icon(
            painter = painterResource(id = tailIcon),
            contentDescription = "Добавить"
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ParamsToolDateBar(
    searchText: String = "",
    text: String = "Список заказов",
    onTailClick: () -> Unit,
    onSearch: (String) -> Unit,
    onSubmit: (String) -> Unit,
    onSearchDismiss: () -> Unit,
    onSelect:(String) -> Unit,
){
    var isShowSearch by remember { mutableStateOf(false) }
    var isShowDatePicker by remember { mutableStateOf(false) }

    TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
        if (isShowSearch) {
            SearchBar(
                searchText = searchText,
                onSearch = {onSearch(it)},
                onSubmit = {
                    onSubmit(it)
                    isShowSearch = false
                           },
                onDismiss = {
                    isShowSearch = false
                    onSearchDismiss()
                }
            ) } else {
            IconButton(onClick = {
                onTailClick()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_dehaze_24),
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = "Меню навигации"
                )
            }
            Text(
                text,
                style = MaterialTheme.typography.h6,
            )
            Spacer(Modifier.weight(1f, true))

            IconButton(onClick = { isShowDatePicker = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = "Календарь"
                )
            }

            IconButton(onClick = { isShowSearch = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_search_24),
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = "Найти"
                )
            }
        }
    }
    if (isShowDatePicker) DatePicker(
        onSelect = {
            onSelect(it)
            isShowDatePicker = false
        },
        onDismiss = {
            isShowDatePicker = false
        })
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ParamsToolSearchBar(
    searchText: String = "",
    text: String = "Ингредиенты",
    onTailClick: () -> Unit,
    onFilterClick: (Int) -> Unit,
    onSearch: (String) -> Unit,
    onSubmit: (String) -> Unit
){
    var isShowSearch by remember { mutableStateOf(false) }
    var counter by remember { mutableStateOf(0) }

    TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
        if (isShowSearch) {
            SearchBar(
                searchText = searchText,
                onSearch = { onSearch(it) },
                onSubmit = {
                    onSubmit(it)
                    isShowSearch = false
                },
                onDismiss = { isShowSearch = false })
        } else {
            IconButton(onClick = {
                onTailClick()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_dehaze_24),
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = "Меню навигации"
                )
            }
            Text(
                text,
                style = MaterialTheme.typography.h6,
            )
            Spacer(Modifier.weight(1f, true))

            IconButton(onClick = {
                counter++
                if (counter > 2) counter = 0
                onFilterClick(counter)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_circle_24),
                    tint = when (counter) {
                        1 -> colorResource(id = R.color.green)
                        2 -> colorResource(id = R.color.red)
                        else -> MaterialTheme.colors.onPrimary
                    },
                    contentDescription = "Сортировка"
                )
            }
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

@Composable
fun ParamsToolBar(
    text: String = "",
    backIcon: Int = R.drawable.ic_baseline_arrow_back_24,
    editIcon: Int = R.drawable.ic_baseline_edit_24,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
){
    TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                painter = painterResource(id = backIcon),
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "Назад"
            )
        }
        Text(
            text,
            style = MaterialTheme.typography.h6,
        )
        Spacer(Modifier.weight(1f, true))

        IconButton(onClick = { onEditClick()}) {
            Icon(
                painter = painterResource(id = editIcon),
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "Очистить"
            )
        }
    }
}