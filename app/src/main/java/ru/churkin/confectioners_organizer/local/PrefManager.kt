package ru.churkin.confectioners_organizer.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import ru.churkin.confectioners_organizer.App
import ru.churkin.confectioners_organizer.view_models.ingredient.IngredientState

object PrefManager {

        private val context = App.applicationContext()
        private val prefs: SharedPreferences =
            context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    fun insertIngredient(ingredient: IngredientState) {
        val ingredients = loadIngredients().plus(ingredient)
        val str = Json.encodeToString(ListSerializer(IngredientState.serializer()), ingredients)

        prefs.edit()
            .putString("MY_INGREDIENTS", str)
            .apply()
    }

    fun loadIngredients(): List<IngredientState> {
        val str = prefs.getString("MY_INGREDIENTS", null)

        str ?: return emptyList()
        val ingredients = Json.decodeFromString(ListSerializer(IngredientState.serializer()), str)
        return ingredients
    }

    fun removeIngredient(id: Int) {
        val curIngredients = loadIngredients()

        val index = curIngredients.indexOfFirst { it.id == id }
        val ingridients = curIngredients.toMutableList()
        ingridients.removeAt(index)
        val str = Json.encodeToString(ListSerializer(IngredientState.serializer()), ingridients)
        prefs.edit()
            .putString("MY_INGRIDIENTS", str)
            .apply()
    }
}