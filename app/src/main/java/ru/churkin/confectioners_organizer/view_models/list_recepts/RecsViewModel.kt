package ru.churkin.confectioners_organizer.view_models.list_recepts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.local.db.entity.Recept
import ru.churkin.confectioners_organizer.repositories.ReceptsRepository

class RecsViewModel(): ViewModel() {

    private val repository: ReceptsRepository = ReceptsRepository()

    private val _state = MutableStateFlow(initialState())

    val state: StateFlow<RecsViewModel.ListRecsState>
        get() = _state

    private val currentState: RecsViewModel.ListRecsState
        get() = _state.value

    private fun initialState(): RecsViewModel.ListRecsState {

        return RecsViewModel.ListRecsState(
            receptsState = ReceptsState.Loading,
            recepts = emptyList()
        )
    }

    init {
        viewModelScope.launch {
            val recepts = repository.loadRecepts()
            _state.value = currentState.copy(
                receptsState = if (recepts.isEmpty()) ReceptsState.Empty
                else ReceptsState.Value(recepts),
                recepts = recepts
            )
        }
    }

    fun removeRecept(id: Long) {
        viewModelScope.launch{
            repository.removeRecept(id = id)
            val recepts = repository.loadRecepts()
            _state.value = currentState.copy(receptsState = if (recepts.isEmpty()) ReceptsState.Empty
            else ReceptsState.Value(recepts),
                recepts = recepts)
        }
    }

    data class ListRecsState(
        val recepts: List<Recept>,
        val receptsState: ReceptsState = ReceptsState.Empty
    )
}

sealed class ReceptsState {
    object Loading : ReceptsState()
    object Empty : ReceptsState()
    data class Value(val recepts: List<Recept>) : ReceptsState()
    data class ValueWithMessage(
        val recepts: List<Recept>,
        val message: String = "Any message"
    ) :
        ReceptsState()
}