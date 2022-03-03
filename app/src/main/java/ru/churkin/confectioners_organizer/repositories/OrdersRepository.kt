package ru.churkin.confectioners_organizer.repositories

import ru.churkin.confectioners_organizer.local.db.AppDb
import ru.churkin.confectioners_organizer.local.db.dao.OrderDao
import ru.churkin.confectioners_organizer.local.db.dao.ProductDao
import ru.churkin.confectioners_organizer.local.db.dao.ReceptIngredientItemDao
import ru.churkin.confectioners_organizer.local.db.entity.Order
import ru.churkin.confectioners_organizer.local.db.entity.OrderFull
import ru.churkin.confectioners_organizer.local.db.entity.Product
import ru.churkin.confectioners_organizer.local.db.entity.ReceptIngredientItem

class OrdersRepository(
    val orderDao: OrderDao = AppDb.db.orderDao(),
    val productDao: ProductDao = AppDb.db.productDao()
) {
    suspend fun loadOrders(): List<Order> = orderDao.loadAll()
    suspend fun loadOrder(id: Long): OrderFull = orderDao.loadOrderFull(id)
    suspend fun insertOrder(order: Order, products: List<Product>) {
        val id = orderDao.insert(order = order)
        productDao.insertList(products = products.map { it.copy(orderId = id) })
    }

    suspend fun removeOrder(id: Long) {
        orderDao.delete(orderId = id)
    }

    suspend fun isEmptyOrders() = orderDao.loadAll().isEmpty()

    suspend fun createOrder() : Long  = orderDao.insert(Order())
}