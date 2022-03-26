package ru.churkin.confectioners_organizer.view_models.recept

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.repositories.ReceptsRepository

class ReceptViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val id : Long?  = savedStateHandle.get<Long>("id")
    private val repository: ReceptsRepository = ReceptsRepository()
    private val _state: MutableStateFlow<ReceptState> = MutableStateFlow(ReceptState())

    val state: StateFlow<ReceptState>
        get() = _state

    val currentState: ReceptState
        get() = state.value

    suspend fun initState(id: Long) {
        Log.e("ReceptViewModel", "recept id $id")

        checkNotNull(id)

        viewModelScope.launch {
            val recept = repository.loadRecept(id)

            _state.value = currentState.copy(
                id = recept.id,
                title = recept.title,
                weight = recept.weight,
                time = recept.time,
                note = recept.note,
                ingredients = recept.listIngredients ?: emptyList()
            )
        }
    }
}