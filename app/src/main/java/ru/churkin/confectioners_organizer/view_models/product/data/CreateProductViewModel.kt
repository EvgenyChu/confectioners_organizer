package ru.churkin.confectioners_organizer.view_models.product.data

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

    init {
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
                repository.loadIngredients().map { IngredientItem(it.title, it.availability) }

            val recepts = repository.loadProductRecepts(currentState.id)
            val availableRecepts =
                repository.loadRecepts().map { ReceptItem(it.title, it.weight) }

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
        val product = currentState.toProduct()
        viewModelScope.launch {
            repository.insertProduct(product, productsRecepts, productsIngredients)
        }
    }
    fun createProductIngredient(title: String, count: Int, availability: Boolean) {
        val productIngredientItem =
            ProductIngredientItem(
                title = title,
                availability = availability,
                count = count
            )
        productsIngredients.add(productIngredientItem)
        _state.value =
            currentState.copy(ingredients = productsIngredients.toList(), isCreateIngredientDialog = false)
    }

    fun createProductRecept(title: String, count: Int) {
        val productReceptItem =
            ProductReceptItem(
                title = title,
                count = count
            )
        productsRecepts.add(productReceptItem)
        _state.value =
            currentState.copy(recepts = productsRecepts.toList(), isCreateReceptDialog = false)
    }

    fun removeProduct(id: Long) {
        viewModelScope.launch{
            repository.removeProduct(id = id)
            repository.loadProducts()
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
    val orderId: Long? = null
)

fun ProductState.toProduct() = Product(id, title, weight, units, costPrice, price, orderId)