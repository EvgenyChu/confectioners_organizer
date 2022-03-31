package ru.churkin.confectioners_organizer.view_models.product.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.local.db.entity.Product
import ru.churkin.confectioners_organizer.local.db.entity.ProductIngredientItem
import ru.churkin.confectioners_organizer.local.db.entity.ProductReceptItem
import ru.churkin.confectioners_organizer.repositories.ProductsRepository
import ru.churkin.confectioners_organizer.view_models.recept.IngredientItem

class CreateProductViewModel() : ViewModel() {
    private val repository: ProductsRepository = ProductsRepository()
    private val productsIngredients: MutableList<ProductIngredientItem> = mutableListOf()
    private val productsRecepts: MutableList<ProductReceptItem> = mutableListOf()
    private val _state: MutableStateFlow<ProductState> = MutableStateFlow(ProductState())

    val state: StateFlow<ProductState>
        get() = _state

    val currentState: ProductState
        get() = state.value

    suspend fun initState(id: Long?, orderId: Long?) {
        _state.value = ProductState()
        viewModelScope.launch {
            if (id == null) {
                val localId = repository.createProduct(orderId = checkNotNull(orderId))
                _state.value = currentState.copy(
                    id = localId,
                    orderId = orderId,
                )
            } else {
                val product = repository.loadProduct(id)
                _state.value = currentState.copy(
                    id = product.id,
                    title = product.title,
                    weight = product.weight,
                    units = product.units,
                    costPrice = product.costPrice,
                    price = product.price,
                    orderId = checkNotNull(orderId)
                )
            }
            val ingredients = repository.loadProductIngredients(currentState.id)
            val availableIngredients =
                repository.loadIngredients().map { IngredientItem(it.title, it.availability, it.unitsAvailable, it.costPrice) }

            val recepts = repository.loadProductRecepts(currentState.id)
            val availableRecepts =
                repository.loadRecepts().map { ReceptItem(
                    it.title,
                    it.availabilityIngredients,
                    it.weight,
                    it.missingReceptIngredients,
                    it.costPrice
                ) }

            _state.value = currentState.copy(
                availableIngredients = availableIngredients,
                ingredients = ingredients,
                availableRecepts = availableRecepts,
                recepts = recepts
            )
        }
    }

    fun hideCreateIngredientDialog() {
        _state.value = currentState.copy(isCreateIngredientDialog = false)
    }

    fun hideCreateReceptDialog() {
        _state.value = currentState.copy(isCreateReceptDialog = false)
    }

    fun showCreateIngredientDialog() {
        _state.value = currentState.copy(isCreateIngredientDialog = true)
    }

    fun showCreateReceptDialog() {
        _state.value = currentState.copy(isCreateReceptDialog = true)
    }

    fun updateTitle(title: String) {
        _state.value = currentState.copy(title = title)
    }

    fun updateWeight(weight: String) {
        try{
            _state.value = currentState.copy(weight = if (weight.isEmpty()) 0 else weight.toInt())
        } catch(e: Exception){}
    }

    fun updateUnits(units: String) {
        _state.value = currentState.copy(units = units)
    }

    fun updatePrice(price: String) {
        try {
            _state.value = currentState.copy(price = if (price.isEmpty()) 0 else price.toInt())
        }
        catch (e: Exception) {}
    }

    fun updateCostPrice() {
                var costPrice = 0f
                val ingredients = currentState.ingredients
                val recepts = currentState.recepts
                ingredients.forEach { costPrice += it.costPrice * it.count }
                recepts.forEach { costPrice += it.costPrice * it.count }
            _state.value = currentState.copy(costPrice = costPrice.toInt())
    }

    fun emptyState() {
        _state.value = ProductState()
    }

    fun addProduct() {
        viewModelScope.launch {
            val missingIngredients = _state.value.missingProductReceptIngredients+_state.value.missingProductIngredients
            var costPrice = 0f
            val ingredients = repository.loadProductIngredients(currentState.id)
            val recepts = repository.loadProductRecepts(currentState.id)
            ingredients.forEach { costPrice += it.costPrice * it.count }
            recepts.forEach { costPrice += it.costPrice * it.count }
            val product = currentState.copy(missingIngredients = missingIngredients.distinct().joinToString(",") { it }, costPrice = costPrice.toInt()).toProduct()
            repository.insertProduct(product, productsIngredients, productsRecepts)
        }
    }

