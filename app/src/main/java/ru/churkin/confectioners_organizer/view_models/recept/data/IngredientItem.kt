package ru.churkin.confectioners_organizer.view_models.recept

data class IngredientItem(
    val title: String,
    val availability: Boolean,
    val unitsAvailable: String
)