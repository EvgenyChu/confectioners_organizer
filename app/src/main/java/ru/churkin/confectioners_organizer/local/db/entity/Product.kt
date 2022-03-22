package ru.churkin.confectioners_organizer.local.db.entity

import androidx.room.*
import ru.churkin.confectioners_organizer.view_models.ingredient.IngredientState
import ru.churkin.confectioners_organizer.local.db.entity.Recept
import java.util.*

@Entity(
    tableName = "products",
   /* foreignKeys = [ForeignKey(
        entity = Order::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("order_id"),
        onDelete = ForeignKey.CASCADE
    )]*/
)
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val weight: Int = 0,
    val units: String = "",
    val costPrice: Int = 0,
    val price: Int = 0,
    @ColumnInfo(name = "order_id")
    val orderId: Long?,
    val availabilityIngredients: Boolean = true,
    val availabilityRecepts: Boolean = true,
    val missingIngredients: String = ""
)

data class ProductFull(
    val id: Long = 0,
    val title: String = "",
    val weight: Int = 0,
    val units: String = "",
    @Relation(
        parentColumn = "id",
        entityColumn = "product_id"
    )
    val listRecepts: List<ProductReceptItem>? = listOf(),
    @Relation(
        parentColumn = "id",
        entityColumn = "product_id"
    )
    val listIngredients: List<ProductIngredientItem>? = listOf(),
    val costPrice: Int = 0,
    val price: Int = 0,
    val orderId: Long?,
    val availabilityIngredients: Boolean = true,
    val availabilityRecepts: Boolean = true,
    val missingIngredients: String = ""
)