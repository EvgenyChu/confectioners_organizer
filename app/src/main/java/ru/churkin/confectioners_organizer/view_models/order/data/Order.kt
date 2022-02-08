package ru.churkin.confectioners_organizer.view_models.order.data

import ru.churkin.confectioners_organizer.view_models.product.data.Product
import java.util.*

data class Order(
    val id: Int,
    val customer: String,
    val phone: String?,
    val deadline: Date,
    val delivery: Boolean,
    val addresss: String?,
    val product: List<Product>,
    val price: Int,
    val paid: Boolean,
    val note: String?,
    var implementation: Boolean
) {
    companion object Factory {

        private var lastId: Int = -1

        fun makeOrder(
            customer: String,
            phone: String?,
            deadline: Date,
            delivery: Boolean,
            addresss: String?,
            product: List<Product>,
            price: Int,
            paid: Boolean,
            note: String?,
            implementation: Boolean

        ): Order {
            lastId += 1

            return Order(
                id = lastId,
                customer = customer,
                phone = phone,
                deadline = deadline,
                delivery = delivery,
                addresss = addresss,
                product = product,
                price = price,
                paid = paid,
                note = note,
                implementation = false
            )
        }
    }
}