package ru.churkin.confectioners_organizer.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.*

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val customer: String = "",
    val phone: String? = null,
    val deadline: Date? = null,
    val delivery: Boolean = false,
    val addresss: String? = null,
    val price: Int = 0,
    val paid: Boolean = false,
    val note: String? = "",
    var implementation: Boolean = false
)

data class OrderFull(
    val id: Long = 0,
    val customer: String = "",
    val phone: String? = null,
    val deadline: Date? = null,
    val delivery: Boolean = false,
    val addresss: String? = null,
    @Relation(
        parentColumn = "id",
        entityColumn = "order_id"
    )
    val product: List<Product>? = emptyList(),
    val price: Int = 0,
    val paid: Boolean = false,
    val note: String? = "",
    var implementation: Boolean = false

)