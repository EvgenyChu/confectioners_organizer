package ru.churkin.confectioners_organizer.view_models.list_recepts

import androidx.lifecycle.ViewModel
import ru.churkin.confectioners_organizer.listRecepts.RecsState
import ru.churkin.confectioners_organizer.view_models.recept.data.Recept

class RecsViewModel(): ViewModel() {
    data class RecsScreenState(
        val title: String = "Рецепты",
        val recs: List<Recept>,
        val recsState: RecsState = RecsState.Empty,
        val isConfirm: Boolean = false,
        val receptsIdForRemove: Int? = null
    )
}