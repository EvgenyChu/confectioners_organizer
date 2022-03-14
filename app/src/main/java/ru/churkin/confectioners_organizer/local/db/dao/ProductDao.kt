package ru.churkin.confectioners_organizer.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.churkin.confectioners_organizer.local.db.entity.Product
import ru.churkin.confectioners_organizer.local.db.entity.ProductFull
import ru.churkin.confectioners_organizer.local.db.entity.ReceptFull
import ru.churkin.confectioners_organizer.local.db.entity.ReceptIngredientItem

@Dao
interface ProductDao {

        @Query("SELECT * FROM products")
        suspend fun loadAll() : List<Product>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(product: Product) : Long

        @Query("DELETE FROM products WHERE id = :id")
        suspend fun delete(id: Long)

        @Insert
        suspend fun insertList(products: List<Product>)

        @Query(
                """
        SELECT * FROM products
        WHERE id = :id
    """
        )
        suspend fun loadProductFull(id: Long): ProductFull
    }