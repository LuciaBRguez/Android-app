package com.example.naturzaragoza

import android.app.Application
import com.example.naturzaragoza.data.DataBase.AppDatabase

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
        // Room
        var database: AppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}