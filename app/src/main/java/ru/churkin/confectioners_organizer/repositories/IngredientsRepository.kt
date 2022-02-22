package ru.churkin.confectioners_organizer.repositories

import ru.churkin.confectioners_organizer.local.db.AppDb
import ru.churkin.confectioners_organizer.local.db.dao.IngredientDao
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient

class IngredientsRepository(
    private val ingredientDao: IngredientDao = AppDb.db.ingredientDao()
) {

    suspend fun loadIngredients(): List<Ingredient> = ingredientDao.loadAll()
    suspend fun insertIngredient(ingredient: Ingredient) {
        ingredientDao.insert(ingredient = ingredient)
    }

    suspend fun removeIngredient(id: Int) {
       ingredientDao.delete(ingredientId = id)
    }

    suspend fun isEmptyIngredients() = ingredientDao.loadAll().isEmpty()
}