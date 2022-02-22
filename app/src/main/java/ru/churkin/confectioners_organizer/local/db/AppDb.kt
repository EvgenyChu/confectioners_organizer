package ru.churkin.confectioners_organizer.local.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.churkin.confectioners_organizer.App
import ru.churkin.confectioners_organizer.local.db.converter.Converters
import ru.churkin.confectioners_organizer.local.db.dao.IngredientDao
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient


@Database(entities = [Ingredient::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao
}

object AppDb{
    val db = Room
        .databaseBuilder(App.applicationContext(), AppDatabase::class.java, "Organizer")
        .build()
}