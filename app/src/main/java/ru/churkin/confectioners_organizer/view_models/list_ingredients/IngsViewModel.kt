package ru.churkin.confectioners_organizer.view_models.list_ingredients

import android.util.Log
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

    private val _searchText = MutableStateFlow("")

    val state: StateFlow<IngredientsState>
        get() = _state

    private val currentState: IngredientsState
        get() = _state.value

    private fun initialState(): IngredientsState {

        return IngredientsState.Empty
    }

    val searchText
        get() = _searchText

    init {
        viewModelScope.launch {
            val ingredients = repository.loadIngredients()
            _state.value = if (ingredients.isEmpty()) IngredientsState.Empty
                else IngredientsState.Value(ingredients)
        }
    }

    fun searchIngredients(query: String) {
        _searchText.value = query
        _state.value = IngredientsState.Loading
        viewModelScope.launch {
            val ingredients =
                if (query.isEmpty()) repository.loadIngredients() else repository.searchIngredient(query)
            Log.e("search", ingredients.toString())
            _state.value = if (ingredients.isEmpty()) IngredientsState.Empty
            else IngredientsState.Value(ingredients)
        }
    }

    fun removeIngredient(id: Long) {
        _state.value = IngredientsState.Loading
        viewModelScope.launch{
            repository.removeIngredient(id = id)
            val ingredients = repository.loadIngredients()
            _state.value = if (ingredients.isEmpty()) IngredientsState.Empty
            else IngredientsState.Value(ingredients)
        }
    }
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