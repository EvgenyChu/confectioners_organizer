package ru.churkin.confectioners_organizer.listRecepts

import ru.churkin.confectioners_organizer.view_models.recept.data.Recept

sealed class RecsState {
    object Loading : RecsState()
    object Empty : RecsState()
    data class Value(val recs: List<Recept>) : RecsState()
    data class ValueWithMessage(val recs: List<Recept>, val message: String = "Any message") :
        RecsState()
}