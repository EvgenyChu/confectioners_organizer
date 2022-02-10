package ru.churkin.confectioners_organizer.view_models.ingredient.data

import kotlinx.serialization.Serializable
import ru.churkin.confectioners_organizer.view_models.ingredient.DateSerializer
import java.util.*

@Serializable
data class Ingredient(
    val id: Int = 0,
    val title: String = "",
    val availability: Boolean = false,
    val available: Int = 0,
    val unitsAvailable: String = "ед. изм.",
    val unitsPrice: String = "рубль за ______",
    val costPrice: Float = 0f,
    @Serializable(with = DateSerializer::class)
    val sellBy: Date? = null
)