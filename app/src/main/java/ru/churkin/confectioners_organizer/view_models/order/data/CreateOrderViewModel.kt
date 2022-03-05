package ru.churkin.confectioners_organizer.view_models.order.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.local.db.entity.Order
import ru.churkin.confectioners_organizer.local.db.entity.OrderProductItem
import ru.churkin.confectioners_organizer.local.db.entity.Product
import ru.churkin.confectioners_organizer.local.db.entity.ReceptIngredientItem
import ru.churkin.confectioners_organizer.repositories.OrdersRepository
import ru.churkin.confectioners_organizer.view_models.recept.IngredientItem
import ru.churkin.confectioners_organizer.view_models.recept.toRecept
import java.util.*

class CreateOrderViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private var id: Long? = savedStateHandle.get<Long>("id")
    private val repository: OrdersRepository = OrdersRepository()
    private val ordersProducts: MutableList<OrderProductItem> = mutableListOf()
    private val _state: MutableStateFlow<OrderState> = MutableStateFlow(OrderState())

    val state: StateFlow<OrderState>
        get() = _state

    val currentState: OrderState
        get() = state.value

    init {
        viewModelScope.launch {
            if (id == null) {
                val localId = repository.createOrder()
                _state.value = currentState.copy(id = localId)
                id = localId
            } else {
                val order = repository.loadOrder(checkNotNull(id))
                _state.value = currentState.copy(
                    id = order.id,
                    customer = order.customer,
                    phone = order.phone,
                    deadline = order.deadline,
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
                repository.loadProducts().map { ProductItem(it.title, it.weight, it.units, it.price) }

            _state.value = currentState.copy(
                availableProducts = availableProducts,
                products = products
            )
        }
    }
    fun addOrder() {
        val order = currentState.toOrder()
        viewModelScope.launch {
            repository.insertOrder(order, ordersProducts)
        }
    }

    fun createOrderProduct(title: String, weight: Int, units: String, price: Int) {
        val orderProductItem =
            OrderProductItem(
                title = title,
                weight = weight,
                units = units,
                price = price
            )
        ordersProducts.add(orderProductItem)
        _state.value =
            currentState.copy(products = ordersProducts.toList())
    }
    fun removeOrder(id: Long) {
        viewModelScope.launch{
            repository.removeOrder(id = id)
            repository.loadOrders()
        }
    }
}

data class OrderState(
    val id: Long = 0,
    val customer: String = "",
    val phone: String? = null,
    val deadline: Date? = null,
    val needDelivery: Boolean = false,
    val address: String? = null,
    val availableProducts: List<ProductItem> = listOf(),
    val products: List<OrderProductItem> = listOf(),
    val price: Int = 0,
    val isPaid: Boolean = false,
    val note: String? = "",
    var isCooked: Boolean = false
)

fun OrderState.toOrder() = Order(
    id,
    customer,
    phone,
    deadline,
    needDelivery,
    address,
    price,
    isPaid,
    note,
    isCooked
)