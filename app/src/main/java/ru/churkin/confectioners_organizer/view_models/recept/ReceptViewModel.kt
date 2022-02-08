package ru.churkin.confectioners_organizer.view_models.recept

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReceptViewModel() : ViewModel() {
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
}

data class ReceptState(
    val id: Int = 0,
    val title: String = "",
    var weight: Int = 0,
    val time: Int = 0
)