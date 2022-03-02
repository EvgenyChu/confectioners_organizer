package ru.churkin.confectioners_organizer.view_models.list_ingredients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient
import ru.churkin.confectioners_organizer.repositories.IngredientsRepository
import ru.churkin.confectioners_organizer.view_models.list_recepts.ReceptsState

class IngsViewModel() : ViewModel() {

    private val repository: IngredientsRepository = IngredientsRepository()

    private val _state = MutableStateFlow(initialState())

    val state: StateFlow<ListIngsState>
        get() = _state

    private val currentState: ListIngsState
        get() = _state.value

    private fun initialState(): ListIngsState {

        return ListIngsState(
            ingredientsState = IngredientsState.Loading,
            ingredients = emptyList()
        )
    }

    init {
        viewModelScope.launch {
            val ingredients = repository.loadIngredients()
            _state.value = currentState.copy(
                ingredientsState = if (ingredients.isEmpty()) IngredientsState.Empty
                else IngredientsState.Value(ingredients),
                ingredients = ingredients
            )
        }
    }

    fun removeIngredient(id: Long) {
        viewModelScope.launch{
            repository.removeIngredient(id = id)
            val ingredients = repository.loadIngredients()
            _state.value = currentState.copy(ingredientsState = if (ingredients.isEmpty()) IngredientsState.Empty
            else IngredientsState.Value(ingredients),
                ingredients = ingredients)
        }
    }

    data class ListIngsState(
        val ingredients: List<Ingredient>,
        val ingredientsState: IngredientsState = IngredientsState.Empty
    )
}

sealed class IngredientsState {
    object Loading : IngredientsState()
    object Empty : IngredientsState()
    data class Value(val ingredients: List<Ingredient>) : IngredientsState()
    data class ValueWithMessage(
        val indigrients: List<Ingredient>,
        val message: String = "Any message"
    ) :
        IngredientsState()
}