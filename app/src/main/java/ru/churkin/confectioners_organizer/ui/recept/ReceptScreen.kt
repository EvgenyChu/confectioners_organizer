package ru.churkin.confectioners_organizer.ui.recept

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.churkin.confectioners_organizer.view_models.recept.ReceptViewModel

@Composable
fun ReceptScreen(navController: NavController, vm: ReceptViewModel = viewModel()){

    Text("Recept")
}