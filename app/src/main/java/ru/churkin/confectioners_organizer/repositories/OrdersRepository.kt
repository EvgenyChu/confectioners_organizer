package ru.churkin.confectioners_organizer.repositories

import ru.churkin.confectioners_organizer.local.db.AppDb
import ru.churkin.confectioners_organizer.local.db.dao.OrderDao
import ru.churkin.confectioners_organizer.local.db.dao.OrderProductItemDao
import ru.churkin.confectioners_organizer.local.db.dao.ProductDao
import ru.churkin.confectioners_organizer.local.db.dao.ReceptIngredientItemDao
import ru.churkin.confectioners_organizer.local.db.entity.*

class OrdersRepository(
    val orderDao: OrderDao = AppDb.db.orderDao(),
    val productDao: ProductDao = AppDb.db.productDao(),
    val orderProductItemDao: OrderProductItemDao = AppDb.db.orderProductItemDao()
) {
    suspend fun loadOrders(): List<Order> = orderDao.loadAll()
    suspend fun loadOrder(id: Long): Order = orderDao.loadOrder(id)
    suspend fun insertOrder(order: Order, products: List<OrderProductItem>) {
        val id = orderDao.insert(order = order)
        orderProductItemDao.insertList(products = products.map { it.copy(orderId = id) })
    }

    suspend fun removeOrder(id: Long) {
        orderDao.delete(orderId = id)
    }

    suspend fun isEmptyOrders() = orderDao.loadAll().isEmpty()

    suspend fun loadProducts(): List<Product> = productDao.loadAll()

    suspend fun loadOrderProducts(orderId: Long) =
        orderProductItemDao.loadOrderProducts(orderId)

    suspend fun insertOrderProductItem(orderProductItem: OrderProductItem) =
        orderProductItemDao.insert(orderProductItem = orderProductItem)

    suspend fun createOrder() : Long  = orderDao.insert(Order())

    suspend fun searchOrder(query: String) = orderDao.searchOrder(query)
}