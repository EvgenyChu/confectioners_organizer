package ru.churkin.confectioners_organizer.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "order_product_items")
data class OrderProductItem (
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "item_id")
        val id: Long = 0,
        val title: String = "",
        val weight: Int = 0,
        val units: String = "",
        val costPrice: Int = 0,
        val price: Int = 0,
        @ColumnInfo(name = "order_id")
        val orderId: Long = 0
)
