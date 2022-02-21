package ru.churkin.confectioners_organizer.view_models.recept

import kotlinx.serialization.Serializable

@Serializable
data class ReceptIngredientItem(
    val title: String,
    val availability: Boolean,
    val count: Int = 0
)