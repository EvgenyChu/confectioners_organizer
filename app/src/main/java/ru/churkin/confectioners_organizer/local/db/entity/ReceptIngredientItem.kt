package ru.churkin.confectioners_organizer.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recept_ingredient_items")
data class ReceptIngredientItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    val id: Int = 0,
    val title: String,
    val availability: Boolean,
    val count: Int = 0,
    // TODO: 25.02.2022 add forign key
    @ColumnInfo(name = "recept_id")
    val receptId: Int = 0
)
