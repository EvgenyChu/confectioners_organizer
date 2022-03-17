package ru.churkin.confectioners_organizer.view_models.list_orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.local.db.entity.Order
import ru.churkin.confectioners_organizer.repositories.OrdersRepository
import ru.churkin.confectioners_organizer.view_models.list_recepts.ReceptsState

class OrdersViewModel() : ViewModel() {
    private val repository: OrdersRepository = OrdersRepository()

    private val _state = MutableStateFlow(initialState())

    private val _searchText = MutableStateFlow("")

    val state: StateFlow<OrdersState>
        get() = _state

    private val currentState: OrdersState
        get() = _state.value

    private fun initialState(): OrdersState {
        return OrdersState.Empty
    }

    val searchText
        get() = _searchText

    init {
        viewModelScope.launch {
            val orders = repository.loadOrders()
            _state.value = if (orders.isEmpty()) OrdersState.Empty
                else OrdersState.Value(orders)
        }
    }

    fun searchOrders(query: String) {
        _searchText.value = query
        _state.value = OrdersState.Loading
        viewModelScope.launch {
            val orders =
                if (query.isEmpty()) repository.loadOrders() else repository.searchOrder(query)
            Log.e("search", orders.toString())
            _state.value = if (orders.isEmpty()) OrdersState.Empty
            else OrdersState.Value(orders)
        }
    }

    fun removeOrder(id: Long) {
        _state.value = OrdersState.Loading
        viewModelScope.launch{
            repository.removeOrder(id = id)
            repository.removeOrderProduct(id = id)
            val orders = repository.loadOrders()
            _state.value = if (orders.isEmpty()) OrdersState.Empty
            else OrdersState.Value(orders)
        }
    }
}

sealed class OrdersState {
    object Loading : OrdersState()
    object Empty : OrdersState()
    data class Value(val orders: List<Order>) : OrdersState()
}