package ru.churkin.confectioners_organizer.listIngredients

import androidx.lifecycle.ViewModel
import ru.churkin.confectioners_organizer.ingredient.Ingredient

class IngsViewModel() : ViewModel() {

    data class IngsScreenState(
        val title: String = "Ингредиенты",
        val ings: List<Ingredient>,
        val ingsState: IngsState = IngsState.Empty,
        val isConfirm: Boolean = false,
        val ingridientsIdForRemove: Int? = null
    )
}