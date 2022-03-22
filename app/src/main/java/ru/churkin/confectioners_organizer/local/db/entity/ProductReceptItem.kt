package ru.churkin.confectioners_organizer.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_recept_items")
data class ProductReceptItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val id: Long = 0,
    val title: String,
    val count: Int = 0,
    val availability: Boolean,
    val missingReceptIngredients: String,
    // TODO: 25.02.2022 add forign key
    @ColumnInfo(name = "product_id")
    val productId: Long = 0
)