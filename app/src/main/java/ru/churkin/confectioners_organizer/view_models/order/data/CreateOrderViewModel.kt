package ru.churkin.confectioners_organizer.view_models.order.data

import android.hardware.camera2.CameraManager
import android.util.Log
import androidx.compose.ui.text.capitalize
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

class CreateOrderViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private var id: Long? = savedStateHandle.get<Long>("id")
    private val repository: OrdersRepository = OrdersRepository()
    private val ordersProducts: MutableList<Product> = mutableListOf()
    private val _state: MutableStateFlow<OrderState> = MutableStateFlow(OrderState())

    val state: StateFlow<OrderState>
        get() = _state

    val currentState: OrderState
        get() = state.value

    suspend fun initState() {
        if (id == null && currentState.id == 0L) {
            val localId = repository.createOrder()
            _state.value = currentState.copy(id = localId)
            id = localId
        } else if (currentState == OrderState()) {

            val order = repository.loadOrder(checkNotNull(id))
            _state.value = currentState.copy(
                id = order.id,
                customer = order.customer,
                phone = order.phone,
                deadLine = order.deadline,
                needDelivery = order.needDelivery,
                address = order.address,
                price = order.price,
                isPaid = order.isPaid,
                note = order.note,
                isCooked = order.isCooked
            )
        }
        val products = repository.loadOrderProducts(currentState.id)
        val availableProducts =
            repository.loadOrderProducts(currentState.id).joinToString(",") { it.title }

        var availabilityProduct = true
            repository.loadOrderProducts(currentState.id).forEach {
                availabilityProduct = !(!it.availabilityIngredients || !it.availabilityRecepts)
        }

        _state.value = currentState.copy(
            availableProducts = availableProducts,
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

    fun emptyState() {
        _state.value = OrderState(id = currentState.id)
    }

    fun addOrder() {
        val order = currentState.toOrder()
        viewModelScope.launch {
            repository.insertOrder(order, ordersProducts)
        }
    }

    fun removeOrder(id: Long) {
        viewModelScope.launch {
            repository.removeOrder(id = id)
            repository.loadOrders()
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
    val isPaid: Boolean = false,
    val note: String? = "",
    var isCooked: Boolean = false,
    val availabilityProduct: Boolean = true
)

fun OrderState.toOrder() = Order(
    id,
    customer,
    phone,
    deadLine,
    needDelivery,
    address,
    price,
    isPaid,
    note,
    isCooked,
    availableProducts,
    availabilityProduct
)

