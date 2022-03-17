package ru.churkin.confectioners_organizer.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.churkin.confectioners_organizer.local.db.entity.ProductIngredientItem

@Dao
interface ProductIngredientItemDao {

    @Query("SELECT * FROM product_ingredient_items")
    suspend fun loadAll() : List<ProductIngredientItem>

    @Query("""
        SELECT * FROM product_ingredient_items
        WHERE product_id = :id
    """)
    suspend fun loadProductIngredients(id: Long) : List<ProductIngredientItem>

    @Insert
    suspend fun insert(productIngredientItem: ProductIngredientItem)

    @Query("DELETE FROM product_ingredient_items WHERE item_id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM product_ingredient_items WHERE product_id = :id")
    suspend fun deleteOrderProductIngredient(id: Long)

    @Insert
    suspend fun insertList(ingredients: List<ProductIngredientItem>)
}