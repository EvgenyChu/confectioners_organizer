package ru.churkin.confectioners_organizer.view_models.recept.data

import kotlinx.serialization.Serializable
import ru.churkin.confectioners_organizer.view_models.ingredient.IngredientState
import ru.churkin.confectioners_organizer.view_models.ingredient.data.Ingredient

@Serializable
data class Recept(
    val id: Int  = 0,
    val title: String = "",
    val weight: Int = 0,
    val time: Int = 0,
    val listIngredients: List<Ingredient>?,
    val note: String = "Примечание"
)