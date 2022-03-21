package ru.churkin.confectioners_organizer.view_models.recept

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.local.db.entity.Recept
import ru.churkin.confectioners_organizer.local.db.entity.ReceptIngredientItem
import ru.churkin.confectioners_organizer.repositories.ReceptsRepository
import ru.churkin.confectioners_organizer.view_models.ingredient.IngredientState
import ru.churkin.confectioners_organizer.view_models.list_recepts.ReceptsState

@InternalCoroutinesApi
class CreateReceptViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private var id: Long? = savedStateHandle.get<Long>("id")
    private val repository: ReceptsRepository = ReceptsRepository()
    private val receptsIngredients: MutableList<ReceptIngredientItem> = mutableListOf()
    private val _state: MutableStateFlow<ReceptState> = MutableStateFlow(ReceptState())

    val state: StateFlow<ReceptState>
        get() = _state

    val currentState: ReceptState
        get() = state.value

    init {
        viewModelScope.launch {
            if (id == null) {
                val localId = repository.createRecept()
                _state.value = currentState.copy(id = localId)
                id = localId
            } else {
                val recept = repository.loadRecept(checkNotNull(id))
                _state.value = currentState.copy(
                    id = recept.id,
                    title = recept.title,
                    weight = recept.weight,
                    time = recept.time,
                    note = recept.note
                )
            }

            val ingredients = repository.loadReceptIngredients(currentState.id)
            val availableIngredients =
                repository.loadIngredients().map { IngredientItem(it.title, it.availability, it.unitsAvailable) }

            _state.value = currentState.copy(
                availableIngredients = availableIngredients,
                ingredients = ingredients
            )
        }
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

    fun emptyState() {
        _state.value = ReceptState(id = currentState.id)
    }


    fun addRecept() {
        val recept = currentState.toRecept()
        viewModelScope.launch {
            repository.insertRecept(recept, receptsIngredients)
        }
    }

    fun createReceptIngredient(title: String, count: Int, availability: Boolean, unitsAvailable: String) {
        viewModelScope.launch {
            val receptIngredientItem =
                ReceptIngredientItem(
                    title = title,
                    availability = availability,
                    count = count,
                    unitsAvailable = unitsAvailable,
                    receptId = currentState.id
                )
            repository.insertReceptIngredientItem(receptIngredientItem)
            val ingredients = repository.loadReceptIngredients(currentState.id)
            var availabilityIngredients: Boolean = true
                ingredients.forEach { if (!it.availability)  availabilityIngredients = false}
            _state.value =
                currentState.copy(
                    ingredients = ingredients,
                    availabilityIngredients = availabilityIngredients,
                    isCreateDialog = false
                )
        }
    }
    fun removeRecept(id: Long) {
        viewModelScope.launch{
            repository.removeRecept(id = id)
            repository.loadRecepts()
        }
    }

    fun removeReceptIngredient(id: Long) {
        viewModelScope.launch {
            repository.removeReceptIngredient(id = id)
            val ingredients = repository.loadReceptIngredients(currentState.id)
            _state.value = currentState.copy(ingredients = ingredients)
        }
    }
}

data class ReceptState(
    val id: Long = 0,
    val title: String = "",
    val weight: Int = 0,
    val time: Int = 0,
    val availableIngredients: List<IngredientItem> = emptyList(),
    val ingredients: List<ReceptIngredientItem> = emptyList(),
    val note: String = "",
    val isCreateDialog: Boolean = false,
    val isConfirm: Boolean = false,
    val available: Int = 0,
    val availabilityIngredients: Boolean = true
)

fun ReceptState.toRecept()  = Recept(id, title, weight, time, note, availabilityIngredients)





