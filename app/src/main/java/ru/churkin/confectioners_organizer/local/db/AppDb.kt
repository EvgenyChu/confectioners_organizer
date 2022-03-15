package ru.churkin.confectioners_organizer.local.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.churkin.confectioners_organizer.App
import ru.churkin.confectioners_organizer.local.db.converter.Converters
import ru.churkin.confectioners_organizer.local.db.dao.*
import ru.churkin.confectioners_organizer.local.db.entity.*


@Database(
    entities = [
        Ingredient::class,
        Recept::class,
        ReceptIngredientItem::class,
        Order::class,
        Product::class,
        ProductReceptItem::class,
        ProductIngredientItem::class], version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao
    abstract fun receptDao(): ReceptDao
    abstract fun receptIngredientItemDao(): ReceptIngredientItemDao
    abstract fun orderDao(): OrderDao
    abstract fun productDao(): ProductDao
    abstract fun productReceptItemDao(): ProductReceptItemDao
    abstract fun productIngredientItemDao(): ProductIngredientItemDao
}

object AppDb {
    val db = Room
        .databaseBuilder(App.applicationContext(), AppDatabase::class.java, "Organizer")
        .build()
}