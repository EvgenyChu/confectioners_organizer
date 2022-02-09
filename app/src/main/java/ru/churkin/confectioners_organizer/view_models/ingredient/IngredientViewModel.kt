package ru.churkin.confectioners_organizer.view_models.ingredient

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.churkin.confectioners_organizer.date.parseDate
import java.util.*
import kotlinx.serialization.Serializable

class IngredientViewModel() : ViewModel() {
    private val _state: MutableStateFlow<IngredientState> = MutableStateFlow(IngredientState())

    val state: StateFlow<IngredientState>
        get() = _state

    val currentState: IngredientState
        get() = state.value

    fun updateTitle(title: String) {
        _state.value = currentState.copy(title = title)
    }

    fun updateAvailability(availability: Boolean) {
        _state.value = currentState.copy(availability = availability)
    }

    fun updateAvailable(available: Int) {
        _state.value = currentState.copy(available = available)
    }

    fun updateUnitsAvailable(unitsAvailable: String) {
        _state.value = currentState.copy(unitsAvailable = unitsAvailable)
    }

    fun updateUnitsPrice(unitsPrice: String) {
        _state.value = currentState.copy(unitsPrice = unitsPrice)
    }

    fun updatecostPrice(costPrice: String) {
        Log.e(
            "IngredientViewModel",
            "${isValidatePrice(costPrice)} $costPrice"
        )
        if (!isValidatePrice(costPrice)) {
            _state.value = currentState.copy(errors = mapOf("costPrice" to "invalid price"))
            return
        } else {
            _state.value = currentState.copy(errors = emptyMap())
        }

        _state.value = currentState.copy(_costPrice = costPrice)
    }

    private fun isValidatePrice(price: String) =
        price.isEmpty() || "^\\d*?\\.?\\d{0,2}$".toRegex().matches(price)

    fun updateSellBy(sellBy: String) {
        if (sellBy.isEmpty()) return
        _state.value = currentState.copy(sellBy = sellBy.parseDate())
    }

    fun emptyState() {
        _state.value = IngredientState()
    }
}

@Serializable
data class IngredientState(
    val id: Int = 0,
    val title: String = "",
    val availability: Boolean = false,
    val available: Int = 0,
    var unitsAvailable: String = "ед. изм.",
    var unitsPrice: String = "рубль за ______",
    val _costPrice: String = "",
    val sellBy: Date? = null,
    val errors: Map<String, String> = emptyMap()
) {
    val costPrice: Float
        get() = if(_costPrice.isEmpty()) 0f else _costPrice.toFloat()

    companion object Factory {

        private var lastId: Int = -1

        fun makeIngredient(title: String, availability: Boolean, available: Int, unitsAvailable: String, unitsPrice: String, _costPrice: String, sellBy: Date?): IngredientState {
            lastId += 1

            return IngredientState(
                id = lastId,
                title = title,
                availability = availability,
                available = available,
                unitsAvailable = unitsAvailable,
                unitsPrice = unitsPrice,
                _costPrice = _costPrice,
                sellBy = sellBy
            )
        }
    }
}