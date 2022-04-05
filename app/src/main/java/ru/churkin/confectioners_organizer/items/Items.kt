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
import ru.churkin.confectioners_organizer.ui.search_bar.SearchBar

@Composable
fun TextItem(
    text: String = "",
    spacer: List<RowSpacer> = emptyList(),
    icons: List<Icon> = emptyList()
) {
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
        spacer.forEach {
            Spacer(modifier = it.modifier.weight(1f, true))
        }
        icons.forEach {
            Icon(
                modifier = it.modifier.padding(start = 16.dp),
                painter = painterResource(id = it.icon),
                tint = it.tint,
                contentDescription = "Наличие"
            )
        }
    }
}

data class Icon(
    val icon: Int,
    val modifier: Modifier = Modifier,
    val tint: Color
)

data class RowSpacer(
    val modifier: Modifier = Modifier
)

@Composable
fun EditTextItem(
    value: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
    label: String = "",
    inputType: KeyboardType = KeyboardType.Text,
    actions: List<IconButton> = emptyList()
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

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        textStyle = MaterialTheme.typography.subtitle1,
        keyboardOptions = KeyboardOptions(keyboardType = inputType),
        label = {
            Text(
                label,
                style = MaterialTheme.typography.subtitle2,
            )
        },
        trailingIcon = {
            actions.forEach {
                IconButton(onClick = { it.action() }) {
                    Icon(
                        painter = painterResource(id = it.icon),
                        tint = it.tint,
                        contentDescription = "Календарь"
                    )
                }
            }
        },
        colors = colors
    )
}

data class IconButton(
    val icon: Int,
    val action: () -> Unit,
    val tint: Color
)

@Composable
fun ActionItem(
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
fun AdditableItem(
    modifier: Modifier = Modifier,
    onTailClick: () -> Unit,
    text: String = "",
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.subtitle2,
    icon: Int = R.drawable.ic_baseline_add_circle_outline_24
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
            style = style
        )
        Spacer(Modifier.weight(1f, true))
        Icon(
            painter = painterResource(id = icon),
            tint = MaterialTheme.colors.secondary,
            contentDescription = "Наличие"
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun SwipeItem(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
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
fun ToggleItem(
    text: String = "",
    value: Boolean,
    onValueChange: (Boolean) -> Unit,

    ) {
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
fun MainButton(
    tailIcon: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = {
            onClick()
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
fun SearchToolBar(
    searchText: String = "",
    text: String = "Список заказов",
    actions: List<ToolBarAction> = emptyList(),
    onNavigate: () -> Unit,
    onSearch: (String) -> Unit,
    onSubmit: (String) -> Unit,
    onSearchDismiss: () -> Unit
) {
    var isShowSearch by remember { mutableStateOf(false) }

    TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
        if (isShowSearch) {
            SearchBar(
                searchText = searchText,
                onSearch = { onSearch(it) },
                onSubmit = {
                    onSubmit(it)
                    isShowSearch = false
                },
                onDismiss = {
                    isShowSearch = false
                    onSearchDismiss()
                }
            )
        } else {
            IconButton(onClick = {
                onNavigate()
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

            actions.forEach {
                IconButton(onClick = { it.action() }) {
                    Icon(
                        painter = painterResource(id = it.icon),
                        tint = it.tint?.let { color -> colorResource(id = color) }
                            ?: MaterialTheme.colors.onPrimary,
                        contentDescription = "Календарь"
                    )
                }
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
}

data class ToolBarAction(
    val icon: Int,
    val action: () -> Unit,
    val tint: Int? = null
)

@Composable
fun ParamsToolBar(
    text: String = "",
    backIcon: Int = R.drawable.ic_baseline_arrow_back_24,
    editIcon: Int = R.drawable.ic_baseline_edit_24,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
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

        IconButton(onClick = { onEditClick() }) {
            Icon(
                painter = painterResource(id = editIcon),
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "Очистить"
            )
        }
    }
}

@Composable
fun ParamsBottomBar(
    text: String = "",
    icons: List<Icon> = emptyList()
){
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxHeight()
    ) {
        BottomAppBar(
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier.height(56.dp)
        ) {
            icons.forEach {
                Icon(
                    modifier = it.modifier.padding(start = 16.dp),
                    painter = painterResource(id = it.icon),
                    tint = it.tint,
                    contentDescription = "Будильник"
                )
            }
            Text(
                text,
                modifier = Modifier.padding(start = 12.dp),
                style = MaterialTheme.typography.body1
            )

        }
    }
}

@Composable
fun ParamsChoiceItem(
    choiceIcon: Int = R.drawable.ic_baseline_check_circle,
    tintIcon: Boolean,
    text: String,
    content: String = "Доставка"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(start = 16.dp),
            painter = painterResource(id = choiceIcon),
            tint = if (!tintIcon) MaterialTheme.colors.surface else MaterialTheme.colors.secondary,
            contentDescription = content
        )
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 16.dp),
            style = MaterialTheme.typography.subtitle1,
        )
    }
}

@Composable
fun RowChoiceItem(
    onClick: () -> Unit,
    tint: Color,
    textFirst: String,
    textSecond: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
            .padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier.padding(16.dp),
            painter = painterResource(id = R.drawable.ic_baseline_circle_24),
            tint = tint,
            contentDescription = "Наличие"
        )
        Text(
            text = textFirst,
            style = MaterialTheme.typography.subtitle1
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = textSecond,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun DialogItem(
    onDismiss: () -> Unit,
    actions: List<DialogAction> = emptyList(),
    text: String = "Выберите единицу измерения",
){
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(
                style = MaterialTheme.typography.h6,
                text = text,
            )
        },
        buttons = {
            Column(modifier = Modifier.padding(all = 16.dp)) {
                actions.forEach {
                    Text(
                        style = MaterialTheme.typography.subtitle1,
                        text = it.text,
                        modifier = Modifier
                            .height(44.dp)
                            .fillMaxWidth()
                            .clickable {
                                it.action()
                            }
                    )
                }
            }
        }
    )
}

data class DialogAction(
    val text: String,
    val action: () -> Unit,
)