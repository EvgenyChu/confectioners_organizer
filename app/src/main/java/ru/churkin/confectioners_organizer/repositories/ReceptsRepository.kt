package ru.churkin.confectioners_organizer.repositories

import ru.churkin.confectioners_organizer.local.PrefManager
import ru.churkin.confectioners_organizer.view_models.ingredient.data.Ingredient
import ru.churkin.confectioners_organizer.view_models.recept.ReceptIngredientItem
import ru.churkin.confectioners_organizer.view_models.recept.data.Recept

class ReceptsRepository {

    private val prefs = PrefManager


    fun loadRecepts(): List<Recept> = prefs.loadRecepts()
    fun insertRecept(recept: Recept) {
        val newInd = prefs.loadRecepts().lastOrNull()?.let { it.id + 1 } ?: 0
        prefs.insertRecept(recept.copy(id = newInd))
    }

    fun removeRecept(id: Int) {
        val recepts = prefs.loadRecepts()
        val index = recepts.indexOfFirst { it.id == id }
        if (index == -1) return
        prefs.removeRecept(id)
    }

    fun isEmptyRecepts() = prefs.loadRecepts().isEmpty()

    fun countRecepts() = prefs.loadRecepts().size

    fun loadIngredients(): List<Ingredient> = prefs.loadIngredients()

    fun loadReceptIngredientItem(): List<ReceptIngredientItem> = prefs.loadReceptIngredientItem()

    fun insertReceptIngredientItem(receptIngredientItem: ReceptIngredientItem) = prefs.insertReceptIngredientItem(receptIngredientItem)
}