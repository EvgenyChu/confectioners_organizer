package ru.churkin.confectioners_organizer.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import ru.churkin.confectioners_organizer.view_models.ingredient.DateSerializer
import java.util.*

@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val availability: Boolean = false,
    val available: Int = 0,
    val unitsAvailable: String = "ед. изм.",
    val unitsPrice: String = "рубль за ______",
    val costPrice: Float = 0f,
    val sellBy: Date? = null
)