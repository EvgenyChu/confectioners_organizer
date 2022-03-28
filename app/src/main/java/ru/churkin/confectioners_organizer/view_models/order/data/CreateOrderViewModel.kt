package ru.churkin.confectioners_organizer.view_models.order.data

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.date.parseDate
import ru.churkin.confectioners_organizer.local.db.entity.Order
import ru.churkin.confectioners_organizer.local.db.entity.Product
import ru.churkin.confectioners_organizer.repositories.OrdersRepository
import java.util.*

class CreateOrderViewModel() : ViewModel() {
    private val repository: OrdersRepository = OrdersRepository()
    private val ordersProducts: MutableList<Product> = mutableListOf()
    private val _state: MutableStateFlow<OrderState> = MutableStateFlow(OrderState())

    val state: StateFlow<OrderState>
        get() = _state

    val currentState: OrderState
        get() = state.value

    suspend fun initState(id: Long?) {
        Log.e("OrderViewModel", "$id, ${currentState.id}")
        if (id == null) {
            val localId = repository.createOrder()
            val order = OrderState()
            _state.value = currentState.copy(
                id = localId,
                customer = order.customer,
                phone = order.phone,
                deadLine = null,
                needDelivery = order.needDelivery,
                address = order.address,
                price = order.price,
                costPrice = order.costPrice,
                isPaid = order.isPaid,
                note = order.note,
                isCooked = order.isCooked,
                availabilityProduct = order.availabilityProduct,
                missingIngredients = order.missingIngredients,
                availableProducts = order.availableProducts,
                products = order.products
            )
        } else {
            val order = repository.loadOrder(checkNotNull(id))
            _state.value = currentState.copy(
                id = order.id,
                customer = order.customer,
                phone = order.phone,
                deadLine = order.deadline,
                needDelivery = order.needDelivery,
                address = order.address,
                price = order.price,
                costPrice = order.costPrice,
                isPaid = order.isPaid,
                note = order.note,
                isCooked = order.isCooked
            )
        }
        val products = repository.loadOrderProducts(currentState.id)
        val availableProducts =
            repository.loadOrderProducts(currentState.id).joinToString(",") { it.title }

        val missingIngredients: MutableSet<String> = mutableSetOf()
        repository.loadOrderProducts(currentState.id).forEach {
            it.missingIngredients.split(",").forEach { ingredient ->
                missingIngredients += ingredient
            }
        }

        var availabilityProduct = true
        repository.loadOrderProducts(currentState.id).forEach {
            availabilityProduct = !(!it.availabilityIngredients || !it.availabilityRecepts)
        }

        _state.value = currentState.copy(
            availableProducts = availableProducts,
            missingIngredients = missingIngredients
                .joinToString(", ") { it },
            availabilityProduct = availabilityProduct,
            products = products
        )
    }

    fun updateCustomer(customer: String) {
        _state.value = currentState.copy(customer = customer)
    }

    fun updatePhone(phone: String?) {
        _state.value = currentState.copy(phone = phone)
    }

    fun updateDeadLine(deadLine: String) {
        if (deadLine.isEmpty()) return
        _state.value = currentState.copy(deadLine = deadLine.parseDate())
    }

    fun updateNeedDelivery(needDelivery: Boolean) {
        _state.value = currentState.copy(needDelivery = needDelivery)
    }

    fun updateAddress(address: String?) {
        _state.value = currentState.copy(address = address)
    }

    fun updatePrice(price: String) {
        try {
            _state.value = currentState.copy(price = price.toInt())
        } catch (e: Exception) {
        }
    }

    fun updateIsPaid(isPaid: Boolean) {
        _state.value = currentState.copy(isPaid = isPaid)
    }

    fun updateNote(note: String?) {
        _state.value = currentState.copy(note = note)
    }

    fun updateCostPrice() {
        var costPrice = 0
        val products = currentState.products
        products?.forEach { costPrice += it.costPrice }
        _state.value = currentState.copy(costPrice = costPrice)
    }

    fun emptyState() {
        _state.value = OrderState(id = currentState.id)
    }

    fun addOrder() {
        viewModelScope.launch {
            var costPrice = 0
            val products = repository.loadOrderProducts(currentState.id)
            products.forEach { costPrice += it.costPrice }
            val order = currentState.copy(costPrice = costPrice).toOrder()
            repository.insertOrder(order, ordersProducts)
        }
    }

    fun removeOrder(id: Long) {
        viewModelScope.launch {
            repository.removeOrder(id = id)
            repository.loadOrders()
        }
    }

    fun removeOrderProduct(id: Long) {
        viewModelScope.launch {
            repository.removeOrderProduct(id)
            repository.removeOrderProductIngredient(id)
            repository.removeOrderProductRecept(id)
            val products = repository.loadOrderProducts(currentState.id)
            val missingIngredients: MutableSet<String> = mutableSetOf()
            repository.loadOrderProducts(currentState.id).forEach {
                it.missingIngredients.split(",").forEach { ingredient ->
                    missingIngredients += ingredient
                }
            }
            val availableProducts =
                repository.loadOrderProducts(currentState.id).joinToString(",") { it.title }

            var availabilityProduct = true
            repository.loadOrderProducts(currentState.id).forEach {
                availabilityProduct = !(!it.availabilityIngredients || !it.availabilityRecepts)
            }
            _state.value = currentState.copy(
                products = products,
                availableProducts = availableProducts,
                missingIngredients = missingIngredients.joinToString(", ") { it },
                availabilityProduct = availabilityProduct
                )
        }
    }
}

data class OrderState(
    val id: Long = 0,
    val customer: String = "",
    val phone: String? = null,
    val deadLine: Date? = null,
    val needDelivery: Boolean = false,
    val address: String? = null,
    val availableProducts: String = "",
    val products: List<Product>? = listOf(),
    val price: Int = 0,
    val costPrice: Int = 0,
    val isPaid: Boolean = false,
    val note: String? = "",
    var isCooked: Boolean = false,
    val availabilityProduct: Boolean = true,
    val missingIngredients: String = ""
)

fun OrderState.toOrder() = Order(
    id,
    customer,
    phone,
    deadLine,
    needDelivery,
    address,
    price,
    costPrice,
    isPaid,
    note,
    isCooked,
    availableProducts,
    availabilityProduct,
    missingIngredients
)

