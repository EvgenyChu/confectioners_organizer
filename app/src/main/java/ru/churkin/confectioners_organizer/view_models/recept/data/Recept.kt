package ru.churkin.confectioners_organizer.view_models.recept.data

import kotlinx.serialization.Serializable
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient

data class Recept(
    val id: Int  = 0,
    val title: String = "",
    val weight: Int = 0,
    val time: Int = 0,
    val listIngredients: List<Ingredient>?,
    val note: String = "Примечание"
)