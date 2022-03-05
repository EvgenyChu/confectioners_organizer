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
    val needDelivery: Boolean = false,
    val address: String? = null,
    val price: Int = 0,
    val isPaid: Boolean = false,
    val note: String? = "",
    var isCooked: Boolean = false
)

data class OrderItem(
    val id: Long = 0,
    val customer: String = "",
    val phone: String? = null,
    val deadline: Date? = null,
    val needDelivery: Boolean = false,
    val price: Int = 0,
    val isPaid: Boolean = false
)