package ru.churkin.confectioners_organizer.local

import android.content.Context
import android.content.SharedPreferences
import ru.churkin.confectioners_organizer.App


object PrefManager {

        private val context = App.applicationContext()
        private val prefs: SharedPreferences =
            context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

}