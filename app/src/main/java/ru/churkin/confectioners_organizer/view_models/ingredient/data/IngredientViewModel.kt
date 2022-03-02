package ru.churkin.confectioners_organizer.view_models.ingredient.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.repositories.IngredientsRepository
import ru.churkin.confectioners_organizer.view_models.ingredient.IngredientState
import ru.churkin.confectioners_organizer.view_models.recept.ReceptState

class IngredientViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val id: Long? = savedStateHandle.get<Long>("id")
    private val repository: IngredientsRepository = IngredientsRepository()
    private val _state: MutableStateFlow<IngredientState> = MutableStateFlow(IngredientState())

    val state: StateFlow<IngredientState>
        get() = _state

    val currentState: IngredientState
        get() = state.value

    init {
        checkNotNull(id)

        viewModelScope.launch {
            val ingredient = repository.loadIngredient(id)

            _state.value = currentState.copy(
                id = ingredient.id,
                title = ingredient.title,
                availability = ingredient.availability,
                available = ingredient.available,
                unitsAvailable = ingredient.unitsAvailable,
                unitsPrice = ingredient.unitsPrice,
                _costPrice = ingredient.costPrice.toString(),
                sellBy = ingredient.sellBy
                )
        }
    }
}