package ru.churkin.confectioners_organizer.drawer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DrawerViewModel() : ViewModel(){

    private val _state: MutableStateFlow<DrawerState> = MutableStateFlow(DrawerState())

    val state: StateFlow<DrawerState>
        get() = _state

}

data class DrawerState(
    val isOpen: Boolean = false
)