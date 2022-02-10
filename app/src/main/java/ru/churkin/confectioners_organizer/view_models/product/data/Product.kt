package ru.churkin.confectioners_organizer.view_models.product.data

import ru.churkin.confectioners_organizer.view_models.ingredient.IngredientState
import ru.churkin.confectioners_organizer.view_models.recept.data.Recept

data class Product(
    val id: Int,
    val title: String,
    val weight: Int,
    val units: String,
    val listRecepts: List<Recept>,
    val listIngredients: List<IngredientState>,
    val costPrice: Int,
    val price: Int
) {
    companion object Factory {

        private var lastId: Int = -1

        fun makeProduct(
            title: String,
            weight: Int,
            units: String,
            listRecepts: List<Recept>,
            listIngredients: List<IngredientState>,
            costPrice: Int,
            price: Int
        ): Product {
            lastId += 1

            return Product(
                id = lastId,
                title = title,
                weight = weight,
                units = units,
                listRecepts = listRecepts,
                listIngredients = listIngredients,
                costPrice = costPrice,
                price = price
            )
        }
    }
}