    fun createProductIngredient(title: String, count: Int, availability: Boolean, unitsAvailable: String, costPrice: Float) {
        viewModelScope.launch {
            val productIngredientItem =
                ProductIngredientItem(
                    title = title,
                    availability = availability,
                    count = count,
                    unitsAvailable = unitsAvailable,
                    productId = currentState.id,
                    costPrice = costPrice
                )
            repository.insertProductIngredientItem(productIngredientItem)
            val ingredients = repository.loadProductIngredients(currentState.id)
            var availabilityIngredients = true
            val missingProductIngredients = mutableSetOf<String>()
            ingredients.forEach {
                if (!it.availability)  {
                    availabilityIngredients = false
                    missingProductIngredients += it.title.lowercase()
                }}
            _state.value =
                currentState.copy(
                    ingredients = ingredients,
                    missingProductIngredients = missingProductIngredients.toSet(),
                    availabilityIngredients = availabilityIngredients,
                    isCreateIngredientDialog = false
                )
        }
    }

    fun createProductRecept(title: String, count: Int, availability: Boolean, missingReceptIngredients: String, costPrice: Float) {
        viewModelScope.launch {
            val productReceptItem =
                ProductReceptItem(
                    title = title,
                    count = count,
                    availability = availability,
                    productId = currentState.id,
                    missingReceptIngredients = missingReceptIngredients,
                    costPrice = costPrice
                )
            repository.insertProductReceptItem(productReceptItem)
            val recepts = repository.loadProductRecepts(currentState.id)
            var availabilityRecepts = true
            val missingProductReceptIngredients = mutableSetOf<String>()
            recepts.forEach {
                if (!it.availability)  {
                    availabilityRecepts = false
                    it.missingReceptIngredients.lowercase().split(",").forEach { ingredient ->
                        missingProductReceptIngredients += ingredient
                    }
                }}
            _state.value =
                currentState.copy(
                    recepts = recepts,
                    availabilityRecepts = availabilityRecepts,
                    missingProductReceptIngredients = missingProductReceptIngredients.toSet(),
                    isCreateReceptDialog = false
                )
        }
    }

    fun removeProduct(id: Long) {
        viewModelScope.launch {
            repository.removeProduct(id = id)
            repository.loadProducts()
        }
    }

    fun removeProductIngredient(id: Long) {
        Log.e("CreateProductViewModel", "$id")
        viewModelScope.launch {
            repository.removeProductIngredient(id = id)
            val ingredients = repository.loadProductIngredients(currentState.id)
            _state.value = currentState.copy(ingredients = ingredients)
        }
    }

    fun removeProductRecept(id: Long) {
        viewModelScope.launch {
            repository.removeProductRecept(id = id)
            val recepts = repository.loadProductRecepts(currentState.id)
            _state.value = currentState.copy(recepts = recepts)
        }
    }
}

data class ProductState(
    val id: Long = 0,
    val title: String = "",
    val weight: Int = 0,
    val units: String = "ед. изм.",
    val availableRecepts: List<ReceptItem> = emptyList(),
    val recepts: List<ProductReceptItem> = emptyList(),
    val availableIngredients: List<IngredientItem> = emptyList(),
    val ingredients: List<ProductIngredientItem> = emptyList(),
    val costPrice: Int = 0,
    val price: Int = 0,
    val isCreateIngredientDialog: Boolean = false,
    val isCreateReceptDialog: Boolean = false,
    val isConfirm: Boolean = false,
    val orderId: Long? = null,
    val availabilityIngredients: Boolean = true,
    val availabilityRecepts: Boolean = true,
    val missingProductReceptIngredients: Set<String> = setOf(),
    val missingProductIngredients: Set<String> = setOf(),
    val missingIngredients: String = ""
)

fun ProductState.toProduct() = Product(
    id,
    title,
    weight,
    units,
    costPrice,
    price,
    orderId,
    availabilityIngredients,
    availabilityRecepts,
    missingIngredients
)