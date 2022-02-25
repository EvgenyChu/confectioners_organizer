package ru.churkin.confectioners_organizer.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "recepts")
data class Recept(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val weight: Int = 0,
    val time: Int = 0,
    val note: String = ""
)

data class ReceptFull(
    val id: Long = 0,
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