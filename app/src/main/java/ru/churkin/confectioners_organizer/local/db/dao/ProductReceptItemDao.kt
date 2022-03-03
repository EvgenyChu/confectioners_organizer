package ru.churkin.confectioners_organizer.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.churkin.confectioners_organizer.local.db.entity.ProductReceptItem

@Dao
interface ProductReceptItemDao {

    @Query("SELECT * FROM product_recept_items")
    suspend fun loadAll() : List<ProductReceptItem>

    @Query("""
        SELECT * FROM product_recept_items
        WHERE product_id = :id
    """)
    suspend fun loadProductRecepts(id: Long) : List<ProductReceptItem>

    @Insert
    suspend fun insert(productReceptItem: ProductReceptItem)

    @Query("DELETE FROM product_recept_items WHERE item_id = :id")
    suspend fun delete(id: Long)

    @Insert
    suspend fun insertList(recepts: List<ProductReceptItem>)
}