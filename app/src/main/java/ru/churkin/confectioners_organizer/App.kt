package ru.churkin.confectioners_organizer

import android.app.Application
import android.content.Context
import android.util.Log

class App() : Application() {
    companion object {
        private var instance: App? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }


    }

    init {
        instance = this
    }


    override fun onCreate() {
        super.onCreate()
        Log.e("App", "Application created")
    }
}