package ru.churkin.confectioners_organizer.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.churkin.confectioners_organizer.local.db.entity.ReceptIngredientItem

@Dao
interface ReceptIngredientItemDao {
    @Query("SELECT * FROM recept_ingredient_items")
    suspend fun loadAll() : List<ReceptIngredientItem>

    @Insert
    suspend fun insert(receptIngredientItem: ReceptIngredientItem)

    @Query("DELETE FROM recept_ingredient_items WHERE item_id = :receptIngredientItemId")
    suspend fun delete(receptIngredientItemId: Int)

    @Insert
    suspend fun insertList(ingredients: List<ReceptIngredientItem>)
}