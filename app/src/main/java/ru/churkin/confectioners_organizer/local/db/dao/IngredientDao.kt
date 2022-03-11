package ru.churkin.confectioners_organizer.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient
import ru.churkin.confectioners_organizer.local.db.entity.Recept

@Dao
interface IngredientDao {

    @Query("SELECT * FROM ingredients")
    suspend fun loadAll() : List<Ingredient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ingredient: Ingredient) : Long

    @Query("DELETE FROM ingredients WHERE id = :ingredientId")
    suspend fun delete(ingredientId: Long)

    @Query(
        """
        SELECT * FROM ingredients
        WHERE id = :id
    """
    )
    suspend fun loadIngredient(id: Long): Ingredient

    @Query("SELECT * FROM ingredients WHERE title LIKE '%' || :search || '%'")
    suspend fun searchIngredient(search: String): List<Ingredient>
}