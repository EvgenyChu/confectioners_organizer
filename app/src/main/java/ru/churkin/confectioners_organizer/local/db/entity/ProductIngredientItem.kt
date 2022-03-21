package ru.churkin.confectioners_organizer.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_ingredient_items")
data class ProductIngredientItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val id: Long = 0,
    val title: String,
    val availability: Boolean,
    val count: Int = 0,
    val unitsAvailable: String = "ед. изм.",
    // TODO: 25.02.2022 add forign key
    @ColumnInfo(name = "product_id")
    val productId: Long = 0
)