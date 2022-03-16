package ru.churkin.confectioners_organizer.view_models.order.data

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.repositories.OrdersRepository
import ru.churkin.confectioners_organizer.view_models.recept.ReceptState

class OrderViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val id : Long?  = savedStateHandle.get<Long>("id")
    private val repository: OrdersRepository = OrdersRepository()
    private val _state: MutableStateFlow<OrderState> = MutableStateFlow(OrderState())

    val state: StateFlow<OrderState>
        get() = _state

    val currentState: OrderState
        get() = state.value

    suspend fun initState() {

        checkNotNull(id)

        viewModelScope.launch {
            val order = repository.loadOrderFull(id)

            _state.value = currentState.copy(
                id = order.id,
                customer = order.customer,
                phone = order.phone,
                deadLine = order.deadline,
                needDelivery = order.needDelivery,
                address = order.address,
                products = order.listProducts,
                price = order.price,
                isPaid = order.isPaid,
                note = order.note
            )
        }
    }
}