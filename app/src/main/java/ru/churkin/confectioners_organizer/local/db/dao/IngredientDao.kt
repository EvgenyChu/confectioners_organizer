package ru.churkin.confectioners_organizer.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient

@Dao
interface IngredientDao {
    @Query("SELECT * FROM ingredients")
    suspend fun loadAll() : List<Ingredient>
    @Insert
    suspend fun insert(ingredient: Ingredient)
    @Query("DELETE FROM ingredients WHERE id = :ingredientId")
    suspend fun delete(ingredientId: Int)
}