package ru.churkin.confectioners_organizer.repositories

import ru.churkin.confectioners_organizer.local.PrefManager
import ru.churkin.confectioners_organizer.view_models.ingredient.data.Ingredient

class IngredientsRepository {

    private val prefs = PrefManager


    fun loadIngredients(): List<Ingredient> = prefs.loadIngredients()
    fun insertIngredient(ingredient: Ingredient) {
        val newInd = prefs.loadIngredients().lastOrNull()?.let { it.id + 1 } ?: 0
        prefs.insertIngredient(ingredient.copy(id = newInd))
    }

    fun removeIngredient(id: Int) {
        val ingredients = prefs.loadIngredients()
        val index = ingredients.indexOfFirst { it.id == id }
        if (index == -1) return
        prefs.removeIngredient(id)
    }

    fun isEmptyIngredients() = prefs.loadIngredients().isEmpty()

    fun countIngredients() = prefs.loadIngredients().size
}