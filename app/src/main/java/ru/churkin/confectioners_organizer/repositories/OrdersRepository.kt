package ru.churkin.confectioners_organizer.repositories

import ru.churkin.confectioners_organizer.local.db.AppDb
import ru.churkin.confectioners_organizer.local.db.dao.OrderDao
import ru.churkin.confectioners_organizer.local.db.dao.ProductDao
import ru.churkin.confectioners_organizer.local.db.dao.ProductIngredientItemDao
import ru.churkin.confectioners_organizer.local.db.dao.ProductReceptItemDao
import ru.churkin.confectioners_organizer.local.db.entity.*
import java.util.*

class OrdersRepository(
    val orderDao: OrderDao = AppDb.db.orderDao(),
    val productDao: ProductDao = AppDb.db.productDao(),
    val productIngredientItemDao: ProductIngredientItemDao = AppDb.db.productIngredientItemDao(),
    val productReceptItemDao: ProductReceptItemDao = AppDb.db.productReceptItemDao()
) {
    suspend fun loadOrders(): List<Order> = orderDao.loadAll()
    suspend fun loadOrdersIsCooked(isCooked: Boolean): List<Order> = orderDao.loadOrdersIsCooked(isCooked)
    suspend fun loadOrderFull(id: Long): OrderFull = orderDao.loadOrderFull(id)
    suspend fun loadOrder(id: Long): Order = orderDao.loadOrder(id)
    suspend fun insertOrder(order: Order, products: List<Product>) {
        val id = orderDao.insert(order = order)
        productDao.insertList(products = products.map { it.copy(orderId = id) })
    }

    suspend fun removeOrder(id: Long) {
        orderDao.delete(orderId = id)
    }

    suspend fun removeOrderProduct(id: Long) {
        productDao.deleteOrderProduct(id = id)
    }

    suspend fun removeOrderProductIngredient(id: Long) {
        productIngredientItemDao.deleteOrderProductIngredient(id = id)
    }

    suspend fun removeOrderProductRecept(id: Long) {
        productReceptItemDao.deleteOrderProductRecept(id = id)
    }

    suspend fun isEmptyOrders() = orderDao.loadAll().isEmpty()

    suspend fun loadOrderProducts(orderId: Long) =
        productDao.loadOrderProducts(orderId)

    suspend fun createOrder() : Long  = orderDao.insert(Order())

    suspend fun searchOrder(query: String) = orderDao.searchOrder(query)

    suspend fun findByDate(parseDate: Date): List<Order> = orderDao.findByDate(parseDate)
}