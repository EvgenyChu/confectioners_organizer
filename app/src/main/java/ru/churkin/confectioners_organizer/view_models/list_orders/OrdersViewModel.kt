package ru.churkin.confectioners_organizer.view_models.list_orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.date.parseDate
import ru.churkin.confectioners_organizer.local.db.entity.Order
import ru.churkin.confectioners_organizer.repositories.OrdersRepository

class OrdersViewModel() : ViewModel() {
    private val repository: OrdersRepository = OrdersRepository()

    private val _state = MutableStateFlow(initialState())

    private val _searchText = MutableStateFlow("")

    var isShowDate = MutableStateFlow(false)

    val state: StateFlow<OrdersState>
        get() = _state

    private fun initialState(): OrdersState {
        return OrdersState.Empty
    }

    val searchText
        get() = _searchText

    suspend fun initState() {
        viewModelScope.launch {
            _state.value = OrdersState.Loading
            val orders = repository.loadOrdersIsCooked(false)
            _state.value = if (orders.isEmpty()) OrdersState.Empty
            else OrdersState.Value(orders)
        }
    }

    fun currentOrdersState(){
        viewModelScope.launch {
            val orders = repository.loadOrdersIsCooked(false)
            _state.value = if (orders.isEmpty()) OrdersState.Empty
            else OrdersState.Value(orders)
        }
    }

    fun searchOrders(query: String) {
        _searchText.value = query
        _state.value = OrdersState.Loading
        viewModelScope.launch {
            val orders =
                if (query.isEmpty()) repository.loadOrdersIsCooked(false) else repository.searchOrder(query)
            Log.e("search", orders.toString())
            _state.value = if (orders.isEmpty()) OrdersState.Empty
            else OrdersState.Value(orders)
        }
    }

    fun removeOrder(id: Long) {
        _state.value = OrdersState.Loading
        viewModelScope.launch {
            val products = repository.loadOrderProducts(id)
            products.forEach {
                repository.removeOrderProductIngredient(it.id)
                repository.removeOrderProductRecept(it.id)
            }
            repository.removeOrderProducts(id = id)
            repository.removeOrder(id = id)
            val orders = repository.loadOrdersIsCooked(false)
            _state.value = if (orders.isEmpty()) OrdersState.Empty
            else OrdersState.Value(orders)
        }
    }

    fun searchDeadLine(date: String) {
        _state.value = OrdersState.Loading
        viewModelScope.launch {
            val orders = repository.findByDate(date.parseDate())
            if (orders.isEmpty()) _state.value = OrdersState.Empty
            else _state.value = OrdersState.Value(orders)
        }
    }
}

sealed class OrdersState {
    object Loading : OrdersState()
    object Empty : OrdersState()
    data class Value(val orders: List<Order>) : OrdersState()
}