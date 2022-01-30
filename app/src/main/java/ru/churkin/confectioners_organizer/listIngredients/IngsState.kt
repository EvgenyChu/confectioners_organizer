package ru.churkin.confectioners_organizer.listIngredients

import ru.churkin.confectioners_organizer.ingredient.Ingredient

sealed class IngsState {
        object Loading : IngsState()
        object Empty : IngsState()
        data class Value(val ings: List<Ingredient>) : IngsState()
        data class ValueWithMessage(val ings: List<Ingredient>, val message: String = "Any message") :
            IngsState()

}