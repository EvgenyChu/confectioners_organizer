package ru.churkin.confectioners_organizer.recept

import ru.churkin.confectioners_organizer.ingredient.Ingredient
import java.util.*

data class Recept(
    val id: Int,
    val title: String,
    val weight: Int = 0,
    val time: Int = 0,
    val listIngredients: List<Ingredient>
) {
    companion object Factory {

        private var lastId: Int = -1

        fun makeIngredient(
            title: String,
            weight: Int,
            time: Int,
            listIngredients: List<Ingredient>

        ): Recept {
            lastId += 1

            return Recept(
                id = lastId,
                title = title,
                weight = weight,
                time = time,
                listIngredients = listIngredients
            )
        }
    }
}