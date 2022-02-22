package ru.churkin.confectioners_organizer.repositories

import ru.churkin.confectioners_organizer.local.PrefManager
import ru.churkin.confectioners_organizer.local.db.AppDb
import ru.churkin.confectioners_organizer.local.db.dao.IngredientDao
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient
import ru.churkin.confectioners_organizer.view_models.recept.ReceptIngredientItem
import ru.churkin.confectioners_organizer.view_models.recept.data.Recept

class ReceptsRepository(
    val ingredientDao: IngredientDao = AppDb.db.ingredientDao()
    val receptDao: IngredientDao = AppDb.db.ingredientDao()
) {

    private val prefs = PrefManager

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

    suspend fun loadIngredients(): List<Ingredient> = ingredientDao.loadAll()

    fun loadReceptIngredientItem(): List<ReceptIngredientItem> = prefs.loadReceptIngredientItem()

    fun insertReceptIngredientItem(receptIngredientItem: ReceptIngredientItem) = prefs.insertReceptIngredientItem(receptIngredientItem)
}