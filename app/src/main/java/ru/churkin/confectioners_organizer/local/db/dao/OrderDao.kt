package ru.churkin.confectioners_organizer.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient
import ru.churkin.confectioners_organizer.local.db.entity.Order
import ru.churkin.confectioners_organizer.local.db.entity.OrderFull

@Dao
interface OrderDao {

    @Query("SELECT * FROM orders")
    suspend fun loadAll() : List<Order>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: Order) : Long

    @Query("DELETE FROM orders WHERE id = :orderId")
    suspend fun delete(orderId: Long)

    @Query(
        """
        SELECT * FROM orders
        WHERE id = :id
    """
    )
    suspend fun loadOrderFull(id: Long): OrderFull
}