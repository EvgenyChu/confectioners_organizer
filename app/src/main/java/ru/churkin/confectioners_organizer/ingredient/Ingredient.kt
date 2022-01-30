package ru.churkin.confectioners_organizer.ingredient

import java.util.*


data class Ingredient(
    val id: Int,
    val title: String,
    val availability: Boolean,
    val available: Int = 0,
    val units: String,
    val costPrice: Int,
    val sellBy: Date? = null
) {
    companion object Factory {

        private var lastId: Int = -1

        fun makeIngredient(
            title: String,
            costPrice: Int,
            availability: Boolean,
            avaliable: Int,
            units: String,
            sellBy: Date?
        ): Ingredient {
            lastId += 1

            return Ingredient(
                id = lastId,
                title = title,
                costPrice = costPrice,
                availability = availability,
                available = avaliable,
                units = units,
                sellBy = Date()
            )
        }
    }
}