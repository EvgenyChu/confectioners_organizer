package ru.churkin.confectioners_organizer.view_models.list_recepts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.churkin.confectioners_organizer.local.db.entity.Recept
import ru.churkin.confectioners_organizer.repositories.ReceptsRepository
import ru.churkin.confectioners_organizer.view_models.recept.ReceptState

@OptIn(InternalCoroutinesApi::class)
class RecsViewModel() : ViewModel() {

    private val repository: ReceptsRepository = ReceptsRepository()

    private val _state = MutableStateFlow(initialState())

    private val _searchText = MutableStateFlow("")

    val state: StateFlow<ReceptsState>
        get() = _state

    private val currentState: ReceptsState
        get() = _state.value

    private fun initialState(): ReceptsState {

        return ReceptsState.Empty
    }

    val searchText
        get() = _searchText

    init {
        viewModelScope.launch {
            val recepts = repository.loadRecepts()
            _state.value = if (recepts.isEmpty()) ReceptsState.Empty
            else ReceptsState.Value(recepts)
        }
    }

    fun searchRecepts(query: String) {
        _searchText.value = query
        _state.value = ReceptsState.Loading
        viewModelScope.launch {
            val recepts =
                if (query.isEmpty()) repository.loadRecepts() else repository.searchRecept(query)
            Log.e("search", recepts.toString())
            _state.value = if (recepts.isEmpty()) ReceptsState.Empty
            else ReceptsState.Value(recepts)
        }
    }

    fun removeRecept(id: Long) {
        _state.value = ReceptsState.Loading
        viewModelScope.launch {
            repository.removeRecept(id = id)
            val recepts = repository.loadRecepts()
            _state.value = if (recepts.isEmpty()) ReceptsState.Empty
                else ReceptsState.Value(recepts)
        }
    }
}

sealed class ReceptsState {
    object Loading : ReceptsState()
    object Empty : ReceptsState()
    data class Value(val recepts: List<Recept>) : ReceptsState()
}