package ru.churkin.confectioners_organizer.ui.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.churkin.confectioners_organizer.R
import ru.churkin.confectioners_organizer.ui.theme.AppTheme

@Composable
fun StartScreen() {
    AppTheme() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.background),
            contentAlignment = Alignment.CenterStart
        ) {

            Column(
                Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_big_cake_24),
                    tint = MaterialTheme.colors.secondary,
                    contentDescription = "Лейбл"
                )
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp))
                Text(
                    text = "Confectioner's organizer",
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}

@Preview
@Composable
fun previewStart() {
    AppTheme {
        StartScreen()
    }
}