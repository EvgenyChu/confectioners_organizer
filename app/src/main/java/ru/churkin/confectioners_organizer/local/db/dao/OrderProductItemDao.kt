package ru.churkin.confectioners_organizer.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.churkin.confectioners_organizer.local.db.entity.OrderProductItem
import ru.churkin.confectioners_organizer.local.db.entity.ReceptIngredientItem

@Dao
interface OrderProductItemDao {
    @Query("SELECT * FROM order_product_items")
    suspend fun loadAll() : List<OrderProductItem>

    @Query("""
        SELECT * FROM order_product_items
        WHERE order_id = :id
    """)
    suspend fun loadOrderProducts(id: Long) : List<OrderProductItem>

    @Insert
    suspend fun insert(orderProductItem: OrderProductItem)

    @Query("DELETE FROM order_product_items WHERE item_id = :id")
    suspend fun delete(id: Long)

    @Insert
    suspend fun insertList(products: List<OrderProductItem>)
}