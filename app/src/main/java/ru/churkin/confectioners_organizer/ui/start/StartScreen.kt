package ru.churkin.confectioners_organizer.ui.start

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import ru.churkin.confectioners_organizer.R

@Composable
fun StartScreen(navController: NavController) {

    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.7f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
        )
        delay(3000L)
        navController.navigate("orders")
    }

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)) {
        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Icon(painter = painterResource(id = R.drawable.ic_baseline_big_cake_24),
                tint = MaterialTheme.colors.secondary,
                contentDescription = "Logo",
                modifier = Modifier.scale(scale.value)
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
            )
            Text(
                text = "Confectioner's organizer",
                style = MaterialTheme.typography.h6
            )
        }
    }
    }


/*
@Preview
@Composable
fun previewStart() {
    AppTheme {
        StartScreen()
    }
}*/
