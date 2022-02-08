package ru.churkin.confectioners_organizer.view_models.ingredient

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.churkin.confectioners_organizer.date.format
import ru.churkin.confectioners_organizer.date.parseDate
import ru.churkin.confectioners_organizer.view_models.recept.ReceptState
import java.util.*

class IngredientViewModel() : ViewModel() {
    private val _state: MutableStateFlow<IngredientState> = MutableStateFlow(IngredientState())

    val state: StateFlow<IngredientState>
        get() = _state

    val currentState: IngredientState
        get() = state.value

    fun updateTitle (title: String){
        _state.value = currentState.copy(title = title)
    }

    fun updateAvailability (availability: Boolean){
        _state.value = currentState.copy(availability = availability)
    }

    fun updateAvailable (available: Int){
        _state.value = currentState.copy(available = available)
    }

    fun updateUnitsAvailable (unitsAvailable: String){
        _state.value = currentState.copy(unitsAvailable = unitsAvailable)
    }

    fun updateUnitsPrice (unitsPrice: String){
        _state.value = currentState.copy(unitsPrice = unitsPrice)
    }

    fun updatecostPrice (costPrice: Float){
        _state.value = currentState.copy(costPrice = costPrice)
    }

    fun updateSellBy (sellBy: String){
        if (sellBy.isEmpty()) return
        _state.value = currentState.copy(sellBy = sellBy.parseDate())
    }

    fun emptyState (){
        _state.value = currentState.copy(0, "", false, 0, "ед. изм.", "рубль за ______", 0f, null)
    }
}

data class IngredientState(
    val id: Int = 0,
    val title: String = "",
    val availability: Boolean = false,
    val available: Int = 0,
    var unitsAvailable: String = "ед. изм.",
    var unitsPrice: String = "рубль за ______",
    val costPrice: Float = 0f,
    val sellBy: Date? = null
)