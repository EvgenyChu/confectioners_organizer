package ru.churkin.confectioners_organizer.view_models.product.data

data class ReceptItem (
    val title: String,
    val availability: Boolean,
    val weight: Int,
    val missingReceptIngredients: String,
    val costPrice: Float
        )