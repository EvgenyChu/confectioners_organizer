package ru.churkin.confectioners_organizer.view_models.recept

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.local.db.entity.Recept
import ru.churkin.confectioners_organizer.local.db.entity.ReceptIngredientItem
import ru.churkin.confectioners_organizer.repositories.ReceptsRepository

@InternalCoroutinesApi
class CreateReceptViewModel() : ViewModel() {
    private val repository: ReceptsRepository = ReceptsRepository()
    private val receptsIngredients: MutableList<ReceptIngredientItem> = mutableListOf()
    private val _state: MutableStateFlow<ReceptState> = MutableStateFlow(ReceptState())

    val state: StateFlow<ReceptState>
        get() = _state

    val currentState: ReceptState
        get() = state.value

    suspend fun initState(id: Long?) {
        viewModelScope.launch {
            if (id == null) {
                val localId = repository.createRecept()
                val recept = ReceptState()
                _state.value = currentState.copy(
                    id = localId,
                    title = recept.title,
                    weight = recept.weight,
                    time = recept.time,
                    note = recept.note,
                    costPrice = recept.costPrice,
                    isCreateDialog = recept.isCreateDialog,
                    isConfirm = recept.isConfirm,
                    availabilityIngredients = recept.availabilityIngredients,
                    availableIngredients = recept.availableIngredients,
                    ingredients = recept.ingredients
                    )
            } else {
                val recept = repository.loadRecept(checkNotNull(id))
                _state.value = currentState.copy(
                    id = recept.id,
                    title = recept.title,
                    weight = recept.weight,
                    time = recept.time,
                    note = recept.note,
                    costPrice = recept.costPrice
                )
            }

            val ingredients = repository.loadReceptIngredients(currentState.id)
            val availableIngredients =
                repository.loadIngredients()
                    .map { IngredientItem(it.title, it.availability, it.unitsAvailable, it.costPrice) }

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
        viewModelScope.launch {
            val ingredients = repository.loadReceptIngredients(currentState.id)
            var costPrice = 0f
            ingredients.forEach { costPrice += it.costPrice * it.count }
            costPrice = if (currentState.weight != 0) costPrice/currentState.weight
            else costPrice
            val recept = currentState.copy(costPrice = costPrice).toRecept()
            repository.insertRecept(recept, receptsIngredients)
        }
    }

    fun createReceptIngredient(
        title: String,
        count: Int,
        availability: Boolean,
        unitsAvailable: String,
        costPrice: Float
    ) {
        viewModelScope.launch {
            val receptIngredientItem =
                ReceptIngredientItem(
                    title = title,
                    availability = availability,
                    count = count,
                    unitsAvailable = unitsAvailable,
                    receptId = currentState.id,
                    costPrice = costPrice
                )
            repository.insertReceptIngredientItem(receptIngredientItem)
            val ingredients = repository.loadReceptIngredients(currentState.id)
            val missingReceptIngredients: MutableSet<String> = mutableSetOf()
            var availabilityIngredients: Boolean = true
            ingredients.forEach {
                if (!it.availability) {
                    availabilityIngredients = false
                    missingReceptIngredients += it.title.lowercase()
                }
            }
            _state.value =
                currentState.copy(
                    ingredients = ingredients,
                    availabilityIngredients = availabilityIngredients,
                    missingReceptIngredients = missingReceptIngredients.distinct().joinToString(",") { it },
                    isCreateDialog = false
                )
        }
    }

    fun removeRecept(id: Long) {
        viewModelScope.launch {
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
    val availabilityIngredients: Boolean = true,
    val costPrice: Float = 0f,
    val missingReceptIngredients: String = ""
)

fun ReceptState.toRecept() =
    Recept(
        id,
        title,
        weight,
        time,
        note,
        availabilityIngredients,
        costPrice,
        missingReceptIngredients
    )





