package ru.churkin.confectioners_organizer.view_models.list_ingredients

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.churkin.confectioners_organizer.view_models.ingredient.IngredientState
import ru.churkin.confectioners_organizer.view_models.ingredient.data.Ingredient
import ru.churkin.confectioners_organizer.view_models.ingredient.data.IngredientsRepository

class ListIngsViewModel() : ViewModel() {

    val repository: IngredientsRepository = IngredientsRepository()

    val screenState = MutableStateFlow(initialState())

    private val currentState: ListIngsState
        get() = screenState.value

    private fun initialState(): ListIngsState {

        return ListIngsState(
            ingredientsState = IngredientsState.Value(repository.loadIngredients()),
            ingredients = emptyList()
        )
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
    data class ValueWithMessage(val indigrients: List<Ingredient>, val message: String = "Any message") :
        IngredientsState()
}