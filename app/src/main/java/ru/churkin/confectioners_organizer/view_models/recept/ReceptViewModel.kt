package ru.churkin.confectioners_organizer.view_models.recept

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.churkin.confectioners_organizer.view_models.ingredient.IngredientState
import ru.churkin.confectioners_organizer.view_models.ingredient.data.Ingredient
import ru.churkin.confectioners_organizer.view_models.ingredient.data.IngredientsRepository
import ru.churkin.confectioners_organizer.view_models.recept.data.Recept
import ru.churkin.confectioners_organizer.view_models.recept.data.ReceptsRepository
import java.util.*

class ReceptViewModel() : ViewModel() {
    val repository: ReceptsRepository = ReceptsRepository()

    private val _state: MutableStateFlow<ReceptState> = MutableStateFlow(ReceptState())

    val state: StateFlow<ReceptState>
        get() = _state

    val currentState: ReceptState
        get() = state.value

    fun updateTitle (title: String){
        _state.value = currentState.copy(title = title)
    }

   fun updateWeight (weight: Int){
        _state.value = currentState.copy(weight = weight)
    }

    fun updateTime (time: Int){
        _state.value = currentState.copy(time = time)
    }

    fun addRecept(
        title: String,
        weight: Int,
        time: Int,
        listIngredients: List<Ingredient>,
        note: String

    ) {
        val recept= Recept(
            title = title,
            weight = weight,
            time = time,
            listIngredients = listIngredients,
            note = note
        )
        repository.insertRecept(recept)
    }
}

data class ReceptState(
    val id: Int = 0,
    val title: String = "",
    val weight: Int = 0,
    val time: Int = 0,
    val listIngredients: List<Ingredient> = listOf(),
    val note: String = "Примечание"
) {
    companion object Factory {

        private var lastId: Int = -1

        fun makeRecept(
            title: String,
            weight: Int,
            time: Int,
            listIngredients: List<Ingredient>,
            note: String

        ): ReceptState {
            lastId += 1

            return ReceptState(
                id = lastId,
                title = title,
                weight = weight,
                time = time,
                listIngredients = listIngredients,
                note = note
            )
        }
    }
}