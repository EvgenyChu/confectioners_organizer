package ru.churkin.confectioners_organizer.view_models.recept

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient
import ru.churkin.confectioners_organizer.local.db.entity.Recept
import ru.churkin.confectioners_organizer.local.db.entity.ReceptIngredientItem
import ru.churkin.confectioners_organizer.repositories.ReceptsRepository

class ReceptViewModel() : ViewModel() {
    private val repository: ReceptsRepository = ReceptsRepository()

    private val receptsIngredients: MutableList<ReceptIngredientItem> = mutableListOf()

    private val _state: MutableStateFlow<ReceptState> = MutableStateFlow(initialState())

    val state: StateFlow<ReceptState>
        get() = _state

    val currentState: ReceptState
        get() = state.value

    init {
        viewModelScope.launch {
            val ingredients =
                repository.loadIngredients().map { IngredientItem(it.title, it.availability) }
            _state.value = currentState.copy(totalIngredients = ingredients)
        }
    }

    private fun initialState(): ReceptState {
        return ReceptState()
    }

    fun hideCreateDialog() {
        _state.value = currentState.copy(isCreateDialog = false)
    }

    fun showCreateDialog() {
        _state.value = currentState.copy(isCreateDialog = true)
    }

    fun updateTitle(title: String) {
        _state.value = currentState.copy(title = title)
    }

    fun updateWeight(weight: Int) {
        _state.value = currentState.copy(weight = weight)
    }

    fun updateTime(time: Int) {
        _state.value = currentState.copy(time = time)
    }

    fun updateNote(note: String) {
        _state.value = currentState.copy(note = note)
    }

    fun updateListIngredientForRecet(listIngredients: List<IngredientItem>) {
        _state.value = currentState.copy(totalIngredients = listIngredients)
    }


    fun addRecept(
        title: String,
        weight: Int,
        time: Int,
        note: String

    ) {
        val recept = Recept(
            title = title,
            weight = weight,
            time = time,
            note = note
        )
        viewModelScope.launch {
            repository.insertRecept(recept, receptsIngredients)
        }
    }

    fun createReceptIngredient(title: String, count: Int, availability: Boolean) {
        val receptIngredientItem =
            ReceptIngredientItem(
                title = title,
                availability = availability,
                count = count
            )
        receptsIngredients.add(receptIngredientItem)
        /*viewModelScope.launch {
            repository.insertReceptIngredientItem(receptIngredientItem)
        }*/
    }

    fun loadReceptIngredient() {
        viewModelScope.launch {
            _state.value = currentState.copy(ingredients = repository.loadReceptIngredientItem())
            repository.loadReceptIngredientItem()
        }
    }
}

    data class ReceptState(
        val id: Int = 0,
        val title: String = "",
        val weight: Int = 0,
        val time: Int = 0,
        val totalIngredients: List<IngredientItem> = emptyList(),
        val ingredients: List<ReceptIngredientItem> = emptyList(),
        val note: String = "",
        val isCreateDialog: Boolean = false,
        val isConfirm: Boolean = false,
        val available: Int = 0
    ) {
        companion object Factory {

            private var lastId: Int = -1

            fun makeRecept(
                title: String,
                weight: Int,
                time: Int,
                listIngredients: List<IngredientItem>,
                note: String

            ): ReceptState {
                lastId += 1

                return ReceptState(
                    id = lastId,
                    title = title,
                    weight = weight,
                    time = time,
                    totalIngredients = listIngredients,
                    note = note
                )
            }
        }
    }



