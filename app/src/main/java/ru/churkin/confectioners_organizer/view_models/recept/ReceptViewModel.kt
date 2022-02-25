package ru.churkin.confectioners_organizer.view_models.recept

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient
import ru.churkin.confectioners_organizer.local.db.entity.Recept
import ru.churkin.confectioners_organizer.local.db.entity.ReceptIngredientItem
import ru.churkin.confectioners_organizer.repositories.ReceptsRepository

@InternalCoroutinesApi
class ReceptViewModel() : ViewModel() {
    private val repository: ReceptsRepository = ReceptsRepository()

    private val receptsIngredients: MutableList<ReceptIngredientItem> = mutableListOf()

    private val _state: MutableStateFlow<ReceptState> = MutableStateFlow(ReceptState())

    val state: StateFlow<ReceptState>
        get() = _state

    val currentState: ReceptState
        get() = state.value

    init {
        viewModelScope.launch {
            val loadRecept = repository.loadReceptIngredientItem()
            val ingredients =
                repository.loadIngredients().map { IngredientItem(it.title, it.availability) }

                _state.value = currentState.copy(totalIngredients = ingredients, ingredients = loadRecept)

            Log.e("vm", currentState.totalIngredients.toString())
            state.collect(object : FlowCollector<ReceptState>{
                override suspend fun emit(value: ReceptState) {
                    Log.e("state", value.toString())
                }
            })
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
    )



