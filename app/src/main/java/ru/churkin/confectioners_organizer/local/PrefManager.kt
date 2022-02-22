package ru.churkin.confectioners_organizer.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import ru.churkin.confectioners_organizer.App
import ru.churkin.confectioners_organizer.local.db.entity.Ingredient
import ru.churkin.confectioners_organizer.view_models.recept.ReceptIngredientItem
import ru.churkin.confectioners_organizer.view_models.recept.data.Recept


object PrefManager {

        private val context = App.applicationContext()
        private val prefs: SharedPreferences =
            context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)


    fun insertRecept(recept: Recept) {/*
        val recepts = loadRecepts().plus(recept)
        val str = Json.encodeToString(ListSerializer(Recept.serializer()), recepts)

        prefs.edit()
            .putString("MY_RECEPTS", str)
            .apply()*/
    }

    fun loadRecepts(): List<Recept> {/*
        val str = prefs.getString("MY_RECEPTS", null)

        str ?: return emptyList()
        val recepts = Json.decodeFromString(ListSerializer(Recept.serializer()), str)
        return recepts*/
        return emptyList()
    }

    fun removeRecept(id: Int) {/*
        val curRecepts = loadRecepts()

        val index = curRecepts.indexOfFirst { it.id == id }
        val recepts = curRecepts.toMutableList()
        recepts.removeAt(index)
        val str = Json.encodeToString(ListSerializer(Recept.serializer()), recepts)
        prefs.edit()
            .putString("MY_RECEPTS", str)
            .apply()*/
    }

    fun loadReceptIngredientItem(): List<ReceptIngredientItem> {
        val str = prefs.getString("RECEPT_INGREDIENTS", null)

        str ?: return emptyList()
        val ingredients = Json.decodeFromString(ListSerializer(ReceptIngredientItem.serializer()), str)
        return ingredients
    }

    fun insertReceptIngredientItem(receptIngredientItem: ReceptIngredientItem) {
        val receptsIngredientItems = loadReceptIngredientItem().plus(receptIngredientItem)
        val str = Json.encodeToString(ListSerializer(ReceptIngredientItem.serializer()), receptsIngredientItems)

        prefs.edit()
            .putString("RECEPT_INGREDIENTS", str)
            .apply()
    }
}