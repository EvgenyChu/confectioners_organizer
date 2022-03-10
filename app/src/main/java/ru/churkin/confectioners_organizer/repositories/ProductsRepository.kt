package ru.churkin.confectioners_organizer.repositories

import ru.churkin.confectioners_organizer.local.db.AppDb
import ru.churkin.confectioners_organizer.local.db.dao.*
import ru.churkin.confectioners_organizer.local.db.entity.*

class ProductsRepository(
    val ingredientDao: IngredientDao = AppDb.db.ingredientDao(),
    val receptDao: ReceptDao = AppDb.db.receptDao(),
    val productDao: ProductDao = AppDb.db.productDao(),
    val productReceptItemDao: ProductReceptItemDao = AppDb.db.productReceptItemDao(),
    val productIngredientItemDao: ProductIngredientItemDao = AppDb.db.productIngredientItemDao()
) {
    suspend fun loadProducts(): List<Product> = productDao.loadAll()
    suspend fun loadProduct(id: Long): ProductFull = productDao.loadProductFull(id)
    suspend fun insertProduct(
        product: Product,
        recepts: List<ProductReceptItem>,
        ingredients: List<ProductIngredientItem>
    ) {
        val id = productDao.insert(product = product)
        productIngredientItemDao.insertList(ingredients = ingredients.map { it.copy(productId = id) })
        productReceptItemDao.insertList(recepts = recepts.map { it.copy(productId = id) })
    }

    suspend fun removeProduct(id: Long) {
        productDao.delete(id = id)
    }

    suspend fun isEmptyProducts() = productDao.loadAll().isEmpty()

    suspend fun createProduct(): Long = productDao.insert(Product())

    suspend fun loadIngredients(): List<Ingredient> = ingredientDao.loadAll()

    suspend fun loadProductIngredients(productId: Long) =
        productIngredientItemDao.loadProductIngredients(productId)

    suspend fun loadRecepts(): List<Recept> = receptDao.loadAll()

    suspend fun loadProductRecepts(productId: Long) =
        productReceptItemDao.loadProductRecepts(productId)
}