package ru.churkin.confectioners_organizer.ui.search_bar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import ru.churkin.confectioners_organizer.R

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
            label = {
                Text(
                    "Поиск",
                    style = MaterialTheme.typography.overline,
                    color = Color.Gray
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