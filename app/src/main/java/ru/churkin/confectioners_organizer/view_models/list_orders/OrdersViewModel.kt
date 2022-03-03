package ru.churkin.confectioners_organizer.view_models.list_orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.local.db.entity.Order
import ru.churkin.confectioners_organizer.repositories.OrdersRepository

class OrdersViewModel() : ViewModel() {
    private val repository: OrdersRepository = OrdersRepository()

    private val _state = MutableStateFlow(initialState())

    val state: StateFlow<OrdersViewModel.ListOrdersState>
        get() = _state

    private val currentState: OrdersViewModel.ListOrdersState
        get() = _state.value

    private fun initialState(): OrdersViewModel.ListOrdersState {

        return OrdersViewModel.ListOrdersState(
            ordersState = OrdersState.Loading,
            orders = emptyList()
        )
    }

    init {
        viewModelScope.launch {
            val orders = repository.loadOrders()
            _state.value = currentState.copy(
                ordersState = if (orders.isEmpty()) OrdersState.Empty
                else OrdersState.Value(orders),
                orders = orders
            )
        }
    }

    fun removeOrder(id: Long) {
        viewModelScope.launch{
            repository.removeOrder(id = id)
            val orders = repository.loadOrders()
            _state.value = currentState.copy(ordersState = if (orders.isEmpty()) OrdersState.Empty
            else OrdersState.Value(orders),
                orders = orders)
        }
    }

    data class ListOrdersState(
        val orders: List<Order>,
        val ordersState: OrdersState = OrdersState.Empty
    )
}

sealed class OrdersState {
    object Loading : OrdersState()
    object Empty : OrdersState()
    data class Value(val orders: List<Order>) : OrdersState()
    data class ValueWithMessage(
        val orders: List<Order>,
        val message: String = "Any message"
    ) :
        OrdersState()
}