package ru.churkin.confectioners_organizer.view_models.recept.data

import ru.churkin.confectioners_organizer.view_models.ingredient.data.Ingredient

data class Recept(
    val id: Int,
    val title: String,
    val weight: Int = 0,
    val time: Int = 0,
    val listIngredients: List<Ingredient>?,
    val note: String = "Примечание"
) {
    companion object Factory {

        private var lastId: Int = -1

        fun makeRecept(
            title: String,
            weight: Int,
            time: Int,
            listIngredients: List<Ingredient>,
            note: String

        ): Recept {
            lastId += 1

            return Recept(
                id = lastId,
                title = title,
                weight = weight,
                time = time,
                listIngredients = listIngredients,
                note = note
            )
        }
    }
}