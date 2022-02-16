package ru.churkin.confectioners_organizer.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import ru.churkin.confectioners_organizer.App
import ru.churkin.confectioners_organizer.view_models.ingredient.data.Ingredient
import ru.churkin.confectioners_organizer.view_models.recept.data.Recept


object PrefManager {

        private val context = App.applicationContext()
        private val prefs: SharedPreferences =
            context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    fun insertIngredient(ingredient: Ingredient) {
        val ingredients = loadIngredients().plus(ingredient)
        val str = Json.encodeToString(ListSerializer(Ingredient.serializer()), ingredients)

        prefs.edit()
            .putString("MY_INGREDIENTS", str)
            .apply()
    }

    fun loadIngredients(): List<Ingredient> {
        val str = prefs.getString("MY_INGREDIENTS", null)

        str ?: return emptyList()
        val ingredients = Json.decodeFromString(ListSerializer(Ingredient.serializer()), str)
        return ingredients
    }

    fun removeIngredient(id: Int) {
        val curIngredients = loadIngredients()

        val index = curIngredients.indexOfFirst { it.id == id }
        val ingridients = curIngredients.toMutableList()
        ingridients.removeAt(index)
        val str = Json.encodeToString(ListSerializer(Ingredient.serializer()), ingridients)
        prefs.edit()
            .putString("MY_INGREDIENTS", str)
            .apply()
    }

    fun insertRecept(recept: Recept) {
        val recepts = loadRecepts().plus(recept)
        val str = Json.encodeToString(ListSerializer(Recept.serializer()), recepts)

        prefs.edit()
            .putString("MY_RECEPTS", str)
            .apply()
    }

    fun loadRecepts(): List<Recept> {
        val str = prefs.getString("MY_RECEPTS", null)

        str ?: return emptyList()
        val recepts = Json.decodeFromString(ListSerializer(Recept.serializer()), str)
        return recepts
    }

    fun removeRecept(id: Int) {
        val curRecepts = loadRecepts()

        val index = curRecepts.indexOfFirst { it.id == id }
        val recepts = curRecepts.toMutableList()
        recepts.removeAt(index)
        val str = Json.encodeToString(ListSerializer(Recept.serializer()), recepts)
        prefs.edit()
            .putString("MY_RECEPTS", str)
            .apply()
    }
}