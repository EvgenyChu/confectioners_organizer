package ru.churkin.confectioners_organizer.repositories

import ru.churkin.confectioners_organizer.local.db.AppDb
import ru.churkin.confectioners_organizer.local.db.dao.IngredientDao
import ru.churkin.confectioners_organizer.local.db.dao.ReceptDao
import ru.churkin.confectioners_organizer.local.db.dao.ReceptIngredientItemDao
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient
import ru.churkin.confectioners_organizer.local.db.entity.ReceptIngredientItem
import ru.churkin.confectioners_organizer.local.db.entity.Recept
import ru.churkin.confectioners_organizer.local.db.entity.ReceptFull

class ReceptsRepository(
    val ingredientDao: IngredientDao = AppDb.db.ingredientDao(),
    val receptDao: ReceptDao = AppDb.db.receptDao(),
    val receptIngredientItemDao: ReceptIngredientItemDao = AppDb.db.receptIngredientItemDao()
) {

    suspend fun loadRecepts(): List<Recept> = receptDao.loadAll()

    suspend fun loadRecept(id: Long): ReceptFull = receptDao.loadReceptFull(id)

    suspend fun insertRecept(recept: Recept, ingredients: List<ReceptIngredientItem>) {
        val id = receptDao.insert(recept = recept)
        receptIngredientItemDao.insertList(ingredients = ingredients.map { it.copy(receptId = id) })
    }

    suspend fun removeRecept(id: Long) {
        receptDao.delete(id = id)
    }

    suspend fun isEmptyRecepts() = receptDao.loadAll().isEmpty()

    suspend fun loadIngredients(): List<Ingredient> = ingredientDao.loadAll()

    suspend fun loadReceptIngredients(receptId: Long) =
        receptIngredientItemDao.loadReceptIngredients(receptId)

    suspend fun insertReceptIngredientItem(receptIngredientItem: ReceptIngredientItem) =
        receptIngredientItemDao.insert(receptIngredientItem = receptIngredientItem)

    suspend fun createRecept() : Long  = receptDao.insert(Recept())

}