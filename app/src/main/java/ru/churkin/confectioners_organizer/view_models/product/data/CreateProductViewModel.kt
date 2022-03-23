package ru.churkin.confectioners_organizer.view_models.product.data

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.local.db.entity.*
import ru.churkin.confectioners_organizer.repositories.ProductsRepository
import ru.churkin.confectioners_organizer.view_models.recept.IngredientItem

class CreateProductViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private var id: Long? = savedStateHandle.get<Long>("id")
    private var orderId: Long? = savedStateHandle.get<Long>("order_id")
    private val repository: ProductsRepository = ProductsRepository()
    private val productsIngredients: MutableList<ProductIngredientItem> = mutableListOf()
    private val productsRecepts: MutableList<ProductReceptItem> = mutableListOf()
    private val _state: MutableStateFlow<ProductState> = MutableStateFlow(ProductState())

    val state: StateFlow<ProductState>
        get() = _state

    val currentState: ProductState
        get() = state.value

    suspend fun initState() {
        viewModelScope.launch {
            if (id == null) {
                val localId = repository.createProduct(orderId = checkNotNull(orderId))
                _state.value = currentState.copy(id = localId, orderId = orderId)
                id = localId
            } else {
                val product = repository.loadProduct(checkNotNull(id))
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
                repository.loadIngredients().map { IngredientItem(it.title, it.availability, it.unitsAvailable) }

            val recepts = repository.loadProductRecepts(currentState.id)
            val availableRecepts =
                repository.loadRecepts().map { ReceptItem(
                    it.title,
                    it.availabilityIngredients,
                    it.weight,
                    it.missingReceptIngredients
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

    fun updateWeight(weight: Int) {
        _state.value = currentState.copy(weight = weight)
    }

    fun updateUnits(units: String) {
        _state.value = currentState.copy(units = units)
    }

    fun updatePrice(price: Int) {
        _state.value = currentState.copy(price = price)
    }

    fun emptyState() {
        _state.value = ProductState()
    }

    fun addProduct() {
        val missingIngredients = _state.value.missingProductReceptIngredients+_state.value.missingProductIngredients
        Log.e("missingIngredients", "${missingIngredients.distinct()}")
            /*_state.value.missingProductReceptIngredients.joinToString(",") { it } + _state.value.missingProductIngredients.joinToString(",") { it }*/
        val product = currentState.copy(missingIngredients = missingIngredients.distinct().joinToString(",") { it }).toProduct()
        viewModelScope.launch {
            repository.insertProduct(product, productsIngredients, productsRecepts)
        }
    }

    fun createProductIngredient(title: String, count: Int, availability: Boolean, unitsAvailable: String) {
        viewModelScope.launch {
            val productIngredientItem =
                ProductIngredientItem(
                    title = title,
                    availability = availability,
                    count = count,
                    unitsAvailable = unitsAvailable,
                    productId = currentState.id
                )
            repository.insertProductIngredientItem(productIngredientItem)
            val ingredients = repository.loadProductIngredients(currentState.id)
            var availabilityIngredients: Boolean = true
            val missingProductIngredients = mutableSetOf<String>()
            ingredients.forEach {
                if (!it.availability)  {
                    availabilityIngredients = false
                    missingProductIngredients += it.title
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

    fun createProductRecept(title: String, count: Int, availability: Boolean, missingReceptIngredients: String) {
        viewModelScope.launch {
            val productReceptItem =
                ProductReceptItem(
                    title = title,
                    count = count,
                    availability = availability,
                    productId = currentState.id,
                    missingReceptIngredients = missingReceptIngredients
                )
            repository.insertProductReceptItem(productReceptItem)
            val recepts = repository.loadProductRecepts(currentState.id)
            var availabilityRecepts: Boolean = true
            val missingProductReceptIngredients = mutableSetOf<String>()
            recepts.forEach {
                if (!it.availability)  {
                    availabilityRecepts = false
                    missingProductReceptIngredients += it.missingReceptIngredients
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