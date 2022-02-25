package ru.churkin.confectioners_organizer.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.serialization.Serializable
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient

@Entity(tableName = "recepts")
data class Recept(
    @PrimaryKey(autoGenerate = true)
    val id: Int  = 0,
    val title: String = "",
    val weight: Int = 0,
    val time: Int = 0,
    val note: String = "Примечание"
)

data class ReceptFull(
    val id: Int  = 0,
    val title: String = "",
    val weight: Int = 0,
    val time: Int = 0,
    @Relation(
        parentColumn = "id",
        entityColumn = "recept_id"
    )
    val listIngredients: List<ReceptIngredientItem>? = emptyList(),
    val note: String = "Примечание"
)